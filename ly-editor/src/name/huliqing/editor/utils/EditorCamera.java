package name.huliqing.editor.utils;

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
public class EditorCamera extends SimpleChaseCamera {
//    private static final Logger LOG = Logger.getLogger(EditorCamera.class.getName());
    
    
    private boolean hRotationEnabled = true;
    private boolean vRotationEnabled = true;

    public EditorCamera(Camera cam, InputManager inputManager) {
        super(cam, inputManager);
        // 使用鼠标中键来实现旋转，和blender一样        
        setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        setInvertVerticalAxis(true);
        setZoomSensitivity(3f);
        setRotationSpeed(5f);
        setMinDistance(0.0001f);
        setMaxDistance(Float.MAX_VALUE);
        setDefaultDistance(15);
        setUpVector(Vector3f.UNIT_Y);
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
        super.vRotateCamera(value);
    }
    
    public Camera getCamera() {
        return cam;
    }

    @Override
    protected void computePosition() {
        super.computePosition(); 
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
