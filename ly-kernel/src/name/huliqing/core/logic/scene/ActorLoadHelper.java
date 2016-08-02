/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.logic.scene;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.GameLogicData;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.game.Game;
import name.huliqing.core.object.gamelogic.AbstractGameLogic;
import name.huliqing.core.utils.ThreadHelper;

/**
 * 用于多线程载入一个角色,当角色载入完成之后，当前逻辑将停止更新。
 * 即isEnabled为false.
 * @author huliqing
 * @param <T>
 */
public abstract class ActorLoadHelper<T extends GameLogicData> extends AbstractGameLogic<T> {
    private final ActorService actorService = Factory.get(ActorService.class);
    
    // 要载入的角色的id
    protected String actorId;
    private Future<Actor> future;
    private Game game;
    
    public ActorLoadHelper() {}
    
    /**
     * 指定要载入的角色ID
     * @param actorId 
     */
    public ActorLoadHelper(String actorId) {
        this.actorId = actorId;
    }

    @Override
    public void initialize(Game game) {
        super.initialize(game); 
        this.game = game;
    }
    
    @Override
    protected void doLogic(float tpf) {
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
                future = null;
                game.removeLogic(this);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void cleanup() {
        future = null;
        super.cleanup(); 
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
