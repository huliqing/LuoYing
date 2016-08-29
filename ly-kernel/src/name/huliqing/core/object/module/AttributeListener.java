/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;

/**
 * 属性侦听器，主要用于监听角色的属性，当角色添加、修改或移除了属性时触这个侦听器。
 * @author huliqing
 */
public interface AttributeListener {
    
    /**
     * 当角色添加了一个新属性后该方法被调用。
     * @param actor 角色
     * @param attribute 新添加的属性
     */
    void onAttributeAdded(Actor actor, Attribute attribute);
    
    /**
     * 当角色移除了一个属性后该方法被调用。
     * @param actor
     * @param attribute 
     */
    void onAttributeRemoved(Actor actor, Attribute attribute);
}
