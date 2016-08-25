/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.module.AttributeModule;
import name.huliqing.core.utils.MathUtils;

/**
 *
 * @author huliqing
 */
public class AttributeServiceImpl implements AttributeService {

    @Override
    public void inject() {
        // 
    }

    @Override
    public boolean existsAttribute(Actor actor, String attributeId) {
        return getAttributeById(actor, attributeId) != null;
    }

    @Override
    public float getDynamicValue(Actor actor, String attributeId) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            return data.getDynamicValue();
        }
        return 0;
    }

    @Override
    public float getMaxValue(Actor actor, String attributeId) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            return data.getMaxValue();
        }
        return 0;
    }

    @Override
    public void applyDynamicValue(Actor actor, String attributeId, float amount) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            data.setDynamicValue(data.getDynamicValue() + amount);
        }
    }

    @Override
    public void clampDynamicValue(Actor actor, String attributeId) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            float dValue = data.getDynamicValue();
            data.setDynamicValue(MathUtils.clamp(dValue, 0, data.getMaxValue()));
        }
    }

    @Override
    public void applyStaticValue(Actor actor, String attributeId, float amount) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            data.setStaticValue(data.getStaticValue() + amount);
        } 
    }

    @Override
    public Attribute getAttributeById(Actor actor, String attrId) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttributeById(attrId);
        }
        return null;
    }

    @Override
    public void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue) {
        AttributeData data = getAttributeById(actor, attributeId).getData();
        if (data != null) {
            data.setLevelValue(levelValue);
            data.setStaticValue(staticValue);
            data.setDynamicValue(dynamicValue);
        }
    }

    @Override
    public List<AttributeData> getAttributes(Actor actor) {
//        AttributeModule module = actor.getModule(AttributeModule.class);
//        if (module != null) {
//            module.getAttributes();
//        }
//        return null;

        return actor.getData().getObjectDatas(AttributeData.class, null);
    }

}
