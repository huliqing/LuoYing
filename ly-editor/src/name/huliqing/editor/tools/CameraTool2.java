/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import java.util.Arrays;
import java.util.logging.Logger;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.utils.BestEditCamera;
import name.huliqing.editor.utils.BestEditCamera.View;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.anim.AnimNode;
import name.huliqing.luoying.object.anim.CurveMoveAnim;

/**
 * 场景镜头工具
 * @author huliqing
 */
public class CameraTool2 extends EditTool {
    
    private static final Logger LOG = Logger.getLogger(CameraTool2.class.getName());
    
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
    
    private final String EVENT_ORTHO_PERSP = "camViewOrthoPersp";
        
    // 镜头
    private BestEditCamera editorCam;
    
    // 相机拖动标记
    private boolean dragEnabled;
    // 最近一次记录光标的位置
    private final Vector2f lastCursorPos = new Vector2f();
    private final float panMoveSpeed = 0.001f;
    
    private final float defFocusDistance = 2f;
    
    public CameraTool2(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        super.initialize();
        editorCam = new BestEditCamera(editor.getCamera(), editor.getInputManager());
    }

    @Override
    public void cleanup() {
        editorCam.cleanup();
        editorCam = null;
        super.cleanup(); 
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
    public JmeEvent bindViewOrthoPerspEvent() {
        return bindEvent(EVENT_ORTHO_PERSP);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e.getName().equals(EVENT_DRAG)) {
            dragEnabled = e.isMatch();
            if (dragEnabled) {
                lastCursorPos.set(editor.getInputManager().getCursorPosition());
            }
            editorCam.setRotationEnabled(!dragEnabled);
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
                        doSwitchView(BestEditCamera.View.back);
                        break;
                case EVENT_VIEW_BOTTOM:
                        doSwitchView(BestEditCamera.View.bottom);
                        break;
                case EVENT_VIEW_FRONT:
                        doSwitchView(BestEditCamera.View.front);
                        break;
                case EVENT_VIEW_LEFT:
                        doSwitchView(BestEditCamera.View.left);
                        break;
                case EVENT_VIEW_RIGHT:
                        doSwitchView(BestEditCamera.View.right);
                        break;
                case EVENT_VIEW_TOP:
                        doSwitchView(BestEditCamera.View.top);
                        break;
                // 正交、透视切换
                case EVENT_ORTHO_PERSP:
                        editorCam.doToggleOrthoPerspMode();
                        break;
                default:
            }
        }
        
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
        editorCam.doSwitchView(view);
    }
    
    private void doChase(Vector3f position) {
        Camera camera = editorCam.getCamera();
        Vector3f camEndLoc = new Vector3f().set(position).subtractLocal(camera.getDirection().mult(defFocusDistance));
        
        CameraNode cameraNode = new CameraNode("", editorCam.getCamera());
        cameraNode.setLocalRotation(editorCam.getCamera().getRotation());
        cameraNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);

        CurveMoveAnim locAnim = new CurveMoveAnim();
        locAnim.setControlPoints(Arrays.asList(camera.getLocation(), camEndLoc));
        locAnim.setCurveTension(0);
        locAnim.setTarget(cameraNode);
        locAnim.setUseTime(0.5f);
        locAnim.addListener((Anim anim) -> {
            editorCam.setFocus(position);
        });
        locAnim.start();
        AnimNode locAnimNode = new AnimNode(locAnim, true);
        locAnimNode.attachChild(cameraNode);
        form.getEditRoot().attachChild(locAnimNode);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); 
        if (dragEnabled) {
            Vector2f cp = editor.getInputManager().getCursorPosition();
            editorCam.doPanCamera(
                      (cp.x - lastCursorPos.x) * panMoveSpeed
                    , (cp.y - lastCursorPos.y) * -panMoveSpeed);
            lastCursorPos.set(cp);
        }
    }

    
}
