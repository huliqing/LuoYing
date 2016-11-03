/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.mess.MessEntityAttributeApply;
import name.huliqing.luoying.mess.MessEntityAttributeSet;
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
    public void setAttribute(Entity entity, String attributeName, Object value) {
        MessEntityAttributeSet mess = new MessEntityAttributeSet();
        mess.setAttributeName(attributeName);
        mess.setEntityId(entity.getEntityId());
        mess.setValue(value);
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            entityService.setAttribute(entity, attributeName, value);
        }
    }
    
    @Override
    public void applyNumberAttributeValue(Entity entity, String attributeName, float value, Entity source) {
         if (NETWORK.isClient()) {
            return;
        }
        
        if (NETWORK.hasConnections()) {
            MessEntityAttributeApply mess = new MessEntityAttributeApply();
            mess.setTarget(entity.getEntityId());
            mess.setAttributeName(attributeName);
            mess.setApplyValue(value);
            if (source != null) {
                mess.setSource(source.getEntityId());
            }
            NETWORK.broadcast(mess);
        }
        
        entityService.applyNumberAttributeValue(entity, attributeName, value, source);
    }

    
}
