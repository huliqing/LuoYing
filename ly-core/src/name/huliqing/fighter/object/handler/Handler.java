/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.object.DataProcessor;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 定义物品的使用方式，各种各样的ProtoData的使用方法可能都是不一样的。
 * @author huliqing
 */
public interface Handler<T extends HandlerData> extends DataProcessor<T> {
    
    /**
     * 移除指定的物品
     * @param actor 目标角色
     * @param objectId 物品id
     * @param count 要移除的数量
     * @param showNotice 是否显示提示信息
     * @return 
     */
    boolean remove(Actor actor, String objectId, int count);
    
    /**
     * 是否能够使用该物品
     * @param actor 使用物品的角色id
     * @param data 
     * @return 
     */
    boolean canUse(Actor actor, ProtoData data);
    
    /**
     * 强制使用物品
     * @param actor
     * @param data
     */
    void useForce(Actor actor, ProtoData data);
}
