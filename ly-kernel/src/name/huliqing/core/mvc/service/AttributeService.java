/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface AttributeService extends Inject{
    
    /**
     * 判断角色是否存在某个属性，如果不存在则返回false.
     * @param actor
     * @param attributeId 如果不存在该属性或者attributeId=null则返回false.
     * @return 
     */
    boolean existsAttribute(Actor actor, String attributeId);
    
    /**
     * 获取属性的动态值,如果角色不存在指定的属性设置则返回0.
     * @param actor
     * @param attributeId
     * @return 
     */
    float getDynamicValue(Actor actor, String attributeId);
    
    /**
     * 获得角色某个属性的最高值，如果不存在该属性则返回0
     * @param actor
     * @param attributeId
     * @return 
     */
    float getMaxValue(Actor actor, String attributeId);
    
    /**
     * 增加或减少角色某个属性的值,如果目标没有指定的属性则什么也不做.
     * @param actor 目标角色
     * @param attributeId 属性ID
     * @param amount 数量，正为增加，负为减少
     */
    void applyDynamicValue(Actor actor, String attributeId, float amount);
    
    /**
     * 使角色某个属性的动态值限制在0~max范围内.注：max等于等级值levelValue和
     * 静态值staticValue之和。
     * @param actor
     * @param attributeId 
     */
    void clampDynamicValue(Actor actor, String attributeId);
    
    /**
     * 增加或减少角色某个属性的静态值,如果目标没有指定的属性则什么也不做。
     * @param actor
     * @param attributeId
     * @param amount 
     */
    void applyStaticValue(Actor actor, String attributeId, float amount);
    
    /**
     * 获取属性
     * @param actor
     * @param attribute
     * @return 
     */
    AttributeData getAttributeData(Actor actor, String attribute);

    /**
     * 获取角色的所有属性
     * @param actor
     * @return 
     */
    List<AttributeData> getAttributes(Actor actor);
    
    /**
     * 同步目标角色的指定属性值
     * @param actor
     * @param attributeId
     * @param levelValue
     * @param staticValue
     * @param dynamicValue 
     */
    void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue);
    
}
