/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import java.util.logging.Logger;
import name.huliqing.ly.data.AttributeData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.attribute.AbstractSimpleAttribute;
import name.huliqing.ly.object.attribute.Attribute;
import name.huliqing.ly.object.module.AttributeListener;
import name.huliqing.ly.object.module.AttributeModule;

/**
 *
 * @author huliqing
 */
public class AttributeServiceImpl implements AttributeService {
    private static final Logger LOG = Logger.getLogger(AttributeServiceImpl.class.getName());

    @Override
    public void inject() {
        // 
    }

    @Override
    public Attribute loadAttribute(String attributeId) {
        return Loader.load(attributeId);
    }

    @Override
    public Attribute loadAttribute(AttributeData data) {
        return Loader.load(data);
    }

    @Override
    public void addAttribute(Actor actor, Attribute attribute) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            module.addAttribute(attribute);
        }
    }

    @Override
    public Attribute getAttributeById(Actor actor, String attrId) {
        if (attrId == null)
            throw new NullPointerException("attrId could not be null, actorId=" + actor.getData().getId());
                
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttributeById(attrId);
        }
        return null;
    }

    @Override
    public Attribute getAttributeByName(Actor actor, String attrName) {
        if (attrName == null) {
            return null;
        }
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttributeByName(attrName, null);
        }
        return null;
    }

    @Override
    public List<Attribute> getAttributes(Actor actor) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttributes();
        }
        return null;
    }

    @Override
    public void addListener(Actor actor, AttributeListener attributeListener) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            module.addListener(attributeListener);
        }
    }

    @Override
    public boolean removeListener(Actor actor, AttributeListener attributeListener) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        return module != null && module.removeListener(attributeListener);
    }

    @Override
    public <V> void setSimpleAttributeValue(Actor actor, String attrName, V value) {
        Attribute attr = getAttributeByName(actor, attrName);
        if (attr instanceof AbstractSimpleAttribute) {
            ((AbstractSimpleAttribute) attr).setValue(value);
        }
    }

    @Override
    public void addNumberAttributeValue(Actor actor, String attrName, float value) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            module.addNumberAttributeValue(attrName, value);
        }
    }

    @Override
    public float getNumberAttributeValue(Actor actor, String attrName, float defValue) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getNumberAttributeValue(attrName, defValue);
        }
        return defValue;
    }

    
}
