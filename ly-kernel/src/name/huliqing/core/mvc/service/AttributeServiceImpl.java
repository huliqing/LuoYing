/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.AbstractSimpleAttribute;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.NumberAttribute;
import name.huliqing.core.object.module.AttributeListener;
import name.huliqing.core.object.module.AttributeModule;

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
            return module.getAttributeByName(attrName);
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
        Attribute attr = getAttributeByName(actor, attrName);
        if (attr instanceof NumberAttribute) {
            ((NumberAttribute)attr).add(value);
        } else {
            LOG.log(Level.WARNING, "Could not addNumberAttributeValue, attrName is not a NumberAttribute,"
                    + " actorId={0}, attrName={1}, value={2}", new Object[] {actor.getData().getId(), attrName, value});
            throw new RuntimeException("Could not addNumberAttributeValue, attrName is not a NumberAttribute");
        }
    }

    @Override
    public float getNumberAttributeValue(Actor actor, String attrName, float defValue) {
        Attribute attr = getAttributeByName(actor, attrName);
        if (attr instanceof NumberAttribute) {
            return ((NumberAttribute) attr).floatValue();
        }
        return defValue;
    }

    
}
