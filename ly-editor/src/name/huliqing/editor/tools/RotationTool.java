/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import java.util.logging.Logger;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.EditFormListener;
import name.huliqing.editor.forms.Mode;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.Axis;
import name.huliqing.editor.tiles.RotationObj;
import name.huliqing.luoying.manager.PickManager;

/**
 * 旋转编辑工具
 * @author huliqing
 */
public class RotationTool extends EditTool implements EditFormListener{
    
    private static final Logger LOG = Logger.getLogger(RotationTool.class.getName());
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private final RotationObj transformObj = new RotationObj();
    // 当前操作的轴向
    private Axis actionAxis;
    // 行为操作开始时编辑器中的被选择的物体，以及该物体的位置
    private SelectObj selectObj;
    
    private final Picker picker = new Picker();
    private boolean picking;
    
    // 开始变换时物体的位置(local)
    private final Quaternion startRotate = new Quaternion();
    private final Quaternion startWorldRotate = new Quaternion();
    private final Quaternion lastRotate = new Quaternion();

    public RotationTool(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize();
        form.getEditRoot().getParent().attachChild(transformObj);
        form.addListener(this);
        updateMarkerState();
    }

    @Override
    public void cleanup() {
        transformObj.removeFromParent();
        form.removeListener(this);
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
        actionAxis = transformObj.pickTransformAxis(ray);
        selectObj = form.getSelected();
        if (transformObj != null && actionAxis != null && selectObj != null) {
            picking = true;
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
            transformObj.showDebugLine(actionAxis, true);
            startRotate.set(selectObj.getSelectedSpatial().getLocalRotation());
            startWorldRotate.set(selectObj.getSelectedSpatial().getWorldRotation());
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
        }
    }

    private void endAction() {
        transformObj.showDebugLine(actionAxis, false);
        if (picking) {
            picker.endPick();
        }
        picking = false;
        actionAxis = null;
        selectObj = null;
    }
    
    @Override
    public void update(float tpf) {
        if (!picking)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }
        
        Quaternion rotation = startRotate.mult(picker.getRotation(startWorldRotate.inverse()));
        selectObj.getSelectedSpatial().setLocalRotation(rotation);
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
            transformObj.setVisible(false);
            return;
        }
        transformObj.setVisible(true);
        transformObj.setLocalTranslation(form.getSelected().getSelectedSpatial().getWorldTranslation());
        Mode mode = form.getMode();
        switch (form.getMode()) {
            case GLOBAL:
                transformObj.setLocalRotation(new Quaternion());
                break;
            case LOCAL:
                transformObj.setLocalRotation(form.getSelected().getSelectedSpatial().getWorldRotation());
                break;
            case CAMERA:
                transformObj.setLocalRotation(editor.getCamera().getRotation());
                break;
            default:
                throw new IllegalArgumentException("Unknow mode type=" + mode);
        }
    }
}
