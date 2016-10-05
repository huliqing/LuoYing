/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.ui.state;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import java.util.LinkedHashMap;
import java.util.Map;
import name.huliqing.ly.ui.UIEventListener;

/**
 *
 * @author huliqing
 */
public class UIState extends AbstractAppState {
    
    private final static UIState ins = new UIState();
    
    // 全局引用
    private Application app;
    private final UIRoot uiRoot = new UIRoot();
    
    private final static String MAPPING_LEFT_CLICK = "view_left_click";
    private final static String MAPPING_LEFT_RELEASE = "view_left_release";
    
    /** 默认UI事件“点击”监听器 */
    public final static String LISTENER_PICK_VIEW = "lis_pick_view";
    /** 默认镜头旋转过滤事件监听器 */
    public final static String LISTENER_PICK_CAMERA = "lis_pick_cam";
    /** 默认鼠标UI“释放”事件监听器 */
    public final static String LISTENER_RELEASE_CLICK = "lis_release_click";
    /** 默认鼠标拖动后“释放”事件监听器 */
    public final static String LISTENER_RELEASE_DRAG = "lis_release_drag";
    
    // 鼠标点击时的侦听列表。
    private final Map<String, PickListener> pickListeners = new LinkedHashMap<String, PickListener>();
    // 鼠标释放时的事件监听
    private final Map<String, ReleaseListener> releaseListeners = new LinkedHashMap<String, ReleaseListener>();
  
    // UI点击及拖放管理
    private final ClickManager clickManager = ClickManager.getInstance();
    private final DragManager dragManager = DragManager.getInstance();
    
    
    private UIState() {}
    
    public static UIState getInstance() {
        return ins;
    }
    
    /**
     * 注册app和guiRoot节点
     * @param app
     */
    public void register(Application app) {
        this.app = app;
        app.getGuiViewPort().attachScene(uiRoot);
    }
    
    /**
     * 添加UI的全局监听器
     * @param listener 
     */
    public void addEventListener(UIEventListener listener) {
        clickManager.addEventListener(listener);
        dragManager.addEventListener(listener);
    }
    
    /**
     * 移除UI的全局监听器
     * @param listener
     */
    public void removeEventListener(UIEventListener listener) {
        clickManager.removeEventListener(listener);
        dragManager.removeEventListener(listener);
    }
 
    /**
     * 获取UIState的本地uiRoot(不是SimpleApplication中的guiRoot);
     * UIState中所有通过addUI添加的UI都放在这个节点下面。
     * @return 
     */
    public Node getUIRoot() {
        return uiRoot;
    }
    
    /**
     * 注意：不要设置UI组件的z值，尽量保持z值为0,否则可能出现一些UI点不了的
     * 问题。
     * @param ui 
     */
    public void addUI(Spatial ui) {
        // add20160420每次添加时把UI放在最前面
        uiRoot.detachChild(ui);
        uiRoot.attachChild(ui);
    }
    
    /**
     * 获取App
     * @return 
     */
    public Application getApplication() {
        return app;
    }
    
    /**
     * 添加一个"点击"监听
     * @param name
     * @param listener 
     */
    public final void putPickListener(String name, PickListener listener) {
        pickListeners.put(name, listener);
    }
    
    /**
     * 移除“点击”监听器
     * @param name 
     */
    public final void removePickListener(String name) {
        pickListeners.remove(name);
    }
    
    /**
     * 添加一个全局"释放"监听,当鼠标释放时会被调用
     * @param name
     * @param listener 
     */
    public final void putReleaseListener(String name, ReleaseListener listener) {
        releaseListeners.put(name, listener);
    }
    
    /**
     * 移除鼠标“释放”监听器
     * @param name 
     */
    public final void removeReleaseListener(String name) {
        releaseListeners.remove(name);
    }
    
    /**
     * 重新注册默认的监听器,主要处理View组件的事件监听
     */
    public void registerDefaultListener() {
        // 事件绑定,左键释放，
        // 重要：镜头的视角旋转优先级较高，MAPPING_LEFT_RELEASE必须放在MAPPING_LEFT_CLICK先注册bindMouse，
        // 以避免在点击UI后再拖动镜头时出现首次无法旋转视角的BUG(虽然再点击一下场景即可转动视角，但不舒服)。
        bindMouse(app.getInputManager(), MAPPING_LEFT_RELEASE, MouseInput.BUTTON_LEFT, new ViewReleaseActionListener());
        // 事件绑定,左键单击
        bindMouse(app.getInputManager(), MAPPING_LEFT_CLICK, MouseInput.BUTTON_LEFT, new ViewPickActionListener());
        
        // ---------------------------------------------------------------------
        // 默认的事件监听
        // ---------------------------------------------------------------------
        // 1.UI点击
        putPickListener(LISTENER_PICK_VIEW, new UIPickListener(app, uiRoot));
        
        // 2.镜头转动
        putPickListener(LISTENER_PICK_CAMERA, new CameraRotPickListener());
        
        // 释放监听
        putReleaseListener(LISTENER_RELEASE_CLICK, clickManager);
        putReleaseListener(LISTENER_RELEASE_DRAG, dragManager);
    }
    
    /**
     * 清理已经注册的所有事件监听器，包括默认的View事件监听器和按键监听器
     */
    public void clearListener() {
        pickListeners.clear();
        releaseListeners.clear();
        app.getInputManager().deleteMapping(MAPPING_LEFT_CLICK);
        app.getInputManager().deleteMapping(MAPPING_LEFT_RELEASE);
        clickManager.cleanup();
        dragManager.cleanup();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app);
        registerDefaultListener();
    }

    @Override
    public void update(float tpf) {
//        super.update(tpf); // ignore

        // 注：不需要在这里更新GeometriesState,只要更新updateLogicalState就可以
        uiRoot.updateLogicalState(tpf);
//        uiRoot.updateGeometricState();

        clickManager.update(tpf);
        dragManager.update(tpf);
    }
    
    @Override
    public void cleanup() {
        clickManager.cleanup();
        dragManager.cleanup();
        
        // 清理所有子UI
        uiRoot.detachAllChildren();
        
        clearListener();
        super.cleanup();
    }
    
    /**
     * 清理界面上的所有UI
     */
    public void clearUI() {
        uiRoot.detachAllChildren();
    }
    
    // =========================================================================
    
    private void bindMouse(InputManager inputManager, String mapName, int button, InputListener listener) {
        // 先移除旧的,再增加新的,避免listener重复(在一些退出后没有清理mapping时的情况可能发生)
        if (inputManager.hasMapping(mapName)) {
            inputManager.deleteMapping(mapName);
        }
        
        Trigger t = new MouseButtonTrigger(button);
        inputManager.addMapping(mapName, t);
        inputManager.addListener(listener, mapName);
    }
    
    private class ViewReleaseActionListener implements ActionListener {
        
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed || !MAPPING_LEFT_RELEASE.equals(name)) {
                return;
            }
            for (ReleaseListener listener : releaseListeners.values()) {
                listener.release();
            }
        }
    }
    
    // 左键点击
    private class ViewPickActionListener implements ActionListener {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (!MAPPING_LEFT_CLICK.equals(name)) {
                return;
            }
            for (PickListener listener : pickListeners.values()) {
                // 不进行重复选择,如果已经选中,则中断后面的选择监听.
                if (listener.pick(isPressed, tpf)) {
                    return;
                }
            }
        }
    }
    
    // add20160603,这是一个定制的节点，由于jme3.1后对于updateLogiclState逻辑的改变
    private class UIRoot extends Node {

        public UIRoot() {
            setQueueBucket(Bucket.Gui);
            setCullHint(CullHint.Never);
        }
        
        @Override
        public void updateLogicalState(float tpf) {
            // 不需要去调用父类的updateLogicalState,这会导致去调用所有子类的control更新，而目前UI是不支持control的。
            // 这可以减少很多方法调用,节省性能
//            super.updateLogicalState(tpf);

            for(Spatial s : children) {
                s.updateLogicalState(tpf);
            }
        }

        @Override
        public boolean checkCulling(Camera cam) {
            // 因为UIRoot作为一个定制的UI根节点，生命周期与默认SimpleApplication中的guiRoot不一样。
            // UIRoot需要自己更新状态。
            // 这里做了一个特殊的处理，由于jme在渲染之前会检查是否有geometry的变换还没有更新，否则报错（IllegalStateException）
            // 因此这里必须主动更新，以防止报异常
            updateGeometricState();
            return super.checkCulling(cam); 
        }
        
    }
}
