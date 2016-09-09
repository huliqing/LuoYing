/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.NumberAttribute;
import name.huliqing.core.object.module.AttributeListener;

/**
 *
 * @author huliqing
 */
public interface AttributeService extends Inject{
    
    Attribute loadAttribute(String attributeId);
    
    Attribute loadAttribute(AttributeData data);
    
    /**
     * 给指定角色添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
     * @param actor
     * @param attribute 
     */
    void addAttribute(Actor actor, Attribute attribute);
    
    /**
     * 使用属性“ID”来查找属性,如果角色不存在指定属性则返回null.
     * @param <T>
     * @param actor
     * @param attrId
     * @return 
     */
    <T extends Attribute> T getAttributeById(Actor actor, String attrId);
    
    /**
     * 使用属性“名称”来查找属性，如果角色不存在指定属性则返回null.
     * @param <T>
     * @param actor
     * @param attrName
     * @return 
     */
    <T extends Attribute> T getAttributeByName(Actor actor, String attrName);
    
    /**
     * 获取角色的所有属性
     * @param actor
     * @return 
     */
    List<Attribute> getAttributes(Actor actor);
    
    /**
     * 给角色添加一个属性侦听器
     * @param actor
     * @param attributeListener 
     */    
    void addListener(Actor actor, AttributeListener attributeListener);
    
    /**
     * 从角色身上移除一个属性侦听器
     * @param actor
     * @param attributeListener
     * @return 
     */
    boolean removeListener(Actor actor, AttributeListener attributeListener);
    
    // remove20160909
//    /**
//     * 设置指定属性名称的值。
//     * @param <V>
//     * @param actor
//     * @param attrName 属性“名称”
//     * @param value 
//     */
//    <V extends Object> void setAttributeValue(Actor actor, String attrName, V value);
    
    /**
     * 设置指定属性名称的值,属性类型必须是SimpleAttribute
     * @param <V>
     * @param actor
     * @param attrName 属性名称
     * @param attrValue 
     */
    <V extends Object> void setSimpleAttributeValue(Actor actor, String attrName, V attrValue);
    
    /**
     * 给指定“名称”的属性添加值。注:所指定的属性必须存在，并且必须是 {@link NumberAttribute}类型的属性，
     * 否则什么也不做。
     * @param actor
     * @param attrName 属性名称
     * @param value 
     */
    void addNumberAttributeValue(Actor actor, String attrName, float value);
    
    /**
     * 获取指定“名称“的NumberAttribute类型的属性的值，目标属性必须存在，并且必须是NubmerAttribute，
     * 否则这个方法将指返回defValue值。
     * @param actor
     * @param attrName 属性"名称"非id.
     * @param defValue 默认值，如果找不到指定的属性或属性不是NumberAttribute类型则返回这个默认值。
     * @return 
     */
    float getNumberAttributeValue(Actor actor, String attrName, float defValue);
    
    
}
