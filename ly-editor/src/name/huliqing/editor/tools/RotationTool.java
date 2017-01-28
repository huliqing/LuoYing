/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

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

    public RotationTool(String name) {
        super(name);
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
                PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
                AxisNode pickAxis = controlObj.getPickAxis(ray);
                if (pickAxis != null) {
                    startRotation(pickAxis);
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
            startRotation(controlObj.getAxisX());
        }
        else if (transforming && e.isMatch() && EVENT_ROTATION_Y.equals(e.getName())) {
            cancelRotation();
            startRotation(controlObj.getAxisY());
        }
        else if (transforming && e.isMatch() && EVENT_ROTATION_Z.equals(e.getName())) {
            cancelRotation();
            startRotation(controlObj.getAxisZ());
        }
    }
    
    private void startFreeRotation() {
        freeRotation = true;
        transforming = true;
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), Mode.CAMERA
                , editor.getCamera(), editor.getInputManager().getCursorPosition(), Picker.PLANE_XY);
        startRotate = selectObj.getReadOnlySelectedSpatial().getLocalRotation().clone();
        startWorldRotate = selectObj.getReadOnlySelectedSpatial().getWorldRotation().clone();
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
    }

    private void startRotation(AxisNode rotationAxis) {
        this.rotationAxis = rotationAxis;
        freeRotation = false;
        transforming = true;
        Quaternion planRotation = Picker.PLANE_XY;
        switch (rotationAxis.getType()) {
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
        rotationAxis.setAxisLineVisible(true);
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
    }

    private void endRotation() {
        if (transforming) {
            picker.endPick();
            edit.addUndoRedo(new RotationUndoRedo(selectObj, startRotate, afterRotate));
        }
        transforming = false;
        freeRotation = false;
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
