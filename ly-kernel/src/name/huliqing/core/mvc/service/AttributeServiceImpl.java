/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import java.util.Map;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.actor.Actor;
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
        return getAttribute(actor, attributeId) != null;
    }

    @Override
    public float getDynamicValue(Actor actor, String attributeId) {
        AttributeData data = getAttribute(actor, attributeId);
        if (data != null) {
            return data.getDynamicValue();
        }
        return 0;
    }

    @Override
    public float getMaxValue(Actor actor, String attributeId) {
        AttributeData data = getAttribute(actor, attributeId);
        if (data != null) {
            return data.getMaxValue();
        }
        return 0;
    }

    @Override
    public void applyDynamicValue(Actor actor, String attributeId, float amount) {
        AttributeData data = getAttribute(actor, attributeId);
        if (data != null) {
            data.setDynamicValue(data.getDynamicValue() + amount);
        }
    }

    @Override
    public void clampDynamicValue(Actor actor, String attributeId) {
        AttributeData data = getAttribute(actor, attributeId);
        if (data != null) {
            float dValue = data.getDynamicValue();
            data.setDynamicValue(MathUtils.clamp(dValue, 0, data.getMaxValue()));
        }
    }

    @Override
    public void applyStaticValue(Actor actor, String attributeId, float amount) {
        AttributeData data = getAttribute(actor, attributeId);
        if (data != null) {
            data.setStaticValue(data.getStaticValue() + amount);
        } 
    }

    @Override
    public AttributeData getAttributeData(Actor actor, String attribute) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttribute(attribute);
        }
        return null;
    }

    @Override
    public void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue) {
        AttributeData data = getAttribute(actor, attributeId);
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

    private AttributeData getAttribute(Actor actor, String attributeId) {
        AttributeModule module = actor.getModule(AttributeModule.class);
        if (module != null) {
            return module.getAttribute(attributeId);
        }
        return null;
    }
}
