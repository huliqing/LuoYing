/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 让Entity使用一件指定id（唯一id）的物品)
 * @author huliqing
 */
@Serializable
public class MessEntityUseDataById extends MessBase {
    
    private long entityId;
    private long objectUniqueId;

    public long getEntityId() {
        return entityId;
    }

    /**
     * 设置EntityId
     * @param entityId 
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getObjectUniqueId() {
        return objectUniqueId;
    }

    /**
     * 设置要使用的物品的id
     * @param objectUniqueId 
     */
    public void setObjectUniqueId(long objectUniqueId) {
        this.objectUniqueId = objectUniqueId;
    }
   
    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityService.class).useData(entity, objectUniqueId);
        }
    }
    
}
