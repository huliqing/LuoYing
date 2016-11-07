/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.object.attribute.Attribute;

/**
 *
 * @author huliqing
 */
public interface EntityListener {
    
    /**
     * 当角色的某个属性被击中<b>之前</b>调用。
     * @param attribute 指定击中的是哪一个属性名称
     * @param hitValue hitValue，<b>偿试</b>应用到指定属性上的值,应用后属性的值应该以属性内部获得的为准，因为一些
     * 属性类型可能会限制应用到属性上的值，比如限制取值范围的一些Number类型的值。
     * @param hitter 实施hit的目标角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色
     */
    void onHitAttributeBefore(Attribute attribute, Object hitValue, Entity hitter);
    
    /**
     * 当角色的某个属性被击中<b>之后</b>调用。
     * @param attribute 指定击中的是哪一个属性名称
     * @param hitValue hitValue，<b>偿试</b>应用到指定属性上的值,应用后属性的值应该以属性内部获得的为准，因为一些
     * 属性类型可能会限制应用到属性上的值，比如限制取值范围的一些Number类型的值。
     * @param hitter 实施hit的目标角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色.
     * @param oldValue 被击中前的属性值, <b>注意：这个值只是在hitValue应用前从属性{@link Attribute#getValue() }中获得的
     * 值，对于一些值为复合型的属性，这个oldValue可能仍然指定新的值的引用。</b>
     */
    void onHitAttributeAfter(Attribute attribute, Object hitValue, Entity hitter, Object oldValue);
    
}
