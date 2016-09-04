/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.ItemService;

/**
 * 补血，补蓝
 * @author huliqing
 */
public class AttributeHandler extends AbstractItemHandler {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final ItemService itemService = Factory.get(ItemService.class);

    // 指定要补充哪个属性id
    private String attribute;
    
    // 补充的量可正，可负
    private float amount; 

    @Override
    public void setData(HandlerData data) {
        super.setData(data); 
        this.attribute = data.getAsString("attribute");
        this.amount = data.getAsFloat("amount", 0);
    }

    @Override
    protected void useObject(Actor actor, ObjectData data) {
        // 补充属性值
        attributeService.addNumberAttributeValue(actor, attribute, amount);
        
        // 物品减少
        itemService.removeItem(actor, data.getId(), 1);
    }
    
}
