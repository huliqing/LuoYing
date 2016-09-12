/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.shortcut;

import name.huliqing.core.Factory;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ItemListener;

/**
 * 用于普通物品(Item)的快捷方式
 * @author huliqing
 */
public class ItemShortcut extends BaseUIShortcut<ItemData> implements ItemListener {
    private final ItemService itemService = Factory.get(ItemService.class);
        
    @Override
    public void initialize() {
        super.initialize();
        itemService.addItemListener(actor, this);
    }

    @Override
    public void cleanup() {
        itemService.removeItemListener(actor, this);
        super.cleanup(); 
    }
    
    @Override
    public void onItemAdded(Actor actor, ItemData item, int trueAdded) {
        if (!item.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(item);
    }

    @Override
    public void onItemRemoved(Actor actor, ItemData item, int trueRemoved) {
        if (!item.getId().equals(objectData.getId()))
            return;
        
        updateObjectData(item);
    }
    
    
}
