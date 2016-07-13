/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.SceneService;
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
public  class Game<T extends GameData> extends AbstractAppState implements DataProcessor<T>, Scene.SceneListener {
    private final SceneService sceneService = Factory.get(SceneService.class);

    public interface GameListener {
        /**
         * 当游戏开始后
         * @param game
         */
        void onGameStarted(Game game);
    }
    
    protected T data;
    
    // ---- inner
    protected Application app;
    
    // 游戏侦听器
    protected List<GameListener> listeners; 
    
    // 场景逻辑
    protected Scene scene;
    // 扩展逻辑
    protected PlayManager playManager;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
        playManager = new PlayManager(app, PlayObject.class);
        scene = sceneService.loadScene(data.getSceneData());
        scene.addSceneListener(this);
        app.getStateManager().attach(scene);
    }

    @Override
    public void onSceneInitialized(Scene scene) {
        if (listeners != null) {
            for (GameListener gl : listeners) {
                gl.onGameStarted(this);
            }
        }
    }
    
    @Override
    public void onSceneObjectAdded(Scene scene, Spatial objectAdded) {
    }

    @Override
    public void onSceneObjectRemoved(Scene scene, Spatial objectRemoved) {
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        playManager.update(tpf);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        if (scene != null) {
            stateManager.detach(scene);
        }
        super.stateDetached(stateManager); 
    }
        
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        playManager.cleanup();
        super.cleanup();
    }
    
    public Scene getScene() {
        return scene;
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
