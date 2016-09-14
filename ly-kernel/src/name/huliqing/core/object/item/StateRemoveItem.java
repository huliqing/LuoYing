/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.Factory;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.actor.Actor;

/**
 * 可用于删除角色身上某些状态的物品
 * @author huliqing
 */
public class StateRemoveItem extends AbstractItem {
    private final StateService stateService = Factory.get(StateService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    private String[] states;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        states = data.getAsArray("states");
    }

    @Override
    public void use(Actor actor) {
        super.use(actor);
        
        if (states == null) 
            return;
        
        for (String sid : states) {
            stateService.removeState(actor, sid);
        }
        // 物品减少
        itemService.removeItem(actor, data.getId(), 1);        
    }
    
}
