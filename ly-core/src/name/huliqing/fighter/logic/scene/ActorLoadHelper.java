/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.logic.scene;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.utils.ThreadHelper;

/**
 * 用于多线程载入一个角色,当角色载入完成之后，当前逻辑将停止更新。
 * 即isEnabled为false.
 * @author huliqing
 */
public abstract class ActorLoadHelper extends IntervalLogic {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 要载入的角色的id
    protected String actorId;
    private Future<Actor> future;
    // 是否已经处理结束
    private boolean end;

    public ActorLoadHelper() {
        super(0);
    }
    
    /**
     * 指定要载入的角色ID
     * @param actorId 
     */
    public ActorLoadHelper(String actorId) {
        super(0);
        this.actorId = actorId;
    }

    @Override
    public void initialize() {
        super.initialize(); 
        end = false;
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (end) {
            return;
        }
        if (future == null) {
            future = ThreadHelper.submit(new Callable<Actor>() {
                @Override
                public Actor call() throws Exception {
                    return load();
                }
            });
        }
        if (future != null && future.isDone()) {
            try {
                Actor actor = future.get();
                callback(actor);
                // 处理载入成功之后即停止运行，即只载入一次。
                this.future = null;
                this.end = true;
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
    
    /**
     * 设置需要载入的角色的id
     * @param actorId 
     */
    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
    
    /**
     * 实现角色的载入，注：该方法在多线程中运行，不建议在该方法在处理该场景
     * 信息.处理场景物体添加需要使用 {@link #callback(name.huliqing.fighter.actor.Actor) }
     * @param actorId 需要载入的角色id
     * @return 
     */
    protected Actor load() {
        if (actorId == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "The load actorId could not be null!");
        }
        return actorService.loadActor(actorId);
    }
    
    /**
     * 处理已经载入的角色,负责将角色添加到场景中
     * @param actor 
     */
    public abstract void callback(Actor actor);
}
