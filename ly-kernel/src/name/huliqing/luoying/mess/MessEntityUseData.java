/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 * 让Entity使用一件指定的物品
 * @author huliqing
 */
@Serializable
public class MessEntityUseData extends MessBase {
    
    private long entityId;
    private ObjectData objectData;

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

    public ObjectData getObjectData() {
        return objectData;
    }

    /**
     * 设置要使用的物品
     * @param objectData 
     */
    public void setObjectData(ObjectData objectData) {
        this.objectData = objectData;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            entity.useObjectData(objectData);
        }
    }
    
}
