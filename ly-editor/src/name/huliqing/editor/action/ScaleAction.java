/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.action;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.Axis;
import name.huliqing.editor.tiles.TransformObj;
import name.huliqing.luoying.manager.PickManager;

/**
 *
 * @author huliqing
 */
public class ScaleAction extends ComplexAction {

    private static final Logger LOG = Logger.getLogger(ScaleAction.class.getName());
    
    
    private final Ray ray = new Ray();
    
    // 变换控制物体
    private TransformObj transformObj;
    // 当前操作的轴向
    private Axis actionAxis;
    private SelectObj selectObj;
    
    private final Picker picker = new Picker();
    private boolean picking;
    
    private final Vector3f startScale = new Vector3f();
    
    public ScaleAction(Editor editor) {
        super(editor);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            if (picking) {
                picker.endPick();
                picking = false;
            }
            if (transformObj != null) {
                transformObj.showDebugLine(actionAxis, false);
                transformObj = null;
            }
            actionAxis = null;
            selectObj = null;
            return;
        }
        
        selectObj = editor.getForm().getSelected();
        PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
        transformObj = editor.getForm().getTransformObj();
        actionAxis = transformObj.pickTransformAxis(ray);
        if (transformObj != null && actionAxis != null && selectObj != null) {
            picking = true;
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
            picker.startPick(selectObj.getSelectedSpatial(), editor.getForm().getTransformMode(), editor.getCamera()
                    , editor.getInputManager().getCursorPosition(), planRotation);
            transformObj.showDebugLine(actionAxis, true);
            startScale.set(selectObj.getSelectedSpatial().getLocalScale());
        }
    }

    @Override
    public void update(float tpf) {
        if (!picking)
            return;
        
        if (!picker.updatePick(editor.getCamera(), editor.getInputManager().getCursorPosition())) {
            return;
        }
        
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
        LOG.log(Level.INFO, "Axis={0}, pickTranslation={1}, startScale={2}, diffScale={3}, finalScale={4}, worldRot={5}, worldRotInverse={6}"
                , new Object[] {axis, pickTranslation, startScale, diff, newScale
                , selectObj.getSelectedSpatial().getWorldRotation(), worldRotInverse});
        
        tv.release();
    }

    
}
