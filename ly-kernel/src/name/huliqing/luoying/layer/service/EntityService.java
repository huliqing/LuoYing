/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface EntityService extends EntityNetwork {
    
    /**
     * 获取指定属性的数值，如果目标属性不是Number类型或
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    float getNumberAttributeValue(Entity entity, String attributeName, float defValue);
}
