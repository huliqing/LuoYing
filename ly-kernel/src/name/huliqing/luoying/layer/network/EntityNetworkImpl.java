/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.mess.MessEntityHitNumberAttribute;
import name.huliqing.luoying.mess.MessEntityHitAttribute;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;

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
            mess.setHitterId(hitter != null ? hitter.getEntityId() : null);
            mess.setEntityId(entity.getEntityId());
            mess.setAttribute(attribute);
            mess.setValue(value);
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
            mess.setHitterId(hitter != null ? hitter.getEntityId() : null);
            mess.setTargetId(entity.getEntityId());
            mess.setAttribute(attribute);
            mess.setAddValue(addValue);
            NETWORK.broadcast(mess);
        }
        entityService.hitNumberAttribute(entity, attribute, addValue, hitter);
    }

    
}
