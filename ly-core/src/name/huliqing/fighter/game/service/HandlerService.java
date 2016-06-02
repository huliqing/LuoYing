/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface HandlerService extends Inject {
    
    /**
     * 判断当前角色是否可以使用指定的物品
     * @param actor
     * @param objectId
     * @return 
     */
    boolean canUse(Actor actor, String objectId);
    
    /**
     * 让角色强制使用某物品。一般要配合{@link #canUse(name.huliqing.fighter.object.actor.Actor, java.lang.String) }
     * 进行使用
     * @param actor
     * @param itemId 
     */
    void useForce(Actor actor, String itemId);
    
    /**
     * 使用物品，如果成功则返回true,否则返回false
     * @param actor
     * @param itemId 
     * @return 
     */
    boolean useObject(Actor actor, String itemId);
    
    /**
     * 移除角色身上的物品
     * @param actor
     * @param objectId
     * @param count
     * @return 
     */
    boolean removeObject(Actor actor, String objectId, int count);
}
