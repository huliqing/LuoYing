/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.action;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.tiles.Axis;
import name.huliqing.editor.tiles.TransformObj;
import name.huliqing.luoying.manager.PickManager;

/**
 * @author huliqing
 */
public class RotationAction extends ComplexAction {

    private static final Logger LOG = Logger.getLogger(RotationAction.class.getName());
    private final Ray ray = new Ray();

    // 变换控制物体
    private TransformObj transformObj;
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
    
    public RotationAction(Editor editor) {
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
        
        PickManager.getPickRay(editor.getCamera(), editor.getInputManager().getCursorPosition(), ray);
        transformObj = editor.getForm().getTransformObj();
        actionAxis = transformObj.pickTransformAxis(ray);
        selectObj = editor.getForm().getSelected();
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
            picker.startPick(selectObj.getSelectedSpatial(), editor.getForm().getTransformMode()
                    , editor.getCamera(), editor.getInputManager().getCursorPosition()
                    ,planRotation);
            transformObj.showDebugLine(actionAxis, true);
            startRotate.set(selectObj.getSelectedSpatial().getLocalRotation());
            startWorldRotate.set(selectObj.getSelectedSpatial().getWorldRotation());
//            LOG.log(Level.INFO, "StartSpatialLoc={0}", startSpatialLoc);
        }
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
    
}
