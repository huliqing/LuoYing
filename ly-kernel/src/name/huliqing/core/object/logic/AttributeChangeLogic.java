/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.logic;

import name.huliqing.core.Factory;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.data.LogicData;
import name.huliqing.core.mvc.network.AttributeNetwork;
import name.huliqing.core.mvc.service.AttributeService;

/**
 * 改变角色属性的逻辑,一般可用来恢复角色的生命，魔法，能量之类的
 * @author huliqing
 * @param <T>
 */
public class AttributeChangeLogic<T extends LogicData> extends Logic<T> {
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
        // remove20160827
//        AttributeData data = attributeService.getAttributeById(actor, targetAttribute).getData();
//        if (data == null) {
//            return;
//        }

        // remove20160827
//        float useFactor = attributeService.getDynamicValue(actor, useAttribute);

        float useFactor = attributeService.getNumberAttributeValue(actor, useAttribute, 0);
        float applyValue = value * useFactor * interval;
        
        // remove20160827
//        data.setDynamicValue(data.getDynamicValue() + applyValue);
//        attributeService.clampDynamicValue(actor, data.getId());
//        attributeNetwork.syncAttribute(actor, data.getId()
//                , data.getLevelValue(), data.getStaticValue(), data.getDynamicValue());
        
        if (applyValue > 0.0001f) {
            attributeNetwork.addNumberAttributeValue(actor, targetAttribute, applyValue);
        }
    }
}
