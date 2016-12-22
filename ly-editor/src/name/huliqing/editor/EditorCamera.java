package name.huliqing.editor;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class EditorCamera extends ChaseCamera {

    public EditorCamera(Camera cam, InputManager inputManager) {
        super(cam, inputManager);
        setSmoothMotion(false);
        setTrailingEnabled(false);
        setInvertVerticalAxis(true);
        setZoomSensitivity(1f);
        setRotationSpeed(5f);
        setRotationSensitivity(5);
        setMinDistance(0.0001f);
        setMaxDistance(Float.MAX_VALUE);
        setDefaultDistance(15);
        setChasingSensitivity(5);
        setDownRotateOnCloseViewOnly(true); 
        setUpVector(Vector3f.UNIT_Y);
        // 不要隐藏光标,否则在MAC系统下鼠标点击后会上下错位
        setHideCursorOnRotate(false);
    }
    
    public void setChase(Spatial spatial) {
        if (target != null) {
            target.removeControl(this);
        }
        spatial.addControl(this);
    }
    
    //move the camera toward or away the target
    @Override
    protected void zoomCamera(float value) {
        if (!enabled) {
            return;
        }

        zooming = true;
        targetDistance += value * zoomSensitivity;
        if (targetDistance > maxDistance) {
            targetDistance = maxDistance;
        }
        if (targetDistance < minDistance) {
            targetDistance = minDistance;
        }
        
        // 让垂直旋转可以自由执行
//        if (veryCloseRotation) {
//            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
//                targetVRotation = minVerticalRotation;
//            }
//        }
    }
    
    //rotate the camera around the target on the vertical plane
    @Override
    protected void vRotateCamera(float value) {
        if (!canRotate || !enabled) {
            return;
        }
        vRotating = true;
        float lastGoodRot = targetVRotation;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = lastGoodRot;
        }
        
        // 让垂直旋转可以自由执行
//        if (veryCloseRotation) {
//            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
//                targetVRotation = minVerticalRotation;
//            } else if (targetVRotation < -FastMath.DEG_TO_RAD * 90) {
//                targetVRotation = lastGoodRot;
//            }
//        } else {
//            if ((targetVRotation < minVerticalRotation)) {
//                targetVRotation = lastGoodRot;
//            }
//        }
        
    }
}
