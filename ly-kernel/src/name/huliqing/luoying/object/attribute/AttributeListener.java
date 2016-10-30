/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

/**
 * 属性侦听器，主要用于监听属性的添加、移除
 * @author huliqing
 */
public interface AttributeListener {
    
    /**
     * 当角色添加了一个新属性后该方法被调用。
     * @param am 属性管理器
     * @param attribute 新添加的属性
     */
    void onAttributeAdded(AttributeManager am, Attribute attribute);
    
    /**
     * 当角色移除了一个属性后该方法被调用。
     * @param am 属性管理器
     * @param attribute 
     */
    void onAttributeRemoved(AttributeManager am, Attribute attribute);
}
