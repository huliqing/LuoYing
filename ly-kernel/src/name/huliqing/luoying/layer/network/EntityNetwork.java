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
    
    /**
     * 设置目标Entity的某个属性的值。
     * @param entity
     * @param attributeName
     * @param value 
     */
    void setAttribute(Entity entity, String attributeName, Object value);
    
    /**
     * 给Entity的提定属性应用数值，目标属性必须存在，并且必须是Number类型，否则没有意义。
     * @param entity 指定的目标Entity
     * @param attributeName 指定的属性名称，必须是NumberAttribute类型
     * @param value 要作用的属性值，可正可负.
     * @param source 源Entity，即发起作用的源，如果没有特别的发起源，则这个参数可以为null.
     */
    void applyNumberAttributeValue(Entity entity, String attributeName, float value, Entity source);
    
}
