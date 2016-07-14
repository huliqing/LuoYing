/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.PlayManager;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.task.Task;

/**
 * 任务控制器，主要用于控制任务的执行。通过 {@link #addTask(Task) }
 * 来添加任务，多个任务形成一个任务链。每个任务执行完毕后会继续下一个任务的
 * 执行。
 * @author huliqing
 * @param <T>
 */
public  class Game<T extends GameData> extends AbstractAppState implements DataProcessor<T> {

    public interface GameListener {
        /**
         * 当游戏开始后
         * @param game
         */
        void onGameStarted(Game game);
    }
    
    protected T data;
    protected Application app;
    // 游戏侦听器
    protected List<GameListener> listeners; 
    // 扩展逻辑
    protected PlayManager playManager;
    // 场景
    protected Scene scene;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    /**
     * 设置场景
     * @param scene 
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
    }
    
    /**
     * 开始执行游戏逻辑
     */
    public void start() {
        playManager = new PlayManager(app, PlayObject.class);
        if (listeners != null) {
            for (GameListener gl : listeners) {
                gl.onGameStarted(this);
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        playManager.update(tpf);
    }
        
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        if (listeners != null) {
            listeners.clear();
        }
        playManager.cleanup();
        super.cleanup();
    }
    
    /**
     * 添加一个子游戏逻辑
     * @param logic 
     */
    public void addLogic(PlayObject logic) {
        playManager.attach(logic);
    }
    
    /**
     * 移除一个子游戏逻辑
     * @param logic 
     * @return  
     */
    public boolean removeLogic(PlayObject logic) {
        return playManager.detach(logic);
    }
    
    /**
     * 添加游戏侦听器
     * @param listener 
     */
    public void addListener(GameListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<GameListener>(2);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 移除指定的游戏侦听器，如果不存在则返回false.
     * @param listener
     * @return 
     */
    public boolean removeListener(GameListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    /**
     * 当玩家选择一个角色时触发该方法,注意：这个player可能是来自客户端，也可能是来自服务端的。
     * @param player 
     */
    public void onPlayerSelected(Actor player) {
        // 由子类按需要覆盖
    }
    
    
}
