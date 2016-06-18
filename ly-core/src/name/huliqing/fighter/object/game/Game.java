/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SceneService;
import name.huliqing.fighter.object.AbstractPlayObject;
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
public  class Game<T extends GameData> extends AbstractPlayObject implements DataProcessor<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final SceneService sceneService = Factory.get(SceneService.class);

    public interface GameListener {
        /**
         * 当场景载入完毕后调用该方法
         */
        void onSceneLoaded();
    }
    
    protected T data;
    
    // ---- inner
    // 游戏侦听器
    protected List<GameListener> listeners; 
    
    // 是否打开逻辑：当作为客户端运行时，game中应该关闭游戏逻辑，因为游戏逻辑主要在服务端运行。
    protected boolean enabled = true;
    
    // 场景逻辑
    protected Scene scene;
    // 扩展逻辑
    protected final PlayManager playManager = new PlayManager(PlayObject.class);

    @Override
    public void initData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void initialize() {
        super.initialize();
        // 场景需要优先载入。然后再载入扩展逻辑，因为部分扩展逻辑可能需要依赖于场景中的物体。比如一些扩展逻辑可能需要
        // 确定场景地面已经载入之后才可以载入角色，否则角色可能直接就掉到下面去了.
        if (scene == null && data.getSceneData() != null) {
            scene = sceneService.loadScene(data.getSceneData());
            addLogic(scene);
            scene.initialize();
            if (listeners != null) {
                for (GameListener gl : listeners) {
                    gl.onSceneLoaded();
                }
            }
        }
    }

    @Override
    public final void update(float tpf) {
        if (enabled) {
            playManager.update(tpf);
            updateLogics(tpf);
        }
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
     * 设置或切换场景，如果存在旧的场景，则旧的场景会被立即清理掉，然后载入新的场景，新的场景会立即进行初始化。
     * @param scene 
     */
    public void setScene(Scene scene) {
        // 移除旧的场景
        if (this.scene != null) {
            removeLogic(this.scene);
        }
        // 切换新场景
        this.scene = scene;
        // 提前载入场景,必须的
        addLogic(this.scene);
        this.scene.initialize();
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
     * 判断游戏逻辑是否打开
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否打开游戏逻辑。注：场景逻辑不会受影响。
     * @param enabled 
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * 更新游戏逻辑
     * @param tpf 
     */
    protected  void updateLogics(float tpf) {
        // 由子类实现
    }

    /**
     * 当玩家选择一个角色时触发该方法。
     * @param actor 
     */
    public void onActorSelected(Actor actor) {
        // 由子类按需要覆盖
    }
    
    
}
