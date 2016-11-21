/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 从Entity身上移除物品
 * @author huliqing
 */
@Serializable
public class MessEntityRemoveData extends MessBase {
    
    private long entityId;
    private long objectId;
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

    /**
     * 获取指定物品的id
     * @return 
     */
    public long getObjectId() {
        return objectId;
    }

    /**
     * 设置指定物品的id(唯一id)
     * @param objectUniqueId 
     */
    public void setObjectId(long objectUniqueId) {
        this.objectId = objectUniqueId;
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
            Factory.get(EntityService.class).removeObjectData(entity, objectId, amount);
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityNetwork.class).removeObjectData(entity, objectId, amount);
        }
    }
    
}
