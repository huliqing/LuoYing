/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface EntityNetwork extends Inject {

//    /**
//     * 设置目标Entity的某个属性的值。
//     * @param entity
//     * @param attributeName
//     * @param value 
//     */
//    void setAttribute(Entity entity, String attributeName, Object value);
    
    /**
     * 应用属性”攻击“, 指定的属性必须存在，否则什么也不会做, 并且属性值必须符合指定的属性类型。
     * @param entity 被攻击的对象
     * @param attribute 被攻击的属性
     * @param value 属性值，这个值会应用到被攻击的属性上
     * @param hitter 攻击源，如果不存在实际的攻击源，则该参数可以为null.
     */
    void hitAttribute(Entity entity, String attribute, Object value, Entity hitter);
    
    /**
     * hitNumberAttribute用于递增或递减角色的Number类型属性的值,通过applyValue来增加或减少角色某个属性的值来
     * 影响角色，指定的属性必须是Number类型，否则什么也不做。一般可以作用于如下情形:<br>
     * 如角色A对角色B造成的技能伤害、增益BUFF、魔法对角色的伤害或增益影响等都可以调用这个方法来处理,。
     * @param entity 受到hit的对象, 属性的数值会应用到这个对象上。
     * @param attribute 要hit的目标属性，必须是Number类型，否则什么也不做。
     * @param addValue 要作用的属性值，可正可负, 这个值会增加到attribute属性上
     * @param hitter hit源，即发起作用的源，如果没有特别的发起源，则这个参数可以为null.
     */
    void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter);
    
}
