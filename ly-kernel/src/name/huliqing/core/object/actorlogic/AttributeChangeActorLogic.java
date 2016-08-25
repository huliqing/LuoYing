/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.mvc.network.AttributeNetwork;
import name.huliqing.core.mvc.service.AttributeService;

/**
 * 改变角色属性的逻辑,一般可用来恢复角色的生命，魔法，能量之类的
 * @author huliqing
 * @param <T>
 */
public class AttributeChangeActorLogic<T extends ActorLogicData> extends ActorLogic<T> {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final AttributeNetwork attributeNetwork = Factory.get(AttributeNetwork.class);
    private float value = 1f;
    // 影响的目标属性的id
    private String targetAttribute;
    // 作为影响因素的目标属性的id
    private String useAttribute;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.value = data.getAsFloat("value");
        this.targetAttribute = data.getAsString("targetAttribute");
        this.useAttribute = data.getAsString("useAttribute");
    }

    @Override
    protected void doLogic(float tpf) {
        AttributeData data = attributeService.getAttributeById(actor, targetAttribute).getData();
        if (data == null) {
            return;
        }
        
        float useFactor = attributeService.getDynamicValue(actor, useAttribute);
        float applyValue = value * useFactor * interval;
        
        data.setDynamicValue(data.getDynamicValue() + applyValue);
        attributeService.clampDynamicValue(actor, data.getId());
        attributeNetwork.syncAttribute(actor, data.getId()
                , data.getLevelValue(), data.getStaticValue(), data.getDynamicValue());
    }
}
