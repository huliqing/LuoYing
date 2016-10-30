/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class EntityServiceImpl implements EntityService {
    
    @Override
    public void inject() {
    }

    @Override
    public float getNumberAttributeValue(Entity entity, String attributeName, float defValue) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
        if (nattr != null) {
            return nattr.getValue().floatValue();
        }
        return defValue;
    }

    @Override
    public void applyNumberAttributeValue(Entity entity, String attributeName, float value, Entity source) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
        if (nattr != null) {
            nattr.setValue(value);
        }
    }

    
}
