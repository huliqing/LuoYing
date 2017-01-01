
package name.huliqing.editor.utils;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.util.TempVars;

public class BestEditCamera implements ActionListener, AnalogListener {

//    private static final Logger LOG = Logger.getLogger(BestEditCamera.class.getName());
    
    public enum View {
        front, back, left, right, top, bottom, user;
    }
    
    private InputManager inputManager;
    private Camera cam = null;
    
    private final Quaternion rot = new Quaternion();
    // 临时变量
    private final Vector3f vector = new Vector3f(0, 0, 5);
    
    // 相机的目标视点
    private final Vector3f focus = new Vector3f();
    // 相机与目标视点的最近距离限制
    private final float minDistance = 0.0001f;
    
    // 是否打开旋转功能
    private boolean rotationEnabled = true;
    private boolean canRotate;
    
    private View view = View.user;
    
    public BestEditCamera(Camera cam, InputManager inputManager) {
        this.cam = cam;
        registerWithInput(inputManager);
    }
    
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals(CameraInput.CHASECAM_TOGGLEROTATE)) {
            if (keyPressed) {
                canRotate = true;
            } else {
                canRotate = false;
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(CameraInput.CHASECAM_MOVELEFT)) {
            doRotateCamera(Vector3f.UNIT_Y, value * 2.5f);
        } else if (name.equals(CameraInput.CHASECAM_MOVERIGHT)) {
            doRotateCamera(Vector3f.UNIT_Y, -value * 2.5f);
        } else if (name.equals(CameraInput.CHASECAM_UP)) {
            doRotateCamera(cam.getLeft(), -value * 2.5f);
        } else if (name.equals(CameraInput.CHASECAM_DOWN)) {
            doRotateCamera(cam.getLeft(), value * 2.5f);
        } else if (name.equals(CameraInput.CHASECAM_ZOOMIN)) {
            doZoomCamera(value);
        } else if (name.equals(CameraInput.CHASECAM_ZOOMOUT)) {
            doZoomCamera(-value);
        }
    }

    /**
     * Registers inputs with the input manager
     * @param inputManager
     */
    public final void registerWithInput(InputManager inputManager) {

        String[] inputs = {CameraInput.CHASECAM_TOGGLEROTATE,
            CameraInput.CHASECAM_DOWN,
            CameraInput.CHASECAM_UP,
            CameraInput.CHASECAM_MOVELEFT,
            CameraInput.CHASECAM_MOVERIGHT,
            CameraInput.CHASECAM_ZOOMIN,
            CameraInput.CHASECAM_ZOOMOUT};

        this.inputManager = inputManager;
        inputManager.addMapping(CameraInput.CHASECAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(CameraInput.CHASECAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(CameraInput.CHASECAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        
        inputManager.addMapping(CameraInput.CHASECAM_MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(CameraInput.CHASECAM_MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        
        inputManager.addMapping(CameraInput.CHASECAM_TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        inputManager.addListener(this, inputs);
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
    
    public void doRotateCamera(Vector3f axis, float amount) {        
        if (!rotationEnabled || !canRotate) {
            return;
        }
        if (axis.equals(cam.getLeft())) {
            float elevation = -FastMath.asin(cam.getDirection().y);
            amount = Math.min(Math.max(elevation + amount, -FastMath.HALF_PI), FastMath.HALF_PI)
                    - elevation;
        }
        rot.fromAngleAxis(amount, axis);
        cam.getLocation().subtract(focus, vector);
        rot.mult(vector, vector);
        focus.add(vector, cam.getLocation());

        Quaternion curRot = cam.getRotation().clone();
        cam.setRotation(rot.mult(curRot));
        
        view = View.user;
    }

    public void doPanCamera(float left, float up) {
//        LOG.log(Level.INFO, "doPanCamera, left={0}, up={1}", new Object[] {left, up});
        cam.getLeft().mult(left, vector);
        vector.scaleAdd(up, cam.getUp(), vector);
        vector.multLocal(cam.getLocation().distance(focus));
        cam.setLocation(cam.getLocation().add(vector));
        focus.addLocal(vector);
    }

    public void doMoveCamera(float forward) {
        cam.getDirection().mult(forward, vector);
        cam.setLocation(cam.getLocation().add(vector));
    }

    public void doZoomCamera(float amount) {
        amount = amount * 0.1f;
        amount = cam.getLocation().distance(focus) * amount;
        float dist = cam.getLocation().distance(focus);
        amount = dist - Math.max(0f, dist - amount);
        
        // 一个最小距离用于防止拉到0距离后缩放不回来的BUG
        if (amount >= dist) {
            amount -= minDistance;
        }
        
        Vector3f loc = cam.getLocation().clone();
        loc.scaleAdd(amount, cam.getDirection(), loc);
        cam.setLocation(loc);

        if (cam.isParallelProjection()) {
            float aspect = (float) cam.getWidth() / cam.getHeight();
            float h = FastMath.tan(45f * FastMath.DEG_TO_RAD * .5f) * dist;
            float w = h * aspect;
            cam.setFrustum(-1000, 1000, -w, w, h, -h);
        }
//        LOG.log(Level.INFO, "doZoomCamera, Camera location={0}", cam.getLocation());
    }

    public void doSwitchView(View view) {
        float dist = cam.getLocation().distance(focus);
        this.view = view;
        switch (view) {
            case front:
                cam.setLocation(new Vector3f(focus.x, focus.y, focus.z + dist));
                cam.lookAt(focus, Vector3f.UNIT_Y);
                break;
            case left:
                cam.setLocation(new Vector3f(focus.x + dist, focus.y, focus.z));
                cam.lookAt(focus, Vector3f.UNIT_Y);
                break;
            case right:
                cam.setLocation(new Vector3f(focus.x - dist, focus.y, focus.z));
                cam.lookAt(focus, Vector3f.UNIT_Y);
                break;
            case back:
                cam.setLocation(new Vector3f(focus.x, focus.y, focus.z - dist));
                cam.lookAt(focus, Vector3f.UNIT_Y);
                break;
            case top:
                cam.setLocation(new Vector3f(focus.x, focus.y + dist, focus.z));
                cam.lookAt(focus, Vector3f.UNIT_Z.mult(-1));
                break;
            case bottom:
                cam.setLocation(new Vector3f(focus.x, focus.y - dist, focus.z));
                cam.lookAt(focus, Vector3f.UNIT_Z);
                break;
            case user:
            default:
        }
    }

    public boolean doToggleOrthoPerspMode() {
        float aspect = (float) cam.getWidth() / cam.getHeight();
        if (!cam.isParallelProjection()) {
            cam.setParallelProjection(true);
            float h = cam.getFrustumTop();
            float w;
            float dist = cam.getLocation().distance(focus);
            float fovY = FastMath.atan(h) / (FastMath.DEG_TO_RAD * .5f);
            h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * dist;
            w = h * aspect;
            cam.setFrustum(-1000, 1000, -w, w, h, -h);
            return true;
        } else {
            cam.setParallelProjection(false);
            cam.setFrustumPerspective(45f, aspect, 1, 1000);
            return false;
        }
    }

    public Vector3f getFocus() {
        return focus;
    }
    
    public void setFocus(Vector3f focusPos) {
        TempVars tv = TempVars.get();
        focusPos.subtract(focus, tv.vect1);
        focus.set(focusPos);
        cam.getLocation().addLocal(tv.vect1);
        cam.update();
        tv.release();
    }
    
    public Vector3f computeCameraPos(Vector3f focusPos, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        TempVars tv = TempVars.get();
        focusPos.subtract(focus, tv.vect1);
        cam.getLocation().add(tv.vect1, store);
        tv.release();
        return store;
    }
    
    public void setRotationEnabled(boolean enabled) {
        this.rotationEnabled = enabled;
    }

    public Camera getCamera() {
        return cam;
    }
    
}
