package name.huliqing.editor.utils;

import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * @author huliqing
 */
public class EditorCamera extends ChaseCamera {
//    private static final Logger LOG = Logger.getLogger(EditorCamera.class.getName());
    
    private boolean hRotationEnabled = true;
    private boolean vRotationEnabled = true;

    public EditorCamera(Camera cam, InputManager inputManager) {
        super(cam, inputManager);
        // 使用鼠标中键来实现旋转，和blender一样        
        setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        setSmoothMotion(false);
        setTrailingEnabled(false);
        setInvertVerticalAxis(true);
        setZoomSensitivity(3f);
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
        setEnableRotation(true);
    }
    
    /**
     * 设置要跟随的目标对象。
     * @param spatial 
     */
    public void setChase(Spatial spatial) {
        if (target != null) {
            target.removeControl(this);
        }
        spatial.addControl(this);
    }
    
    /**
     * 是否打开水平旋转
     * @param hRotation 
     */
    public void setEnableRotation(boolean hRotation) {
        hRotationEnabled = hRotation;
    }
    
    /**
     * 设置是否打开垂直旋转
     * @param vRotation 
     */
    public void setEnabledRotationV(boolean vRotation) {
        this.vRotationEnabled = vRotation;
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
        
        // 去掉这段代码，以让垂直旋转可以自由执行
//        if (veryCloseRotation) {
//            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
//                targetVRotation = minVerticalRotation;
//            }
//        }
    }
    
    @Override
    protected void rotateCamera(float value) {
        if (!hRotationEnabled) {
            return;
        }
        super.rotateCamera(value);
    }
    
    //rotate the camera around the target on the vertical plane
    @Override
    protected void vRotateCamera(float value) {
        if (!vRotationEnabled) {
            return;
        }
        if (!canRotate || !enabled) {
            return;
        }
        vRotating = true;
        float lastGoodRot = targetVRotation;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = lastGoodRot;
        }
        
        // 去掉这段代码，以让垂直旋转可以自由执行
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
    
    /**
     * 清理相机的按键绑定
     */
    public void cleanup() {
        String[] inputs = {CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT};
        for (String input : inputs) {
            inputManager.deleteMapping(input);
        }
    }
    
    public Camera getCamera() {
        return cam;
    }
    
    /**
     * 计算出相机的跟随位置，但是不立即改变相机的位置。
     * @param locStore 
     * @param rotationStore 
     */
    public void getComputeTransform(Vector3f locStore, Quaternion rotationStore) {
        TempVars tv = TempVars.get();
        Vector3f originLoc = tv.vect1.set(cam.getLocation());
        Quaternion originRot = tv.quat1.set(cam.getRotation());
        
        boolean originEnabled = isEnabled();
        setEnabled(true);
        update(0.016f);
        setEnabled(originEnabled);
        
        locStore.set(cam.getLocation());
        rotationStore.set(cam.getRotation());
        
        cam.setLocation(originLoc);
        cam.setRotation(originRot);
        tv.release();
    }
}
