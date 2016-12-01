/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.logic.scene;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYingException;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.utils.ThreadHelper;

/**
 * 用于多线程载入一个角色,当角色载入完成之后，当前逻辑将停止更新。
 * 即isEnabled为false.
 * @author huliqing
 */
public abstract class ActorLoadHelper extends AbstractGameLogic {
    
    // 要载入的角色的id
    private String actorId;
    private Future<Entity> future;
    
    public ActorLoadHelper() {}
    
    /**
     * 指定要载入的角色ID
     * @param actorId 
     */
    public ActorLoadHelper(String actorId) {
        this.actorId = actorId;
    }
    
    @Override
    protected void doLogicUpdate(float tpf) {
        if (future == null) {
            future = ThreadHelper.submit(new Callable<Entity>() {
                @Override
                public Entity call() throws Exception {
                    return load();
                }
            });
        }
        if (future != null && future.isDone()) {
            try {
                Entity actor = future.get();
                callback(actor);
                // 处理载入成功之后即停止运行，即只载入一次。
                future = null;
                game.removeLogic(this);
            } catch (Exception ex) {
                throw new LuoYingException("Could not load actor, actorId=" + actorId, ex);
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
     * 实现角色的载入，注：该方法在非渲染线程中运行，不要在这个方法中处理场景物体添加或删除，
     * 只实现角色载入并返回actor对象就可以，把角色添加到场景可以实现方法
     * @return 
     */
    protected Entity load() {
        if (actorId == null) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "The load actorId could not be null!");
        }
        return Loader.load(actorId);
    }
    
    /** 
     * 处理已经载入的角色,负责将角色添加到场景中
     * @param actor 
     */
    public abstract void callback(Entity actor);
}
