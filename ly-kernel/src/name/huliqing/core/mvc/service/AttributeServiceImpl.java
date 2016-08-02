/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.actor.Actor;
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
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return false;
        return attrMap.containsKey(attributeId);
    }

    @Override
    public float getDynamicValue(Actor actor, String attributeId) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return 0;
        AttributeData data = attrMap.get(attributeId);
        if (data == null) {
            return 0;
        }
        return data.getDynamicValue();
    }

    @Override
    public float getMaxValue(Actor actor, String attributeId) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return 0;
        AttributeData data = attrMap.get(attributeId);
        if (data == null)
            return 0;
        return data.getMaxValue();
    }

    @Override
    public void applyDynamicValue(Actor actor, String attributeId, float amount) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null) 
            return;
        AttributeData data = attrMap.get(attributeId);
        if (data == null)
            return;
        data.setDynamicValue(data.getDynamicValue() + amount);
    }

    @Override
    public void clampDynamicValue(Actor actor, String attributeId) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null) 
            return;
        AttributeData data = attrMap.get(attributeId);
        if (data == null)
            return;
        float dValue = data.getDynamicValue();
        data.setDynamicValue(MathUtils.clamp(dValue, 0, data.getMaxValue()));
    }

    @Override
    public void applyStaticValue(Actor actor, String attributeId, float amount) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return;
        AttributeData data = attrMap.get(attributeId);
        if (data == null)
            return;
        data.setStaticValue(data.getStaticValue() + amount);
    }

    @Override
    public AttributeData getAttributeData(Actor actor, String attribute) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return null;
        return attrMap.get(attribute);
    }

    @Override
    public void syncAttribute(Actor actor, String attributeId, float levelValue, float staticValue, float dynamicValue) {
        Map<String, AttributeData> attrMap = actor.getData().getAttributes();
        if (attrMap == null)
            return;
        AttributeData ad = attrMap.get(attributeId);
        if (ad == null)
            return;
        ad.setLevelValue(levelValue);
        ad.setStaticValue(staticValue);
        ad.setDynamicValue(dynamicValue);
    }

    
}
