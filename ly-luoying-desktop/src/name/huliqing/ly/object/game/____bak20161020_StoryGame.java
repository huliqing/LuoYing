///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.object.game;
//
//import com.jme3.app.Application;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.network.ActorNetwork;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.object.actor.Actor;
//
///**
// * 故事模式的游戏方式，有一些特殊的游戏逻辑行为。并且一旦主角死亡也就是任务失败
// * @author huliqing
// */
//public abstract class StoryGame extends RpgGame {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
//    
//    // 任务列表
//    protected final List<GameTask> tasks = new ArrayList<GameTask>();
//    
//    // 当前正在执行的任务
//    private GameTask current;
//    
//    // 已经执行完的task数
//    private int finishCount;
//    
//    /**
//     * 主角的用户组id
//     */
//    public final static int GROUP_PLAYER = 1;
//    
//    protected Actor player;
//    
//    public void addTask(GameTask task) {
//        tasks.add(task);
//    }
//
//    @Override
//    public void initialize(Application app) {
//        super.initialize(app);
//        finishCount = 0;
//        current = null;
//    }
//    
//    /**
//     * 是否有下一个任务
//     * @return 
//     */
//    public boolean hasNext() {
//        return finishCount < tasks.size();
//    }
//    
//    /**
//     * 执行下一个任务
//     */
//    private void doNext() {
//        GameTask previous = current;
//        if (current == null) {
//            current = tasks.get(0);
//        } else {
//            int i = tasks.indexOf(current) + 1; // 由外部判断是否有hasNext
//            current = tasks.get(i);
//        }
//        current.start(previous);
//    }
//
//    @Override
//    public void update(float tpf) {
//        super.update(tpf);
//        if (player == null) {
//            // 设置player分组
//            player = playService.getPlayer();
//            if (player != null) {
//                // 给玩家指定分组
//                actorNetwork.setGroup(player, GROUP_PLAYER);
//                
//                // 子类初始化
//                doStoryInitialize();
//                doNext();
//            }
//            return;
//        }
//        
//        // update tasks
//        if (current.isFinished()) {
//            finishCount++;
//            if (hasNext()) {
//                doNext();
//            } else {
//                
//                // remove20160716
////                app.getStateManager().detach(this);
//
//            }
//        } else {
//            current.update(tpf);
//        }
//    }
//    
//    /**
//     * 清理并结束当前游戏
//     */
//    @Override
//    public void cleanup() {
//        for (GameTask task : tasks) {
//            if (!task.isFinished()) {
//                task.cleanup();
//            }
//        }
//        tasks.clear();
//        player = null;
//        super.cleanup();
//    }
//    
//    /**
//     * 故事模式的初始化，该方法的调用在gameInitialize之后。并且是在主玩家(player)载入之后才会被调用。
//     */
//    protected abstract void doStoryInitialize();
//}
