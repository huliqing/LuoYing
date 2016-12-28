/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.action.Picker;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.forms.EditFormListener;
import name.huliqing.editor.forms.Mode;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.Axis;
import name.huliqing.editor.tiles.ScaleObj;
import name.huliqing.luoying.manager.PickManager;

/**
 * 缩放编辑工具
 * @author huliqing
 */
public class ScaleTool extends EditTool implements EditFormListener{

    private static final Logger LOG = Logger.getLogger(ScaleTool.class.getName());
    
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private final ScaleObj transformObj = new ScaleObj();
    // 当前操作的轴向
    private Axis actionAxis;
    private SelectObj selectObj;
    
    private final Picker picker = new Picker();
    private boolean picking;
    
    private boolean isFullScale;
    private final Vector3f startScale = new Vector3f();

    public ScaleTool(String name) {
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
    
    /**
     * 绑定移动按键
     * @return 
     */
    public JmeEvent bindScaleEvent() {
        return bindEvent(name + "scaleEvent");
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
        selectObj = form.getSelected();
        if (selectObj == null) {
            return;
        }
        
        isFullScale = transformObj.isPickCenter(ray);
        if (isFullScale) {
            picking = true;
            picker.startPick(selectObj.getSelectedSpatial(), Mode.CAMERA, editor.getCamera()
                    , editor.getInputManager().getCursorPosition(), editor.getCamera().getRotation());
        } else {
            actionAxis = transformObj.pickTransformAxis(ray);
            if (actionAxis == null) {
                return;
            }
            picking = true;
            transformObj.showDebugLine(actionAxis, true);
            Quaternion planRotation = Picker.PLANE_XY;
            switch (actionAxis.getType()) {
                case x:
                    planRotation = Picker.PLANE_XY;
                    break;
                case y:
                    planRotation = Picker.PLANE_YZ;
                    break;
                case z:
                    planRotation = Picker.PLANE_XZ;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            picker.startPick(selectObj.getSelectedSpatial(), form.getMode(), editor.getCamera()
                    , editor.getInputManager().getCursorPosition(), planRotation);
        }
        startScale.set(selectObj.getSelectedSpatial().getLocalScale());
    }
    
    private void endAction() {
        if (picking) {
            picker.endPick();
        }
        picking = false;
        transformObj.showDebugLine(actionAxis, false);
        actionAxis = null;
        selectObj = null;
        isFullScale = false;
    }

    @Override
    public void update(float tpf) {
        if (!picking) {
            return;
        }

        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }

        if (isFullScale) {
            Vector3f constraintAxis = picker.getStartOffset().normalize();
            float diff = picker.getLocalTranslation(constraintAxis).dot(constraintAxis);
            diff += 1f;
            Vector3f scale = startScale.mult(diff);
            selectObj.getSelectedSpatial().setLocalScale(scale);

        } else {
            TempVars tv = TempVars.get();
            Vector3f axis = actionAxis.getDirection(tv.vect1);
            Vector3f newScale = tv.vect2.set(startScale);
            Vector3f diff = tv.vect3;
            Vector3f pickTranslation = picker.getTranslation(axis);

            Quaternion worldRotInverse = tv.quat1.set(selectObj.getSelectedSpatial().getWorldRotation()).inverse();
            worldRotInverse.mult(pickTranslation, diff);
            //        diff.multLocal(0.5f);

            newScale.addLocal(diff);
            selectObj.getSelectedSpatial().setLocalScale(newScale);
            LOG.log(Level.INFO, "Axis={0}, pickTranslation={1}, startScale={2}, diffScale={3}, finalScale={4}, worldRot={5}, worldRotInverse={6}", new Object[]{axis, pickTranslation, startScale, diff, newScale, selectObj.getSelectedSpatial().getWorldRotation(), worldRotInverse});

            tv.release();
        }
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
