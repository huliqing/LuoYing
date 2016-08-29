/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.network.Network;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.AttributeStore;
import name.huliqing.core.object.module.AttributeListener;

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
    
    // remove20160826
//    @Override
//    public boolean existsAttribute(Actor actor, String attributeId) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public float getDynamicValue(Actor actor, String attributeId) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public float getMaxValue(Actor actor, String attributeId) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void applyDynamicValue(Actor actor, String attributeId, float amount) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void clampDynamicValue(Actor actor, String attributeId) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void applyStaticValue(Actor actor, String attributeId, float amount) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Attribute getAttributeById(Actor actor, String attribute) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue) {
//        if (!network.isClient()) {
//            
//            attributeService.syncAttribute(actor, attributeId, levelValue, staticValue, dynamicValue);
//            
//            // 同步到客户端
//            syncAttribute(actor, attributeId);
//        }
//    }
//
//    @Override
//    public void syncAttribute(Actor actor, String attributeId) {
//        if (!network.isClient()) {
//            AttributeData aData = attributeService.getAttributeById(actor, attributeId).getData();
//            if (network.hasConnections()) {
//                MessAttributeSync mess = new MessAttributeSync();
//                mess.setActorId(actor.getData().getUniqueId());
//                mess.setAttribute(attributeId);
//                mess.setLevelValue(aData.getLevelValue());
//                mess.setStaticValue(aData.getStaticValue());
//                mess.setDynamicValue(aData.getDynamicValue());
//                network.broadcast(mess);
//            }
//        }
//    }
//
//    @Override
//    public List<AttributeData> getAttributes(Actor actor) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public Attribute loadAttribute(String attributeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Attribute loadAttribute(AttributeData data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addAttribute(Actor actor, Attribute attribute) throws AttributeStore.AttributeConflictException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Attribute getAttributeById(Actor actor, String attrId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Attribute getAttributeByName(Actor actor, String attrName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Attribute> getAttributes(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListener(Actor actor, AttributeListener attributeListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeListener(Actor actor, AttributeListener attributeListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <V> void setAttributeValue(Actor actor, String attrName, V value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addAttributeValue(Actor actor, String attrName, float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getNumberAttributeValue(Actor actor, String attrName, float defValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
