/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.mess.MessEntityAddData;
import name.huliqing.luoying.mess.MessEntityHitNumberAttribute;
import name.huliqing.luoying.mess.MessEntityHitAttribute;
import name.huliqing.luoying.mess.MessEntityRemoveData;
import name.huliqing.luoying.mess.MessEntityUseData;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class EntityNetworkImpl implements EntityNetwork {
    private final static Network NETWORK = Network.getInstance();
    private EntityService entityService;

    @Override
    public void inject() {
        entityService = Factory.get(EntityService.class);
    }

    @Override
    public void hitAttribute(Entity entity, String attribute, Object value, Entity hitter) {
        if (NETWORK.isClient()) {
            return;
        }
        if (NETWORK.hasConnections()) {
            MessEntityHitAttribute mess = new MessEntityHitAttribute();
            mess.setEntityId(entity.getEntityId());
            mess.setAttribute(attribute);
            mess.setValue(value);
            mess.setHitterId(hitter != null ? hitter.getEntityId() : null);
            NETWORK.broadcast(mess);
        }
        entityService.hitAttribute(entity, attribute, value, hitter);
    }
    
    @Override
    public void hitNumberAttribute(Entity entity, String attribute, float addValue, Entity hitter) {
         if (NETWORK.isClient()) {
            return;
        }
        
        if (NETWORK.hasConnections()) {
            MessEntityHitNumberAttribute mess = new MessEntityHitNumberAttribute();
            mess.setEntityId(entity.getEntityId());
            mess.setAttribute(attribute);
            mess.setAddValue(addValue);
            mess.setHitterId(hitter != null ? hitter.getEntityId() : null);
            NETWORK.broadcast(mess);
        }
        entityService.hitNumberAttribute(entity, attribute, addValue, hitter);
    }

    @Override
    public void addData(Entity entity, ObjectData data, int amount) {
        if (NETWORK.isClient()) {
            return;
        }
        if (NETWORK.hasConnections()) {
            MessEntityAddData mess = new MessEntityAddData();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectData(data);
            mess.setAmount(amount);
            NETWORK.broadcast(mess);
        }
        entityService.addData(entity, data, amount);
    }

    @Override
    public void removeData(Entity entity, ObjectData data, int amount) {
        if (NETWORK.isClient()) {
            return;
        }
        if (NETWORK.hasConnections()) {
            MessEntityRemoveData mess = new MessEntityRemoveData();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectData(data);
            mess.setAmount(amount);
            NETWORK.broadcast(mess);
        }
        entityService.removeData(entity, data, amount);
    }

    @Override
    public void useData(Entity entity, ObjectData data) {
        if (NETWORK.isClient()) {
            return;
        }
        if (NETWORK.hasConnections()) {
            MessEntityUseData mess = new MessEntityUseData();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectData(data);
            NETWORK.broadcast(mess);
        }
        entityService.useData(entity, data);
    }

    
}
