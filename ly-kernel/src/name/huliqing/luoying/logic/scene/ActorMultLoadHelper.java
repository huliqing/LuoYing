/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.logic.scene;

import com.jme3.app.Application;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.object.IntervalLogic;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.ThreadHelper;

/**
 * 用于多线程载入多个角色,当角色载入完成之后，当前逻辑将停止更新。
 * 即isEnabled为false, 该方法在另一线程中运行，并且是根据给定的角色ID，一个
 * 一个的载入，直到全部载入完成。
 * @author huliqing
 */
public abstract class ActorMultLoadHelper extends IntervalLogic {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 要载入的角色的id
    protected String[] actorIds;
    
    private Future<Entity> future;
    // 当前正在载入的角色id索引
    private int lastLoadIndex;
    // 是否已经处理结束
    private boolean end;
    
    /**
     * 指定要载入的角色ID 
     * @param actorIds
     */
    public ActorMultLoadHelper(String... actorIds) {
        super(0);
        this.actorIds = actorIds;
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        end = false;
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (end) {
            return;
        }
        
        // load OK
        if (future == null && lastLoadIndex < actorIds.length) {
            future = ThreadHelper.submit(new Callable<Entity>() {
                @Override
                public Entity call() throws Exception {
                    Entity actor = load(actorIds[lastLoadIndex]);
                    return actor;
                }
            });
        }
        
        if (future != null) {
            if (future.isDone()) {
                try {
                    Entity result = future.get();
                    callback(result, lastLoadIndex);
                    
//                    Logger.getLogger(getClass().getName()).log(Level.INFO, "callBack ok, lastLoadIndex={0}", lastLoadIndex);
                    
                    // 递增index等待载入下一个,只有处理成功之后才可以进行下一个的载入。
                    lastLoadIndex++;
                    future = null;
                } catch (Exception ex) {
                    if (future != null) {
                        future.cancel(true);
                    }
                    future = null;
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            } else if (future.isCancelled()){
                future = null;
            }
        }
        
        // 全部载入完毕停止
        if (lastLoadIndex >= actorIds.length) {
            this.end = true;
        }
    }
    
    /**
     * 实现角色的载入，注：该方法在多线程中运行，不建议在该方法在处理该场景
     * 信息.处理场景物体添加需要使用
     * @param actorId 需要载入的角色id
     * @return 
     */
    protected Entity load(String actorId) {
        if (actorId == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "The load actorId could not be null!");
        }
        return Loader.load(actorId);
    }
    
    /**
     * 处理已经载入的角色,负责将角色添加到场景中
     * @param actor 
     * @param loadIndex 
     */
    public abstract void callback(Entity actor, int loadIndex);
}
