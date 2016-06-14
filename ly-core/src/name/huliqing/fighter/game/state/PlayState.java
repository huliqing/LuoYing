/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import name.huliqing.fighter.game.state.game.GameState;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.fighter.Fighter;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.state.game.GameState.PlayListener;
import name.huliqing.fighter.game.view.TeamView;
import name.huliqing.fighter.object.NetworkObject;
import name.huliqing.fighter.object.view.View;

/**
 *
 * @author huliqing
 */
public abstract class PlayState extends AbstractAppState {
    private static final Logger LOG = Logger.getLogger(PlayState.class.getName());
    
    protected Fighter app;
    protected GameState gameState;
    
    public PlayState(GameState gameState) {
        this.gameState = gameState;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Fighter) app;
        stateManager.attach(gameState);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        stateManager.detach(gameState);
        super.stateDetached(stateManager);
    }

    @Override
    public void update(float tpf) {}
    
    /**
     * 退出当前PlayState,并返回到开始界面。
     */
    public void exit() {
        // 退出到"开始面板"
        ((Fighter) getApp()).changeStartState();
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    public Application getApp() {
        return this.app;
    }
    
    /**
     * 切换游戏
     * @param gameId 游戏ID
     */
    public abstract void changeGameState(String gameId);
    
    /**
     * 添加一个侦听器
     * @param listener 
     */
    public final void addListener(PlayListener listener) {
        gameState.addListener(listener);
    }
    
    /**
     * 移除一个侦听器
     * @param listener 
     * @return  
     */
    public final boolean removeListener(PlayListener listener) {
        return gameState.removeListener(listener);
    }
    
    /**
     * 给场景中添加物体
     * @param object 
     * @param gui 
     */
    public void addObject(Object object, boolean gui) {
        gameState.addObject(object, gui);
    }
    
    /**
     * 从场景中移除物体
     * @param object 
     */
    public void removeObject(Object object) {
        gameState.removeObject(object);
    }
    
    /**
     * 获取场景中的同步对象
     * @param objectId
     * @return 
     */
    public final NetworkObject getSyncObjects(long objectId) {
        return gameState.getSyncObjects(objectId);
    }
    
    /**
     * 获取当前正在运行中的游戏
     * @return 
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * 判断节点是否存在于场景中。
     * @param spatial
     * @return 
     */
    public boolean isInScene(Spatial spatial) {
        return gameState.isInScene(spatial);
    }

    /**
     * 获取当前场景所有活动对象，包括player,如果没有，则返回empty.
     * 不要返回null.
     * @return 
     */
    public List<Actor> getActors() {
        return gameState.getActors();
    }
    
    /**
     * 获取视图组件,视图组件是需要同步到客户端的。
     * @return 
     */
    public List<View> getViews() {
        return gameState.getViews();
    }
    
    /**
     * 获取玩家角色，如果不存在玩家角色，则返回null.
     * @return 
     */
    public Actor getPlayer() {
        return gameState.getPlayer();
    }
    
    /**
     * 添加HUD提示信息
     * @param message 
     * @param messageType
     */
    public void addMessage(String message, MessageType messageType) {
        gameState.addMessage(message, messageType);
    }
    
    /**
     * 获取当前的目标对象
     * @return 
     */
    public Actor getTarget() {
        return gameState.getTarget();
    }
    
    /**
     * 设置当前界面的主目标对象
     * @param target 
     */
    public void setTarget(Actor target) {
        gameState.setTarget(target);
    }
    
    /**
     * 把目标设置为玩家
     * @param actor 
     */
    public void setPlayer(Actor actor) {
        gameState.setPlayer(actor);
    }
    
    /**
     * 显示角色选择面板
     * @param selectableActors 
     */
    public void showSelectPanel(List<String> selectableActors) {
        gameState.showSelectPanel(selectableActors);
    }
    
    /**
     * 切换显示当前界面的所有UI.注意:该方法将只影响当前已经存在的UI,对后续
     * 添加到场景中的UI不会有影响.也就是,如果想要只显示某个特殊UI,则先设置
     * setUIVisiable(false),然后再把特定UI添加到UI根节点中
     * @param visiable 
     */
    public void setUIVisiable(boolean visiable) {
        gameState.setUIVisiable(visiable);
    }
    
    /**
     * 获取队伍面板
     * @return 
     */
    public TeamView getTeamView() {
        return gameState.getTeamView();
    }

    
}
