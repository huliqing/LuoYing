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
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.GameLogicData;
import name.huliqing.fighter.game.service.GameLogicService;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.gamelogic.GameLogic;
import name.huliqing.fighter.object.gamelogic.GameLogicManager;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.task.Task;

/**
 * 任务控制器，主要用于控制任务的执行。通过 {@link #addTask(Task) }
 * 来添加任务，多个任务形成一个任务链。每个任务执行完毕后会继续下一个任务的
 * 执行。
 * @author huliqing
 * @param <T>
 */
public abstract class Game<T extends GameData> extends AbstractAppState implements DataProcessor<T> {
    private final GameLogicService gameLogicService = Factory.get(GameLogicService.class);
    
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
    protected GameLogicManager gameLogicManager;
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

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }

    /**
     * 设置场景
     * @param scene 
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * 获取当前游戏场景
     * @return 
     */
    public Scene getScene() {
        return scene;
    }
    
    @Override
    public final void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); 
        this.app = app;
        
        // 初始化游戏逻辑数据
        gameLogicManager = new GameLogicManager(this, GameLogic.class);
        List<GameLogicData> logics = data.getGameLogics();
        if (logics != null) {
            for (GameLogicData gld : logics) {
                GameLogic gl = gameLogicService.loadGameLogic(gld);
                gameLogicManager.attach(gl);
            }
        }
        
        // 初始化游戏，由子类实现决定。
        gameInitialize();
        
        // 执行侦听器。
        if (listeners != null) {
            for (GameListener gl : listeners) {
                gl.onGameStarted(this);
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        gameLogicManager.update(tpf);
    }
        
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        if (listeners != null) {
            listeners.clear();
        }
        gameLogicManager.cleanup();
        super.cleanup();
    }
    
    /**
     * 添加一个子游戏逻辑
     * @param logic 
     */
    public void addLogic(GameLogic logic) {
        gameLogicManager.attach(logic);
    }
    
    /**
     * 移除一个子游戏逻辑
     * @param logic 
     * @return  
     */
    public boolean removeLogic(GameLogic logic) {
        return gameLogicManager.detach(logic);
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
        
    /**
     * 初始化游戏数据
     */
    protected abstract void gameInitialize();
}
