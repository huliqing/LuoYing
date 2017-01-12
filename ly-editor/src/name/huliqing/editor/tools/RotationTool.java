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
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.RotationControlObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.luoying.manager.PickManager;
import name.huliqing.editor.edit.SimpleEditFormListener;

/**
 * 旋转编辑工具
 * @author huliqing
 */
public class RotationTool extends EditTool implements SimpleEditFormListener{
//    private static final Logger LOG = Logger.getLogger(RotationTool.class.getName());
    
    private final static String EVENT_ROTATION = "rotationEvent";
    private final static String EVENT_ROTATION_X = "rotationXEvent";
    private final static String EVENT_ROTATION_Y = "rotationYEvent";
    private final static String EVENT_ROTATION_Z = "rotationZEvent";
    
    private final static String EVENT_FREE_ROTATION_START = "freeRotationStartEvent";
    private final static String EVENT_FREE_ROTATION_APPLY = "freeRotationApplyEvent";
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
    private final Quaternion startRotate = new Quaternion();
    private final Quaternion startWorldRotate = new Quaternion();
    // 经过旋转操作后的LocaleRotationl
    private final Quaternion afterRotate = new Quaternion();
    
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
    public void initialize() {
        super.initialize();
        form.getEditRoot().getParent().attachChild(controlObj);
        form.addEditFormListener(this);
        updateMarkerState();
    }

    @Override
    public void cleanup() {
        controlObj.removeFromParent();
        form.removeEditFormListener(this);
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
    public JmeEvent bindFreeRotationApplyEvent() {
        return bindEvent(EVENT_FREE_ROTATION_APPLY);
    }
    public JmeEvent bindFreeRotationCancelEvent() {
        return bindEvent(EVENT_FREE_ROTATION_CANCEL);
    }

    @Override
    protected void onToolEvent(Event e) {
        selectObj = form.getSelected();
        if (selectObj == null) {
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
            // 应用旋转
        } else if (EVENT_FREE_ROTATION_APPLY.equals(e.getName())) {
            if (e.isMatch() && freeRotation) {
                endRotation();
            }
            // 取消旋转
        } else if (EVENT_FREE_ROTATION_CANCEL.equals(e.getName())) { 
            if (e.isMatch()) {
                cancelRotation();
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
        startRotate.set(selectObj.getReadOnlySelectedSpatial().getLocalRotation());
        startWorldRotate.set(selectObj.getReadOnlySelectedSpatial().getWorldRotation());
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
        picker.startPick(selectObj.getReadOnlySelectedSpatial(), form.getMode()
                , editor.getCamera(), editor.getInputManager().getCursorPosition()
                ,planRotation);
        startRotate.set(selectObj.getReadOnlySelectedSpatial().getLocalRotation());
        startWorldRotate.set(selectObj.getReadOnlySelectedSpatial().getWorldRotation());
        controlObj.setAxisVisible(false);
        controlObj.setAxisLineVisible(false);
        rotationAxis.setAxisLineVisible(true);
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
    }

    private void endRotation() {
        if (transforming) {
            picker.endPick();
            form.addUndoRedo(new RotationUndoRedo(selectObj, startRotate, afterRotate));
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
        if (form.getMode() == Mode.CAMERA) {
            updateMarkerState();
        }
        
        if (!transforming)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }

        Quaternion rotation = startRotate.mult(picker.getRotation(startWorldRotate.inverse()));
        selectObj.setLocalRotation(rotation);
        afterRotate.set(rotation);
        
    }
    
    @Override
    public void onModeChanged(Mode mode) {
        updateMarkerState();
    }

    @Override
    public void onSelectChanged(SelectObj selectObj) {
        updateMarkerState();
    }

    private void updateMarkerState() {
        if (form.getSelected() == null) {
            controlObj.setVisible(false);
            return;
        }
        controlObj.setVisible(true);
        controlObj.setLocalTranslation(form.getSelected().getReadOnlySelectedSpatial().getWorldTranslation());
        Mode mode = form.getMode();
        switch (form.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(form.getSelected().getReadOnlySelectedSpatial().getWorldRotation());
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
        
    }
}
