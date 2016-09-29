/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.mess.MessAttributeNumberAddValue;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.network.Network;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.module.AttributeListener;

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
    public void addNumberAttributeValue(Actor actor, String attrName, float value) {
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
