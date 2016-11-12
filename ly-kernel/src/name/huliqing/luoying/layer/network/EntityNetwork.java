/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public interface EntityNetwork extends Inject {

    /**
     * 应用属性"Hit", 指定的属性必须存在，否则什么也不会做, 并且属性值必须符合指定的属性类型。<br>
     * <b>注：这里的hit更多的是指交互的意思，并不一定是指战斗时的"攻击"，该方法可以触发实体的EntityListener侦听器。
     * 一般该方法用于实体角色之间的属性交互，比如战斗时角色A对角色B某个属性进行了伤害（降低）; 或者队友之间的属性
     * 交互，如角色A对角色B加属性值（增加Buff)等，都可以调用这个方法来实现对属性的修改。
     * 这样可以从EntityListener中监听到某角色对某角色进行了攻击或者属性增益。</b>
     * @param entity 被攻击的对象
     * @param attribute 被攻击的属性
     * @param value 属性值，这个值会应用到指定的属性上.
     * @param hitter Hit源，如果不存在，则该参数可以为null.
     * @see #hitNumberAttribute(Entity, String, float, Entity) 
     */
    void hitAttribute(Entity entity, String attribute, Object value, Entity hitter);
    
    /**
     * hitNumberAttribute用于<b>递增</b>或<b>递减</b>角色的Number类型属性的值,通过addValue来增加或减少角色某个属性的值来
     * 影响角色，指定的属性必须是Number类型，否则什么也不做。一般可以作用于如下情形:<br>
     * 如角色A对角色B造成的技能伤害、增益BUFF、魔法对角色的伤害或增益影响等都可以调用这个方法来处理,。
     * @param entity 受到hit的对象, 属性的数值会应用到这个对象上。
     * @param attribute 要hit的目标属性，必须是Number类型，否则什么也不做。
     * @param addValue 要增加的属性值，可正可负, 这个值会增加到attribute属性上
     * @param hitter hit源，即发起作用的源，如果没有特别的发起源，则这个参数可以为null.
     * @see #hitAttribute(Entity, String, Object, Entity) 
     */
    void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter);
    
    /**
     * 给Entity添加物品
     * @param entity 
     * @param data
     * @param amount 添加的数量，必须大于0
     * @return 
     */
    boolean addData(Entity entity, ObjectData data, int amount);
    
    /**
     * 给Entity添加物品。
     * @param entity
     * @param objectId
     * @param amount 
     * @return  
     */
    boolean addData(Entity entity, String objectId, int amount);
    
    /**
     * 从Entity身上移除物品
     * @param entity
     * @param data 
     * @param amount 
     * @return  
     */
    boolean removeData(Entity entity, ObjectData data, int amount);
    
    /**
     * 让Entity使用一个物品
     * @param entity
     * @param data 
     * @return  
     */
    boolean useData(Entity entity, ObjectData data);
    
    /**
     * 让Entity使用一个指定id的物品，Entity必须存在这个物品才能使用。否则找不到这个物品时将什么也不会做。
     * @param entity
     * @param objectUniqueId 
     * @return  
     */
    boolean useData(Entity entity, long objectUniqueId);
}
