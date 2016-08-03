/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.ItemService;

/**
 * 补血，补蓝
 * @author huliqing
 */
public class AttributeHandler extends AbstractHandler {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final ItemService itemService = Factory.get(ItemService.class);

    // 指定要补充哪个属性id
    private String attribute;
    
    // 补充的量可正，可负
    private float amount; 

    @Override
    public void setData(HandlerData data) {
        super.setData(data); 
        this.attribute = data.getAttribute("attribute");
        this.amount = data.getAsFloat("amount", 0);
    }

    @Override
    protected void useObject(Actor actor, ProtoData data) {
        // 补充属性值
        attributeService.applyDynamicValue(actor, attribute, amount);
        attributeService.clampDynamicValue(actor, attribute);
        
        // 物品减少
//        remove(actor, data.getId(), 1); // remove
        itemService.removeItem(actor, data.getId(), 1);
    }
    
}
