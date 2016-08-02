/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.network;

import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.game.service.AttributeService;
import name.huliqing.core.network.Network;
import name.huliqing.core.game.mess.MessAttributeSync;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class AttributeNetworkImpl implements AttributeNetwork {
    
    private final static Network network = Network.getInstance();
    private AttributeService attributeService;
    
    @Override
    public void inject() {
        attributeService = Factory.get(AttributeService.class);
    }
    
    @Override
    public boolean existsAttribute(Actor actor, String attributeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getDynamicValue(Actor actor, String attributeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getMaxValue(Actor actor, String attributeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void applyDynamicValue(Actor actor, String attributeId, float amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clampDynamicValue(Actor actor, String attributeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void applyStaticValue(Actor actor, String attributeId, float amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AttributeData getAttributeData(Actor actor, String attribute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue) {
        if (!network.isClient()) {
            
            attributeService.syncAttribute(actor, attributeId, levelValue, staticValue, dynamicValue);
            
            // 同步到客户端
            syncAttribute(actor, attributeId);
        }
    }

    @Override
    public void syncAttribute(Actor actor, String attributeId) {
        if (!network.isClient()) {
            AttributeData aData = attributeService.getAttributeData(actor, attributeId);
            if (network.hasConnections()) {
                MessAttributeSync mess = new MessAttributeSync();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setAttribute(attributeId);
                mess.setLevelValue(aData.getLevelValue());
                mess.setStaticValue(aData.getStaticValue());
                mess.setDynamicValue(aData.getDynamicValue());
                network.broadcast(mess);
            }
        }
    }

    
}
