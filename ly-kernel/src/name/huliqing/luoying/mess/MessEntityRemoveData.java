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
 * 从Entity身上移除物品
 * @author huliqing
 */
@Serializable
public class MessEntityRemoveData extends MessBase {
    
    private long entityId;
    private ObjectData objectData;
    private int amount;

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
     * 设置要移除的物品
     * @param objectData 
     */
    public void setObjectData(ObjectData objectData) {
        this.objectData = objectData;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * 设置要移除的物品数量
     * @param amount 
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            entity.removeObjectData(objectData, amount);
        }
    }
    
}
