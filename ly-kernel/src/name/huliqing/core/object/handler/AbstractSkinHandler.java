/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeMatch;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.MatchAttribute;

/**
 *
 * @author huliqing
 */
public abstract class AbstractSkinHandler extends AbstractHandler {
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        if (!(data instanceof SkinData)) {
            throw new IllegalArgumentException("AbstractItemHandler only supported ItemData object, dataId=" 
                    + data.getId() + ", handlerId=" + this.data.getId());
        }
        // 检查属性限制
        return canUseItem(actor , (SkinData) data);
    }
    
    private boolean canUseItem(Actor actor, SkinData data) {
        // attributeMatchs == null 说明没有任何限制
        if (data.getMatchAttributes() == null)
            return true;
        
        // 如果角色的属性中有一个不能和AttributeMatchs中要求的不匹配则视为不能使用。
        Attribute attr;
        for (AttributeMatch am : data.getMatchAttributes()) {
            attr = attributeService.getAttributeByName(actor, am.getAttributeName());
            if (!(attr instanceof MatchAttribute)) {
                return false;
            }
            if (!((MatchAttribute)attr).match(am.getValue())) {
                return false;
            }
        }
        return true;
    }
    
}
