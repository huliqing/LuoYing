/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.game;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.logic.scene.ActorCleanLogic;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 故事模式的游戏方式，有一些特殊的游戏逻辑行为。并且一旦主角死亡也就是任务失败
 * @author huliqing
 */
public abstract class StoryGame extends Game {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 任务列表
    protected final List<GameTask> tasks = new ArrayList<GameTask>();
    
    // 当前正在执行的任务
    private GameTask current;
    
    // 已经执行完的task数
    private int finishCount;

    // 检查主角是否死亡
    private PlayerDeadChecker playerChecker;
    
    /**
     * 主角的用户组id
     */
    public final static int GROUP_PLAYER = 1;
    
    // 判断是否处于开始状态
    protected boolean started;
    
    public void addTask(GameTask task) {
        tasks.add(task);
    }

    @Override
    public final void initialize() {
        super.initialize();
        finishCount = 0;
        current = null;
        
        // 角色清理器
        addLogic(new ActorCleanLogic());
        
        // 默认false,需要等待载入player
        started = false;
    }
    
    /**
     * 是否有下一个任务
     * @return 
     */
    public boolean hasNext() {
        return finishCount < tasks.size();
    }
    
    protected abstract void doInit();
    
    /**
     * 执行下一个任务
     */
    private void doNext() {
        GameTask previous = current;
        if (current == null) {
            current = tasks.get(0);
        } else {
            int i = tasks.indexOf(current) + 1; // 由外部判断是否有hasNext
            current = tasks.get(i);
        }
        current.start(previous);
    }

    @Override
    protected void updateLogics(float tpf) {
        if (!started) {
            // 设置player分组
            Actor player = playService.getPlayer();
            if (player != null) {
                // 给玩家指定分组
                actorNetwork.setGroup(player, GROUP_PLAYER);
                // 检查角色是否存活
                playerChecker = new PlayerDeadChecker(this, player);
                addLogic(playerChecker);
                
                // 子类初始化
                doInit();
                doNext();
                
                started = true;
            }
            return;
        }
        
        if (playerChecker.isDead()) {
            return;
        }
        
        // update tasks
        if (current.isFinished()) {
            finishCount++;
            if (hasNext()) {
                doNext();
            } else {
                playService.removePlayObject(this);
            }
        } else {
            current.update(tpf);
        }
    }
    
    /**
     * 清理并结束当前游戏
     */
    @Override
    public void cleanup() {
        for (GameTask task : tasks) {
            if (!task.isFinished()) {
                task.cleanup();
            }
        }
        tasks.clear();
        super.cleanup();
    }
    
}
