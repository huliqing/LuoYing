/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import name.huliqing.fighter.Fighter;
import name.huliqing.fighter.data.SceneData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.view.TeamView;
import name.huliqing.fighter.object.PlayManager;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.object.NetworkObject;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.view.View;
import name.huliqing.fighter.ui.state.UIState;

/**
 *
 * @author huliqing
 */
public abstract class PlayState extends AbstractAppState {
    private static final Logger LOG = Logger.getLogger(PlayState.class.getName());
    
    public interface PlayListener {
        
        /**
         * 当场景中添加了物体时触发
         * @param spatial 
         */
        void onObjectAdded(Object spatial);
        
        /**
         * 当场景中移除了物体时触发
         * @param spatial 
         */
        void onObjectRemoved(Object spatial);
        
        /**
         * 当退出PlayState时
         */
        void onExit();
        
    }
    
    protected Fighter app;
    
    // 管理所有的PlayObject的运行
    protected final PlayManager playManager = new PlayManager(PlayObject.class);
    
    // 侦听器
    private final List<PlayListener> listeners = new ArrayList<PlayListener>();
    
    // 场景中需要与客户端进行同步的对象。
    private final Map<Long, NetworkObject> networkObjects = new HashMap<Long, NetworkObject>();
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Fighter) app;
        this.listeners.clear();
        this.networkObjects.clear();
    }

    @Override
    public void update(float tpf) {
        playManager.update(tpf);
    }
    
    /**
     * 添加一个侦听器
     * @param listener 
     */
    public final void addListener(PlayListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 移除一个侦听器
     * @param listener 
     */
    public final void removeListener(PlayListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * 给场景中添加物体
     * @param object 
     */
    public void addObject(Object object, boolean gui) {
        if (object instanceof PlayObject) {
            playManager.attach((PlayObject) object);
        }
        
        // 检查并放到同步对象中
        if (object instanceof NetworkObject) {
            NetworkObject so = (NetworkObject) object;
            networkObjects.put(so.getSyncId(), so);
        }
        
        // 触发侦听器
        for (PlayListener lis : listeners) {
            lis.onObjectAdded(object);
        }
    }
    
    /**
     * 从场景中移除物体
     * @param object 
     */
    public void removeObject(Object object) {
        // 检查并从同步对象中移除
        if (object instanceof NetworkObject) {
            NetworkObject so = (NetworkObject) object;
            networkObjects.remove(so.getSyncId());
        }
        
        if (object instanceof PlayObject) {
            playManager.detach((PlayObject) object);
        }
        
        // 触发侦听
        for (PlayListener lis : listeners) {
            lis.onObjectRemoved(object);
        }
    }
    
    /**
     * 获取场景中的同步对象
     * @return 
     */
    public final NetworkObject getSyncObjects(long objectId) {
        return networkObjects.get(objectId);
    }
    
    public Application getApp() {
        return this.app;
    }
    
    /**
     * 退出当前PlayState,并返回到开始界面。
     */
    public void exit() {
        for (PlayListener lis : listeners) {
            lis.onExit();
        }
        // 退出到"开始面板"
        ((Fighter) getApp()).changeStartState();
    }
    
    @Override
    public void cleanup() {
        listeners.clear();
        networkObjects.clear();
        playManager.cleanup();
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
    /**
     * 获取场景信息
     * @return 
     */
    public abstract Scene getScene();
    
    /**
     * 判断节点是否存在于场景中。
     * @return 
     */
    public abstract boolean isInScene(Spatial spatial);

    /**
     * 获取当前场景所有活动对象，包括player,如果没有，则返回empty.
     * 不要返回null.
     * @return 
     */
    public abstract List<Actor> getActors();
    
    /**
     * 获取视图组件,视图组件是需要同步到客户端的。
     * @return 
     */
    public abstract List<View> getViews();
    
    /**
     * 获取玩家角色，如果不存在玩家角色，则返回null.
     * @return 
     */
    public abstract Actor getPlayer();
    
    /**
     * 添加HUD提示信息
     * @param message 
     * @param messageType
     */
    public abstract void addMessage(String message, MessageType messageType);
    
    /**
     * 获取当前的目标对象
     * @return 
     */
    public abstract Actor getTarget();
    
    /**
     * 设置当前界面的主目标对象
     * @param target 
     */
    public abstract void setTarget(Actor target);
    
    /**
     * 把目标设置为玩家
     * @param actor 
     */
    public abstract void setPlayer(Actor actor);
    
    /**
     * 载入场景数据
     * @param sceneData 
     */
    public abstract void initScene(SceneData sceneData);
    
    /**
     * 显示角色选择面板
     * @param selectableActors 
     */
    public abstract void showSelectPanel(List<String> selectableActors);
    
    /**
     * 切换显示当前界面的所有UI.注意:该方法将只影响当前已经存在的UI,对后续
     * 添加到场景中的UI不会有影响.也就是,如果想要只显示某个特殊UI,则先设置
     * setUIVisiable(false),然后再把特定UI添加到UI根节点中
     * @param visiable 
     */
    public abstract void setUIVisiable(boolean visiable);
    
    /**
     * 获取队伍面板
     * @return 
     */
    public abstract TeamView getTeamView();

    
}
