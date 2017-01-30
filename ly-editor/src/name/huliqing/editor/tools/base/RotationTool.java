/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import com.jme3.input.KeyInput;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.edit.Mode;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.RotationControlObj;
import name.huliqing.editor.edit.UndoRedo;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.editor.edit.SimpleJmeEditListener;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.tools.EditTool;

/**
 * 旋转编辑工具
 * @author huliqing
 */
public class RotationTool extends EditTool implements SimpleJmeEditListener{
//    private static final Logger LOG = Logger.getLogger(RotationTool.class.getName());
    
    private final static String EVENT_ROTATION = "rotationEvent";
    private final static String EVENT_ROTATION_X = "rotationXEvent";
    private final static String EVENT_ROTATION_Y = "rotationYEvent";
    private final static String EVENT_ROTATION_Z = "rotationZEvent";
    
    private final static String EVENT_FREE_ROTATION_START = "freeRotationStartEvent";
    private final static String EVENT_FREE_ROTATION_CANCEL = "freeRotationCancelEvent";
    
    private final Ray ray = new Ray();
    private final Picker picker = new Picker();
    
    // 变换控制物体
    private final RotationControlObj controlObj = new RotationControlObj();
    // 当前操作的轴向
    private AxisNode rotationAxis;
    // 行为操作开始时编辑器中的被选择的物体，以及该物体的位置
    private SelectObj selectObj;
    
    // 开始变换时物体的位置(local)
    private Quaternion startRotate;
    private Quaternion startWorldRotate;
    // 经过旋转操作后的LocaleRotationl
    private Quaternion afterRotate;
    
    // 是否正在变换操作中
    private boolean transforming;
    // 自由旋转，相机视角
    private boolean freeRotation;
    // 按轴旋转
    private boolean axisRotation;

    public RotationTool(String name, String tips, String icon) {
        super(name, tips, icon);
        // 绑定三个按键：x,y,z用于在自由旋转的时候转化到按轴旋转
        bindEvent(EVENT_ROTATION_X).bindKey(KeyInput.KEY_X, false);
        bindEvent(EVENT_ROTATION_Y).bindKey(KeyInput.KEY_Y, false);
        bindEvent(EVENT_ROTATION_Z).bindKey(KeyInput.KEY_Z, false);
    }

    @Override
    public void initialize(SimpleJmeEdit jmeEdit, EditToolbar toolbar) {
        super.initialize(jmeEdit, toolbar);
        edit.getEditRoot().getParent().attachChild(controlObj);
        edit.addSimpleEditListener(this);
        updateMarkerState();
    }

    @Override
    public void cleanup() {
        endRotation();
        controlObj.removeFromParent();
        edit.removeEditFormListener(this);
        super.cleanup(); 
    }

    /**
     * 绑定一个按键用于打开旋转功能
     * @return 
     */
    public JmeEvent bindRotationEvent() {
        return bindEvent(EVENT_ROTATION);
    }
    
    /**
     * 绑定一个开始全局缩放的按键事件
     * @return 
     */
    public JmeEvent bindFreeRotationStartEvent() {
        return bindEvent(EVENT_FREE_ROTATION_START);
    }
    public JmeEvent bindFreeRotationCancelEvent() {
        return bindEvent(EVENT_FREE_ROTATION_CANCEL);
    }

    @Override
    protected void onToolEvent(Event e) {
        selectObj = edit.getSelected();
        if (selectObj == null || selectObj.getReadOnlySelectedSpatial() == null) {
            endRotation();
            return;
        }
        
        if (e.getName().equals(EVENT_ROTATION)) {
            if (e.isMatch()) {
                
                // 重要优化：如果当前正在自由操作，则直接结束操作，不要启动新的按轴操作。
                // 因为这个操作可能是要结束自由操作，而不是希望启动按轴操作, 如果是这样则会多出一个多余的历史记录。
                if (freeRotation) {
                    endRotation();
                    return;
                }
                
                PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
                AxisNode pickAxis = controlObj.getPickAxis(ray);
                if (pickAxis != null) {
                    startAxisRotation(pickAxis);
                }
            } else {
                endRotation();
            }
        }
        
        // 开始旋转
        else if (EVENT_FREE_ROTATION_START.equals(e.getName())) {
            if (e.isMatch()) {
                startFreeRotation();
            }
        } 
        // 取消旋转
        else if (transforming && EVENT_FREE_ROTATION_CANCEL.equals(e.getName())) { 
            if (e.isMatch()) {
                cancelRotation();
                // 销毁后续事件，注意确保不要误销毁其它正常事件
                e.setConsumed(true);
            }
        }
        
        // 从自由旋转转换到按轴旋转
        else if (transforming && e.isMatch() && EVENT_ROTATION_X.equals(e.getName())) {
            cancelRotation();
            startAxisRotation(controlObj.getAxisX());
        }
        else if (transforming && e.isMatch() && EVENT_ROTATION_Y.equals(e.getName())) {
            cancelRotation();
            startAxisRotation(controlObj.getAxisY());
        }
        else if (transforming && e.isMatch() && EVENT_ROTATION_Z.equals(e.getName())) {
            cancelRotation();
            startAxisRotation(controlObj.getAxisZ());
        }
    }
    
    private void startFreeRotation() {
        if (axisRotation) {
            endRotation();
        }
        transforming = true;
        freeRotation = true;
        axisRotation = false;
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), Mode.CAMERA
                , editor.getCamera(), editor.getInputManager().getCursorPosition(), Picker.PLANE_XY);
        startRotate = selectObj.getReadOnlySelectedSpatial().getLocalRotation().clone();
        startWorldRotate = selectObj.getReadOnlySelectedSpatial().getWorldRotation().clone();
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
    }

    private void startAxisRotation(AxisNode axis) {
        // 重要：这要避免自由操作和按轴操作时的冲突,因为自由操作按键可能和按轴操作按键冲突。
        // 比如：当自由操作通过按下“左键”来应用操作的时候，而这个按键又和按轴操作冲突，
        // 这时自由操作会直接变成按轴操作，而自由操作又来不及保存历史记录，
        // 这时候就导致“自由操作”丢失历史记录，而按轴操作又没有启动. 所以这里要FIX这个BUG。
        // 在按轴操作启动时要确保如果自由操作正在进行则需要保存历史记录再退出。
        if (freeRotation) {
            endRotation();
        }
        transforming = true;
        freeRotation = false;
        axisRotation = true;
        rotationAxis = axis;
        Quaternion planRotation = Picker.PLANE_XY;
        switch (axis.getType()) {
            case x:
                planRotation = Picker.PLANE_YZ;
                break;
            case y:
                planRotation = Picker.PLANE_XZ;
                break;
            case z:
                planRotation = Picker.PLANE_XY;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), edit.getMode()
                , editor.getCamera(), editor.getInputManager().getCursorPosition()
                ,planRotation);
        startRotate = selectObj.getReadOnlySelectedSpatial().getLocalRotation().clone();
        startWorldRotate = selectObj.getReadOnlySelectedSpatial().getWorldRotation().clone();
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        axis.setAxisLineVisible(true);
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
    }

    private void endRotation() {
        if (transforming) {
            picker.endPick();
            edit.addUndoRedo(new RotationUndoRedo(selectObj, startRotate, afterRotate));
        }
        transforming = false;
        freeRotation = false;
        axisRotation = false;
        rotationAxis = null;
        controlObj.setAxisVisible(true);
        controlObj.setAxisLineVisible(false);
    }
    
    private void cancelRotation() {
        if (transforming) {
            picker.endPick();
            selectObj.setLocalRotation(startRotate);
            transforming = false;
            // 更新一次位置,因为操作取消了
            updateMarkerState();
        }
        endRotation();
    }
    
    @Override
    public void update(float tpf) {
        // 对于相机视角，Marker必须实时随着相机的移动旋转而更新
        if (edit.getMode() == Mode.CAMERA) {
            updateMarkerState();
        }
        
        if (!transforming)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }

        Quaternion rotation = startRotate.mult(picker.getRotation(startWorldRotate.inverse()));
        selectObj.setLocalRotation(rotation);
        afterRotate = rotation;
        
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        updateMarkerState();
    }

    @Override
    public void onSelect(SelectObj selectObj) {
        updateMarkerState();
    }

    private void updateMarkerState() {
        if (edit.getSelected() == null) {
            controlObj.setVisible(false);
            return;
        }
        controlObj.setVisible(true);
        controlObj.setLocalTranslation(edit.getSelected().getReadOnlySelectedSpatial().getWorldTranslation());
        Mode mode = edit.getMode();
        switch (edit.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(edit.getSelected().getReadOnlySelectedSpatial().getWorldRotation());
                break;
            case CAMERA:
                controlObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
    
    private class RotationUndoRedo implements UndoRedo {
        private final SelectObj selectObj;
        private final Quaternion before = new Quaternion();
        private final Quaternion after = new Quaternion();
        public RotationUndoRedo(SelectObj selectObj, Quaternion before, Quaternion after) {
            this.selectObj = selectObj; 
            this.before.set(before);
            this.after.set(after);
        }
        
        @Override
        public void undo() {
            selectObj.setLocalRotation(before);
            updateMarkerState();
        }

        @Override
        public void redo() {
            selectObj.setLocalRotation(after);
            updateMarkerState();
        }
        
        @Override
        public String toString() {
            return "RotationUndoRedo:" + selectObj + ", before=" + before + ", after=" + after;
        }
    }
}
