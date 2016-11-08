/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

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
    
    /**
     * 从角色身上获取物品,如果角色存上不存在该物品则返回null.
     * @param actor
     * @param id 
     * @return  
     */
    ObjectData getData(Entity actor, String id);
    
    /**
     * 获取角色身上所有的物体,注：返回的列表不可以直接修改,只能作为只读使用。
     * @param entity
     * @return 
     */
    List<ObjectData> getDatas(Entity entity);
}
