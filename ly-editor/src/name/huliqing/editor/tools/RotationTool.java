/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.scene.Spatial;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.EditFormListener;
import name.huliqing.editor.forms.Mode;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.AxisNode;
import name.huliqing.editor.tiles.RotationControlObj;
import name.huliqing.editor.undoredo.UndoRedo;
import name.huliqing.luoying.manager.PickManager;

/**
 * 旋转编辑工具
 * @author huliqing
 */
public class RotationTool extends EditTool implements EditFormListener{
    
//    private static final Logger LOG = Logger.getLogger(RotationTool.class.getName());
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private final RotationControlObj controlObj = new RotationControlObj();
    // 当前操作的轴向
    private AxisNode actionAxis;
    // 行为操作开始时编辑器中的被选择的物体，以及该物体的位置
    private SelectObj selectObj;
    
    private final Picker picker = new Picker();
    private boolean transforming;
    
    // 开始变换时物体的位置(local)
    private final Quaternion startRotate = new Quaternion();
    private final Quaternion startWorldRotate = new Quaternion();
    // 经过旋转操作后的LocaleRotationl
    private final Quaternion afterRotate = new Quaternion();

    public RotationTool(String name) {
        super(name);
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

    public JmeEvent bindRotationEvent() {
        return bindEvent(name + "rotationEvent"); 
    }

    @Override
    protected void onToolEvent(Event e) {
        if (e.isMatch()) {
            startAction();
        } else {
            endAction();
        }
    }

    private void startAction() {
        PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
        actionAxis = controlObj.getPickAxis(ray);
        selectObj = form.getSelected();
        if (controlObj != null && actionAxis != null && selectObj != null) {
            transforming = true;
            Quaternion planRotation = Picker.PLANE_XY;
            switch (actionAxis.getType()) {
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
            picker.startPick(selectObj.getSelectedSpatial(), form.getMode()
                    , editor.getCamera(), editor.getInputManager().getCursorPosition()
                    ,planRotation);
            startRotate.set(selectObj.getSelectedSpatial().getLocalRotation());
            startWorldRotate.set(selectObj.getSelectedSpatial().getWorldRotation());
            controlObj.setAxisVisible(false);
            controlObj.setAxisLineVisible(false);
            actionAxis.setAxisLineVisible(true);
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
        }
    }

    private void endAction() {
        if (transforming) {
            picker.endPick();
            form.addUndoRedo(new RotationUndoRedo(selectObj.getSelectedSpatial(), startRotate, afterRotate));
        }
        controlObj.setAxisVisible(true);
        controlObj.setAxisLineVisible(false);
        transforming = false;
        actionAxis = null;
        selectObj = null;
    }
    
    @Override
    public void update(float tpf) {
        if (!transforming)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }
        
        Quaternion rotation = startRotate.mult(picker.getRotation(startWorldRotate.inverse()));
        selectObj.getSelectedSpatial().setLocalRotation(rotation);
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
        controlObj.setLocalTranslation(form.getSelected().getSelectedSpatial().getWorldTranslation());
        Mode mode = form.getMode();
        switch (form.getMode()) {
            case GLOBAL:
                controlObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                controlObj.setLocalRotation(form.getSelected().getSelectedSpatial().getWorldRotation());
                break;
            case CAMERA:
                controlObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
    
    private class RotationUndoRedo implements UndoRedo {
        private final Spatial spatial;
        private final Quaternion before = new Quaternion();
        private final Quaternion after = new Quaternion();
        public RotationUndoRedo(Spatial spatial, Quaternion before, Quaternion after) {
            this.spatial = spatial;
            this.before.set(before);
            this.after.set(after);
        }
        
        @Override
        public void undo() {
            spatial.setLocalRotation(before);
            updateMarkerState();
        }

        @Override
        public void redo() {
            spatial.setLocalRotation(after);
            updateMarkerState();
        }
        
    }
}
