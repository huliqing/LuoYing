/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.logic;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.game.network.AttributeNetwork;
import name.huliqing.fighter.game.service.AttributeService;

/**
 * 改变角色属性的逻辑,一般可用来恢复角色的生命，魔法，能量之类的
 * @author huliqing
 * @param <T>
 */
public class AttributeChangeLogic<T extends LogicData> extends ActorLogic<T> {
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
        this.targetAttribute = data.getAttribute("targetAttribute");
        this.useAttribute = data.getAttribute("useAttribute");
    }

    @Override
    protected void doLogic(float tpf) {
        AttributeData data = attributeService.getAttributeData(self, targetAttribute);
        if (data == null) {
            return;
        }
        
        float useFactor = attributeService.getDynamicValue(self, useAttribute);
        float applyValue = value * useFactor * interval;
        data.setDynamicValue(data.getDynamicValue() + applyValue);
        attributeService.clampDynamicValue(self, data.getId());
        attributeNetwork.syncAttribute(self, data.getId()
                , data.getLevelValue(), data.getStaticValue(), data.getDynamicValue());
    }
}
