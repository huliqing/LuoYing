///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.layer.service;
//
//import java.util.List;
//import name.huliqing.luoying.data.AttributeData;
//import name.huliqing.luoying.layer.network.AttributeNetwork;
//import name.huliqing.luoying.object.attribute.Attribute;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.module.AttributeListener;
//
///**
// *
// * @author huliqing
// */
//public interface AttributeService extends AttributeNetwork {
//    
//    Attribute loadAttribute(String attributeId);
//    
//    Attribute loadAttribute(AttributeData data);
//    
//    /**
//     * 给指定角色添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
//     * @param actor
//     * @param attribute 
//     */
//    void addAttribute(Entity actor, Attribute attribute);
//    
//    /**
//     * 使用属性“ID”来查找属性,如果角色不存在指定属性则返回null.
//     * @param <T>
//     * @param actor
//     * @param attrId
//     * @return 
//     */
//    <T extends Attribute> T getAttributeById(Entity actor, String attrId);
//    
//    /**
//     * 使用属性“名称”来查找属性，如果角色不存在指定属性则返回null.
//     * @param <T>
//     * @param actor
//     * @param attrName
//     * @return 
//     */
//    <T extends Attribute> T getAttributeByName(Entity actor, String attrName);
//    
//    /**
//     * 获取角色的所有属性
//     * @param actor
//     * @return 
//     */
//    List<Attribute> getAttributes(Entity actor);
//    
//    /**
//     * 给角色添加一个属性侦听器
//     * @param actor
//     * @param attributeListener 
//     */    
//    void addListener(Entity actor, AttributeListener attributeListener);
//    
//    /**
//     * 从角色身上移除一个属性侦听器
//     * @param actor
//     * @param attributeListener
//     * @return 
//     */
//    boolean removeListener(Entity actor, AttributeListener attributeListener);
//    
//    /**
//     * 设置指定属性名称的值,属性类型必须是SimpleAttribute
//     * @param <V>
//     * @param actor
//     * @param attrName 属性名称
//     * @param attrValue 
//     */
//    <V extends Object> void setSimpleAttributeValue(Entity actor, String attrName, V attrValue);
//    
//    /**
//     * 获取指定“名称“的NumberAttribute类型的属性的值，目标属性必须存在，并且必须是NubmerAttribute，
//     * 否则这个方法将指返回defValue值。
//     * @param actor
//     * @param attrName 属性"名称"非id.
//     * @param defValue 默认值，如果找不到指定的属性或属性不是NumberAttribute类型则返回这个默认值。
//     * @return 
//     */
//    float getNumberAttributeValue(Entity actor, String attrName, float defValue);
//    
//    
//}
