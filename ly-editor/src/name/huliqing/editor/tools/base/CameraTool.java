/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools.base;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import java.util.Arrays;
import name.huliqing.editor.EditorListener;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.events.Event;
import name.huliqing.editor.events.JmeEvent;
import name.huliqing.editor.manager.Manager;
import name.huliqing.editor.manager.ResManager;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.editor.tools.EditTool;
import name.huliqing.editor.utils.BestEditCamera;
import name.huliqing.editor.utils.BestEditCamera.View;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.anim.AnimNode;
import name.huliqing.luoying.object.anim.CurveMoveAnim;

/**
 * 场景镜头工具
 * @author huliqing
 */
public class CameraTool extends EditTool implements EditorListener {
    
//    private static final Logger LOG = Logger.getLogger(CameraTool.class.getName());
    
    private final String EVENT_DRAG = "cameraDragEvent";
    private final String EVENT_ROTATE = "cameraRotateEvent";
    private final String EVENT_ZOOM_IN = "cameraZoomInEvent";
    private final String EVENT_ZOOM_OUT = "cameraZoomOutEvent";
    private final String EVENT_RECHASE = "cameraRechaseEvent";
    private final String EVENT_RESET = "cameraResetEvent";
    
    private final String EVENT_VIEW_FRONT = "camViewFrontEvent";
    private final String EVENT_VIEW_RIGHT = "camViewRightEvent";
    private final String EVENT_VIEW_TOP = "camViewTopEvent";
    
    private final String EVENT_ORTHO_PERSP = "camViewOrthoPersp";
    
    // 判断Ctr是否按下的事件
    private final JmeEvent LCtrEvent;
    private final JmeEvent RCtrEvent;
    private boolean ctrPressed;
    
    // 镜头
    private BestEditCamera editorCam;
    
    // 相机拖动标记
    private boolean dragEnabled;
    // 最近一次记录光标的位置
    private final Vector2f lastCursorPos = new Vector2f();
    private final float panMoveSpeed = 0.001f;
    
    // remove20170118:
    // -defFocusDistance会导致镜头拉近或拉远，因物体大小不一，这有可能会导致拉到物体内部。所以不再去拉近距离
    // -只要定位镜头位置就可以。
    // 默认相机定位时的最近距离
//    private final float defFocusDistance = 2f;
    
    // 视角及提示
    private final BitmapText viewText = new BitmapText(Manager.getFont());
    private View view;
    private boolean ortho;

    public CameraTool(String name, String tips, String icon) {
        super(name, tips, icon);
        LCtrEvent = bindEvent("LCtrPressedEvent").bindKey(KeyInput.KEY_LCONTROL, true);
        RCtrEvent = bindEvent("RCtrPressedEvent").bindKey(KeyInput.KEY_RCONTROL, true);
        viewText.setSize(Manager.FONT_SIZE);
        viewText.setColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1));
        viewText.setAlignment(BitmapFont.Align.Left);
        viewText.setVerticalAlignment(BitmapFont.VAlign.Top);
    }

    @Override
    public void initialize(SimpleJmeEdit jmeEdit, EditToolbar toolbar) {
        super.initialize(jmeEdit, toolbar);
        editorCam = new BestEditCamera(editor.getCamera(), editor.getInputManager());
        view = editorCam.getView();
        updateViewText();
        editor.getGuiNode().attachChild(viewText);
        editor.addListener(this);
        
    }

    @Override
    public void cleanup() {
        editor.removeListener(this);
        editorCam.cleanup();
        editorCam = null;
        viewText.removeFromParent();
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
    
    public JmeEvent bindViewRightEvent() {
        return bindEvent(EVENT_VIEW_RIGHT);
    }
    
    public JmeEvent bindViewTopEvent() {
        return bindEvent(EVENT_VIEW_TOP);
    }
    
    public JmeEvent bindViewOrthoPerspEvent() {
        return bindEvent(EVENT_ORTHO_PERSP);
    }
    
    @Override
    protected void onToolEvent(Event e) {
        if (e == LCtrEvent || e == RCtrEvent) {
            ctrPressed = e.isMatch();
        }
        
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
                case EVENT_VIEW_FRONT:
                        doSwitchView(ctrPressed ? BestEditCamera.View.back : BestEditCamera.View.front);
                        break;
                case EVENT_VIEW_RIGHT:
                        doSwitchView(ctrPressed ? BestEditCamera.View.left : BestEditCamera.View.right);
                        break;
                case EVENT_VIEW_TOP:
                        doSwitchView(ctrPressed ? BestEditCamera.View.bottom : BestEditCamera.View.top);
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
        if (edit.getSelected() != null) {
            doChase(edit.getSelected().getControlSpatial().getWorldTranslation());
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
    
    public void doOrthoView() {
        editorCam.doOrthoMode();
    }
    
    public void doPerspView() {
        editorCam.doPerspMode();
    }
    
    private void doChase(Vector3f position) {
        Camera camera = editorCam.getCamera();
        
        // defFocusDistance会导致镜头拉近或拉远，因物体大小不一，这有可能会导致拉到物体内部。所以不再去拉近距离
        // 只要定位镜头位置就可以。
//        Vector3f camEndLoc = new Vector3f().set(position).subtractLocal(camera.getDirection().mult(defFocusDistance));
        Vector3f camEndLoc = new Vector3f().set(position).subtractLocal(camera.getDirection().mult(editorCam.getFocus().distance(camera.getLocation())));
        
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
        edit.getEditRoot().attachChild(locAnimNode);
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
        
        // 更新视角提示信息
        if (editorCam.getView() != view || ortho != editorCam.getCamera().isParallelProjection()
                ) {
            view = editorCam.getView();
            ortho = editorCam.getCamera().isParallelProjection();
            updateViewText();
        }
    }

    public void updateViewText() {
        String textKey = "";
        switch (view) {
            case back:
                textKey = ResConstants.VIEW_BACK;
                break;
            case bottom:
                textKey = ResConstants.VIEW_BOTTOM;
                break;
            case front:
                textKey = ResConstants.VIEW_FRONT;
                break;
            case left:
                textKey = ResConstants.VIEW_LEFT;
                break;
            case right:
                textKey = ResConstants.VIEW_RIGHT;
                break;
            case top:
                textKey = ResConstants.VIEW_TOP;
                break;
            case user:
                textKey = ResConstants.VIEW_USER;
                break;
            default:
                break;
        }
        String orthoKey = editorCam.getCamera().isParallelProjection() ? ResConstants.VIEW_ORTHO : ResConstants.VIEW_PERSP;
        ResManager resManager = Manager.getResManager();
        String viewTextStr = resManager.get(textKey) + " " + resManager.get(orthoKey);
        viewText.setText(viewTextStr);
        float x = editorCam.getCamera().getWidth() - viewText.getLineWidth() - 10;
        float y = editorCam.getCamera().getHeight() - 10;
        viewText.setLocalTranslation(x, y, 0);
    }

    @Override
    public void onReshape(int w, int h) {
        updateViewText();
        // 窗口大小变化的时候要更新相机视图，否则画面长宽比会发生变化。
        editorCam.doToggleOrthoPerspMode();
        editorCam.doToggleOrthoPerspMode();
    }
    
}
