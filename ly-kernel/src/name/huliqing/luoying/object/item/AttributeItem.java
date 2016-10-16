/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.service.AttributeService;
import name.huliqing.luoying.layer.service.ItemService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 消耗品，用于为指定角色补充属性值的物品
 * @author huliqing
 */
public class AttributeItem extends AbstractItem {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final ItemService itemService = Factory.get(ItemService.class);

    // 指定要补充哪个属性
    private String attribute;
    
    // 补充的量可正，可负
    private float amount;

    @Override
    public void setData(ItemData data) {
        super.setData(data); 
        this.attribute = data.getAsString("attribute");
        this.amount = data.getAsFloat("amount", 0);
    }
    
    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        // 补充属性值
        attributeService.addNumberAttributeValue(actor, attribute, amount);
        
        // 物品减少
        itemService.removeItem(actor, data.getId(), 1);
    }
    
}
