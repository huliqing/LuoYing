/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.EntityService;

/**
 * 改变角色属性的逻辑,一般可用来恢复角色的生命，魔法，能量之类的
 * @author huliqing
 * @param <T>
 */
public class AttributeChangeLogic<T extends LogicData> extends Logic<T> {
    private final EntityService entityService = Factory.get(EntityService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
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
//        float useFactor = actor.getAttributeManager().getNumberAttributeValue(useAttribute, 0);
        float useFactor = entityService.getNumberAttributeValue(actor, useAttribute, 0);
        float applyValue = value * useFactor * interval;
        
        if (applyValue > 0.0001f) {
            entityNetwork.applyNumberAttributeValue(actor, targetAttribute, applyValue, null);
        }
    }
}
