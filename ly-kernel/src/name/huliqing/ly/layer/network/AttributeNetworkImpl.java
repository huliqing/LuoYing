/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.mess.MessAttributeNumberAddValue;
import name.huliqing.ly.layer.service.AttributeService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class AttributeNetworkImpl implements AttributeNetwork {
    private final static Network NETWORK = Network.getInstance();
    private AttributeService attributeService;
    
    @Override
    public void inject() {
        attributeService = Factory.get(AttributeService.class);
    }

    @Override
    public void addNumberAttributeValue(Entity actor, String attrName, float value) {
        if (NETWORK.isClient()) {
            return;
        }
        
        if (NETWORK.hasConnections()) {
            MessAttributeNumberAddValue mess = new MessAttributeNumberAddValue();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setAttributeName(attrName);
            mess.setValue(value);
            NETWORK.broadcast(mess);
        }
        
        attributeService.addNumberAttributeValue(actor, attrName, value);
    }

}
