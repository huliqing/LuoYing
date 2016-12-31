/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.input.CameraInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import com.jme3.util.TempVars;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.events.JmeKeyMapping;
import name.huliqing.editor.tiles.ChaseObj;
import name.huliqing.editor.utils.BestEditCamera.View;
import name.huliqing.editor.utils.EditorCamera;
import name.huliqing.luoying.object.anim.AnimNode;
import name.huliqing.luoying.object.anim.CurveMoveAnim;
import name.huliqing.luoying.object.anim.InterpolateRotationAnim;

/**
 * 场景镜头工具
 * @author huliqing
 */
public class CameraTool extends EditTool implements ActionListener {

    
    private static final Logger LOG = Logger.getLogger(CameraTool.class.getName());
    
    private final String EVENT_DRAG = "cameraDragEvent";
    private final String EVENT_ROTATE = "cameraRotateEvent";
    private final String EVENT_ZOOM_IN = "cameraZoomInEvent";
    private final String EVENT_ZOOM_OUT = "cameraZoomOutEvent";
    private final String EVENT_RECHASE = "cameraRechaseEvent";
    private final String EVENT_RESET = "cameraResetEvent";
    
    private final String EVENT_VIEW_FRONT = "camViewFrontEvent";
    private final String EVENT_VIEW_BACK = "camViewBackEvent";
    private final String EVENT_VIEW_LEFT= "camViewLeftEvent";
    private final String EVENT_VIEW_RIGHT = "camViewRightEvent";
    private final String EVENT_VIEW_TOP = "camViewTopEvent";
    private final String EVENT_VIEW_BOTTOM = "camViewBottomEvent";
    
    // 镜头
    private EditorCamera editorCam;
    // 被镜头跟随的物体
    private final ChaseObj chaseObj = new ChaseObj();
    
    // 最近一次记录光标的位置
    private final Vector2f lastCursorPos = new Vector2f();
    // 最近一次记录的相机所跟随的物体的位置
    private final Vector3f lastChasePos = new Vector3f();
    // 相机的移动速度，默认1.0f
    private final float camMoveSpeed = 1.0f;
    // 相机的缩放速度
    private final float camZoomSpeed = 1.0f;
    
    // 相机拖动标记
    private boolean dragEnabled;
    
    public CameraTool(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize(); 
        
        if (editorCam == null) {
            editorCam = new EditorCamera(editor.getCamera(), editor.getInputManager());
            editorCam.setChase(chaseObj);
        }
        form.getEditRoot().getParent().attachChild(chaseObj);
//        chaseObj.setCullHint(Spatial.CullHint.Never); // for debug
        
        Trigger[] zoomInTriggers = convertKeyMapping(bindZoomInEvent());
        if (zoomInTriggers != null) {
            editorCam.setZoomInTrigger(zoomInTriggers);
        }
        Trigger[] zoomOutTriggers = convertKeyMapping(bindZoomOutEvent());
        if (zoomOutTriggers != null) {
            editorCam.setZoomOutTrigger(zoomOutTriggers);
        }
        Trigger[] toggleRotateTriggers = convertKeyMapping(bindToggleRotateEvent());
        if (toggleRotateTriggers != null) {
            editorCam.setToggleRotationTrigger(toggleRotateTriggers);
        }
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMIN);
        inputManager.addListener(this, CameraInput.CHASECAM_ZOOMOUT);
        
        lastCursorPos.set(editor.getInputManager().getCursorPosition());
        lastChasePos.set(chaseObj.getLocalTranslation());
    }

    @Override
    public void cleanup() {
        editorCam.cleanup();
        editorCam = null;
        chaseObj.removeFromParent();
        super.cleanup(); 
    }
    
    /**
     * 把镜头移动场景中选择的物体
     */
    public void doChaseSelected() {
        if (form.getSelected() != null) {
            doChase(form.getSelected().getSelectedSpatial().getWorldTranslation());
        }
    }
    
    /**
     * 把镜头移动到场景原点
     */
    public void doChaseOrigin() {
        doChase(new Vector3f());
    }
    
    /**
     * 让相机转到指定的视角
     * @param view 
     */
    public void doSwitchView(View view) {
        // ///
    }
 
    /**
     * 绑定拖动事件，用来拖动相机在场景中的视角。
     * @return 
     */
    public JmeEvent bindDragEvent() {
        return bindEvent(EVENT_DRAG);
    }
    
    /**
     * 绑定相机的转动事件。
     * @return 
     */
    public JmeEvent bindToggleRotateEvent() {
        return bindEvent(EVENT_ROTATE);
    }
    
    /**
     * 绑定相机的放大事件
     * @return 
     */
    public JmeEvent bindZoomInEvent() {
        return bindEvent(EVENT_ZOOM_IN);
    }
    
    /**
     * 绑定相机的缩小事件
     * @return 
     */
    public JmeEvent bindZoomOutEvent() {
        return bindEvent(EVENT_ZOOM_OUT);
    }
    
    /**
     * 绑定一个按键用于重新将相机跟随到当前场景的被选择的物体。
     * @return 
     */
    public JmeEvent bindRechaseEvent() {
        return bindEvent(EVENT_RECHASE);
    }
    
    /**
     * 绑定一个按键，用于重置相机位置
     * @return 
     */
    public JmeEvent bindResetEvent() {
        return bindEvent(EVENT_RESET);
    }
    
    public JmeEvent bindViewFrontEvent() {
        return bindEvent(EVENT_VIEW_FRONT);
    }
    
    public JmeEvent bindViewBackEvent() {
        return bindEvent(EVENT_VIEW_BACK);
    }
    
    public JmeEvent bindViewLeftEvent() {
        return bindEvent(EVENT_VIEW_LEFT);
    }
    
    public JmeEvent bindViewRightEvent() {
        return bindEvent(EVENT_VIEW_RIGHT);
    }
    
    public JmeEvent bindViewTopEvent() {
        return bindEvent(EVENT_VIEW_TOP);
    }
    
    public JmeEvent bindViewBottomEvent() {
        return bindEvent(EVENT_VIEW_BOTTOM);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_DRAG)) {
            dragEnabled = e.isMatch();
            if (dragEnabled) {
                lastCursorPos.set(editor.getInputManager().getCursorPosition());
                lastChasePos.set(chaseObj.getLocalTranslation());
                editorCam.setEnableRotation(false);
                editorCam.setEnabledRotationV(false);
            } else {
                editorCam.setEnableRotation(true);
                editorCam.setEnabledRotationV(true);
            }
        } 
        
        // 重定向相机跟随
        else if (e.isMatch() && e.getName().equals(EVENT_RECHASE)) {
            doChaseSelected();
        }
        
        // 重置相机跟随到原点
        else if (e.isMatch() && e.getName().equals(EVENT_RESET)) {
            doChaseOrigin();
        }
        
        // 相机视角
        else if (e.isMatch()) {
            switch (e.getName()) {
                case EVENT_VIEW_BACK:
                        doSwitchView(View.back);
                        break;
                case EVENT_VIEW_BOTTOM:
                        doSwitchView(View.bottom);
                        break;
                case EVENT_VIEW_FRONT:
                        doSwitchView(View.front);
                        break;
                case EVENT_VIEW_LEFT:
                        doSwitchView(View.left);
                        break;
                case EVENT_VIEW_RIGHT:
                        doSwitchView(View.right);
                        break;
                case EVENT_VIEW_TOP:
                        doSwitchView(View.top);
                        break;
                default:
            }
        }
        
    }
    
    private void doChase(Vector3f position) {
        chaseObj.setLocalTranslation(position);
        Vector3f camLoc = editorCam.getCamera().getLocation();
        Quaternion camRot = editorCam.getCamera().getRotation();
        Vector3f camEndLoc = new Vector3f();
        Quaternion camEndRot = new Quaternion();
        editorCam.setEnabled(false);
        editorCam.setDefaultDistance(2);
        editorCam.getComputeTransform(camEndLoc, camEndRot);

        CameraNode cameraNode = new CameraNode("", editorCam.getCamera());
        cameraNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);

        CurveMoveAnim locAnim = new CurveMoveAnim();
        locAnim.setControlPoints(Arrays.asList(camLoc, camEndLoc));
        locAnim.setCurveTension(0);
        locAnim.setTarget(cameraNode);
        locAnim.setUseTime(0.5f);
        locAnim.start();
        AnimNode locAnimNode = new AnimNode(locAnim, true);

        InterpolateRotationAnim rotAnim = new InterpolateRotationAnim();
        rotAnim.setStart(camRot);
        rotAnim.setEnd(camEndRot);
        rotAnim.setUseTime(0.5f);
        rotAnim.setTarget(cameraNode);
        rotAnim.addListener(t -> {
            editorCam.setEnabled(true);
        });
        rotAnim.start();
        AnimNode rotAnimNode = new AnimNode(rotAnim, true);
        rotAnimNode.attachChild(cameraNode);

        form.getEditRoot().attachChild(locAnimNode);
        form.getEditRoot().attachChild(rotAnimNode);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if (dragEnabled) {
               Vector2f cp = editor.getInputManager().getCursorPosition();
               float xDist = cp.x - lastCursorPos.x;
               float yDist = cp.y - lastCursorPos.y;
               if (xDist != 0 || yDist != 0) {
                   TempVars tv = TempVars.get();
                   Vector3f camDir = editor.getCamera().getDirection(tv.vect1);
                   Vector3f right = tv.vect2.set(camDir).crossLocal(Vector3f.UNIT_Y).normalizeLocal();
                   Vector3f up = tv.vect3.set(right).crossLocal(camDir).normalizeLocal();
                   // 允许根据距离来调节移动速度
                   float distance = editor.getCamera().getLocation().distance(lastChasePos)  * 0.002f;
                   Vector3f chasePos = tv.vect4.set(lastChasePos);
                   chasePos.subtractLocal(right.multLocal(xDist * camMoveSpeed * distance));
                   chasePos.subtractLocal(up.multLocal(yDist * camMoveSpeed * distance));
                   chaseObj.setLocalTranslation(chasePos);
                   tv.release();
               }
        }
    }
    
    private Trigger[] convertKeyMapping(JmeEvent event) {
        List<JmeKeyMapping> kms = event.getKeyMappings();
        if (kms == null || kms.isEmpty()) {
            return null;
        }
        Trigger[] triggers = new Trigger[kms.size()];
        for (int i = 0; i < kms.size(); i++) {
            JmeKeyMapping km = kms.get(i);
            switch (km.getType()) {
                case key:
                    triggers[i] = new KeyTrigger(km.getCode());
                    break;
                case button:
                    triggers[i] = new MouseButtonTrigger(km.getCode());
                    break;
                case axis:
                    triggers[i] = new MouseAxisTrigger(km.getCode(), km.isNegative());
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported key mapping type=" + km.getType());
            }
        }
        return triggers;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        // 跟随距离来调整相机的缩放强度，距离越大，缩放强度越大
        if (CameraInput.CHASECAM_ZOOMIN.equals(name) || CameraInput.CHASECAM_ZOOMOUT.equals(name)) {
            float distance = editor.getCamera().getLocation().distance(chaseObj.getLocalTranslation());
            float zoomSpeed = distance * 0.1f * camZoomSpeed;
            zoomSpeed = zoomSpeed < 0.01f ? 0.01f : zoomSpeed;
            editorCam.setZoomSensitivity(zoomSpeed);
//            LOG.log(Level.INFO, "cameraZoomSpeed={0}", zoomSpeed);
        } 
    }
    
    
}
