/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * EntityNetwork用于服务端将对于Entity的修改事件广播到所有客户端。
 * 客户端对于EntityNetwork的调用会被忽略。
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
     * 给Entity添加物品，注：对于一些可以量化的类型的物体，可能添加后的物体会被合并在已有的物体中,
     * 而对于一些不可叠加的物体，新的物体可能被抛弃或者旧的物体可能会被替换，具体由各个实现模块去决定。
     * 确定指定的物体是否成功添加到entity中可以通过 {@link EntityService#getObjectDataByUniqueId(Entity, long) }
     * @param entity 
     * @param data
     * @param amount 添加的数量，必须大于0
     * @return 
     * @see EntityService#getObjectData(Entity, String);
     * @see EntityService#getObjectDataByTypeId(Entity, String);
     * @see EntityService#getObjectDataByUniqueId(Entity, long);
     */
    boolean addObjectData(Entity entity, ObjectData data, int amount);
   
    // remove20161122不使用直接id添加物品的方式，这会造成添加后的物品的唯一id(uniqueId)在客户端和服务端不一致的问题。
//    /**
//     * 给Entity添加物品。
//     * @param entity
//     * @param objectId
//     * @param amount 
//     * @return  
//     */
//    boolean addObjectData(Entity entity, String objectId, int amount);
    
    /**
     * 从Entity身上移除指定id的物品, 如果角色身上不存在指定的物体，则该方法什么也不会做。
     * @param entity 角色
     * @param objectUniqueId 物品的唯一id,角色必须拥有这个物品
     * @param amount 移除的数量
     * @return 
     */
    boolean removeObjectData(Entity entity, long objectUniqueId, int amount);
    
    /**
     * 让Entity使用一个物品。注：这个物品可以是角色身上存在的，也可能不是角色身上拥有的。具体由特定的模块去实现。
     * @param entity
     * @param data 
     * @return  
     */
    boolean useObjectData(Entity entity, ObjectData data);
    
    /**
     * 让Entity使用一个指定id的物品，Entity必须存在这个物品才能使用。如果找不到这个物品时将什么也不会做。
     * @param entity
     * @param objectUniqueId 
     * @return  
     */
    boolean useObjectData(Entity entity, long objectUniqueId);
}
