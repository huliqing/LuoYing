/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity;

/**
 *
 * @author huliqing
 */
public interface EntityListener {
    
    /**
     * 当角色的某个属性被击中<b>之前</b>调用。
     * @param attribute 指定击中的是哪一个属性名称
     * @param value 属性值，这个值将会应用到指定的属性上
     * @param hitter 实施hit的目标角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色
     */
    void onHitAttributeBefore(String attribute, Object value, Entity hitter);
    
    /**
     * 当角色的某个属性被击中<b>之后</b>调用。
     * @param attribute 指定击中的是哪一个属性名称
     * @param newValue 被击中后属性的新值
     * @param oldValue 被击中前的属性值
     * @param hitter 实施hit的目标角色，注：hitter可能为null，因hitter有可能不是一个实际存在的角色
     */
    void onHitAttributeAfter(String attribute, Object newValue, Object oldValue, Entity hitter);
    
}
