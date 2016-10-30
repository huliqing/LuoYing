/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.network.Network;

/**
 *
 * @author huliqing
 */
public class AttributeNetworkImpl implements AttributeNetwork {
    private final static Network NETWORK = Network.getInstance();
    
    @Override
    public void inject() {
    }

    // remove20161030
//    @Override
//    public void addNumberAttributeValue(Entity actor, String attrName, float value) {
//        if (NETWORK.isClient()) {
//            return;
//        }
//        
//        if (NETWORK.hasConnections()) {
//            MessEntityAttributeAddValue mess = new MessEntityAttributeAddValue();
//            mess.setEntityId(actor.getData().getUniqueId());
//            mess.setAttributeName(attrName);
//            mess.setValue(value);
//            NETWORK.broadcast(mess);
//        }
//        
////        attributeService.addNumberAttributeValue(actor, attrName, value);
//        throw new UnsupportedOperationException();
//    }

}
