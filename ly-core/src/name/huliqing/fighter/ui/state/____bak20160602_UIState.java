///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.ui.state;
//
//import com.jme3.app.Application;
//import com.jme3.app.state.AbstractAppState;
//import com.jme3.app.state.AppStateManager;
//import com.jme3.input.InputManager;
//import com.jme3.input.MouseInput;
//import com.jme3.input.controls.ActionListener;
//import com.jme3.input.controls.InputListener;
//import com.jme3.input.controls.MouseButtonTrigger;
//import com.jme3.input.controls.Trigger;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import name.huliqing.fighter.ui.UIEventListener;
//
///**
// *
// * @author huliqing
// */
//public class UIState extends AbstractAppState {
//    
//    private final static UIState ins = new UIState();
//    
//    // 全局引用
//    private Application app;
//    private Node guiRoot;
//    private final Node localGuiRoot = new Node("UIState_localUIRoot");
//    
//    private final static String MAPPING_LEFT_CLICK = "view_left_click";
//    private final static String MAPPING_LEFT_RELEASE = "view_left_release";
//    
//    /** 默认UI事件“点击”监听器 */
//    public final static String LISTENER_PICK_VIEW = "lis_pick_view";
//    /** 默认镜头旋转过滤事件监听器 */
//    public final static String LISTENER_PICK_CAMERA = "lis_pick_cam";
//    /** 默认鼠标UI“释放”事件监听器 */
//    public final static String LISTENER_RELEASE_CLICK = "lis_release_click";
//    /** 默认鼠标拖动后“释放”事件监听器 */
//    public final static String LISTENER_RELEASE_DRAG = "lis_release_drag";
//    
//    // 鼠标点击时的侦听列表。
//    private final Map<String, PickListener> pickListeners = new LinkedHashMap<String, PickListener>();
//    // 鼠标释放时的事件监听
//    private final Map<String, ReleaseListener> releaseListeners = new LinkedHashMap<String, ReleaseListener>();
//  
//    // UI点击及拖放管理
//    private final ClickManager clickManager = ClickManager.getInstance();
//    private final DragManager dragManager = DragManager.getInstance();
//    
//    private UIState() {}
//    
//    public static UIState getInstance() {
//        return ins;
//    }
//    
//    /**
//     * 注册app和guiRoot节点
//     * @param app
//     * @param guiRoot 
//     */
//    public void register(Application app, Node guiRoot) {
//        this.app = app;
//        this.guiRoot = guiRoot;
//        this.guiRoot.attachChild(localGuiRoot);
//    }
//    
//    /**
//     * 添加UI的全局监听器
//     * @param listener 
//     */
//    public void addEventListener(UIEventListener listener) {
//        clickManager.addEventListener(listener);
//        dragManager.addEventListener(listener);
//    }
//    
//    /**
//     * 移除UI的全局监听器
//     */
//    public void removeEventListener(UIEventListener listener) {
//        clickManager.removeEventListener(listener);
//        dragManager.removeEventListener(listener);
//    }
// 
//    // remove20160420
////    /**
////     * 获取GUI root
////     * @return 
////     */
////    private Node getGuiRoot() {
////        if (guiRoot != null)
////            return guiRoot;
////            
////        if (app instanceof SimpleApplication) {
////            guiRoot = ((SimpleApplication) app).getGuiNode();
////            return guiRoot;
////        }
////        
////        throw new UnsupportedOperationException("Could not found guiNode, only supported "
////                + " SimpleApplication, app=" + app);
////    }
//    
//    /**
//     * 获取UIState的本地uiRoot(不是SimpleApplication中的guiRoot);
//     * UIState中所有通过addUI添加的UI都放在这个节点下面。
//     * @return 
//     */
//    public Node getUIRoot() {
//        return localGuiRoot;
//    }
//    
//    /**
//     * 注意：不要设置UI组件的z值，尽量保持z值为0,否则可能出现一些UI点不了的
//     * 问题。
//     * @param ui 
//     */
//    public void addUI(Spatial ui) {
//        // add20160420每次添加时把UI放在最前面
//        if (ui.getParent() == localGuiRoot) {
//            localGuiRoot.detachChild(ui);
//        }
//        localGuiRoot.attachChild(ui);
//    }
//    
//    /**
//     * 获取App
//     * @return 
//     */
//    public Application getApplication() {
//        return app;
//    }
//    
//    /**
//     * 添加一个"点击"监听
//     * @param name
//     * @param listener 
//     */
//    public final void putPickListener(String name, PickListener listener) {
//        pickListeners.put(name, listener);
//    }
//    
//    /**
//     * 移除“点击”监听器
//     * @param name 
//     */
//    public final void removePickListener(String name) {
//        pickListeners.remove(name);
//    }
//    
//    /**
//     * 添加一个全局"释放"监听,当鼠标释放时会被调用
//     * @param name
//     * @param listener 
//     */
//    public final void putReleaseListener(String name, ReleaseListener listener) {
//        releaseListeners.put(name, listener);
//    }
//    
//    /**
//     * 移除鼠标“释放”监听器
//     * @param name 
//     */
//    public final void removeReleaseListener(String name) {
//        releaseListeners.remove(name);
//    }
//    
//    /**
//     * 重新注册默认的监听器,主要处理View组件的事件监听
//     */
//    public void registerDefaultListener() {
//        // 事件绑定,左键释放，
//        // 重要：镜头的视角旋转优先级较高，MAPPING_LEFT_RELEASE必须放在MAPPING_LEFT_CLICK先注册bindMouse，
//        // 以避免在点击UI后再拖动镜头时出现首次无法旋转视角的BUG(虽然再点击一下场景即可转动视角，但不舒服)。
//        bindMouse(app.getInputManager(), MAPPING_LEFT_RELEASE, MouseInput.BUTTON_LEFT, new ViewReleaseActionListener());
//        // 事件绑定,左键单击
//        bindMouse(app.getInputManager(), MAPPING_LEFT_CLICK, MouseInput.BUTTON_LEFT, new ViewPickActionListener());
//        
//        // ---------------------------------------------------------------------
//        // 默认的事件监听
//        // ---------------------------------------------------------------------
//        // 1.UI点击
//        putPickListener(LISTENER_PICK_VIEW, new UIPickListener(app, guiRoot));
//        // 2.镜头转动
//        putPickListener(LISTENER_PICK_CAMERA, new CameraRotPickListener());
//        
//        // 释放监听
//        putReleaseListener(LISTENER_RELEASE_CLICK, clickManager);
//        putReleaseListener(LISTENER_RELEASE_DRAG, dragManager);
//    }
//    
//    /**
//     * 清理已经注册的所有事件监听器，包括默认的View事件监听器和按键监听器
//     */
//    public void clearListener() {
//        pickListeners.clear();
//        releaseListeners.clear();
//        app.getInputManager().deleteMapping(MAPPING_LEFT_CLICK);
//        app.getInputManager().deleteMapping(MAPPING_LEFT_RELEASE);
//        clickManager.cleanup();
//        dragManager.cleanup();
//    }
//    
//    @Override
//    public void initialize(AppStateManager stateManager, final Application app) {
//        super.initialize(stateManager, app);
//        registerDefaultListener();
//    }
//
//    @Override
//    public void update(float tpf) {
////        super.update(tpf); // ignore
//
//
//
//        clickManager.update(tpf);
//        dragManager.update(tpf);
//    }
//    
//    @Override
//    public void cleanup() {
//        clickManager.cleanup();
//        dragManager.cleanup();
//        // 清理所有子UI
//        localGuiRoot.detachAllChildren();
//        clearListener();
//        super.cleanup();
//    }
//    
//    /**
//     * 清理界面上的所有UI
//     */
//    public void clearUI() {
//        localGuiRoot.detachAllChildren();
//    }
//    
//    // =========================================================================
//    
//    private void bindMouse(InputManager inputManager, String mapName, int button, InputListener listener) {
//        // 先移除旧的,再增加新的,避免listener重复(在一些退出后没有清理mapping时的情况可能发生)
//        if (inputManager.hasMapping(mapName)) {
//            inputManager.deleteMapping(mapName);
//        }
//        
//        Trigger t = new MouseButtonTrigger(button);
//        inputManager.addMapping(mapName, t);
//        inputManager.addListener(listener, mapName);
//    }
//    
//    private class ViewReleaseActionListener implements ActionListener {
//        
//        @Override
//        public void onAction(String name, boolean isPressed, float tpf) {
//            if (isPressed || !MAPPING_LEFT_RELEASE.equals(name)) {
//                return;
//            }
//            for (ReleaseListener listener : releaseListeners.values()) {
//                listener.release();
//            }
//        }
//    }
//    
//    // 左键点击
//    private class ViewPickActionListener implements ActionListener {
//        @Override
//        public void onAction(String name, boolean isPressed, float tpf) {
//            if (!MAPPING_LEFT_CLICK.equals(name)) {
//                return;
//            }
//            for (PickListener listener : pickListeners.values()) {
//                // 不进行重复选择,如果已经选中,则中断后面的选择监听.
//                if (listener.pick(isPressed, tpf)) {
//                    return;
//                }
//            }
//        }
//    }
//}
