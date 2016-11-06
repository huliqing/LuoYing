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
     * 获取指定属性的数值，如果属性不存在或者不是Number类型，则返回defValue。
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    Number getNumberAttributeValue(Entity entity, String attributeName, float defValue);
    
    /**
     * 获取指定Boolean属性的值，如果属性不存在或者不是Boolean类型，则返回defValue。
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    boolean getBooleanAttributeValue(Entity entity, String attributeName, boolean defValue);
}
