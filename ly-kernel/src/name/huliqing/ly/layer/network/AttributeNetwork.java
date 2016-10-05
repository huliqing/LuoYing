/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface AttributeNetwork extends Inject {

    /**
     * 给指定“名称”的属性添加值。注:所指定的属性必须存在，并且必须是 {@link NumberAttribute}类型的属性，
     * 否则什么也不做。
     * @param actor
     * @param attrName 属性名称
     * @param value 
     */
    void addNumberAttributeValue(Actor actor, String attrName, float value);
    
    
}
