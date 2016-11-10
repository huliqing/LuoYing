/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.service.StateService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可用于删除角色身上某些状态的物品
 * @author huliqing
 */
public class StateRemoveItem extends AbstractItem {
    private final StateService stateService = Factory.get(StateService.class);
    
    private String[] states;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        states = data.getAsArray("states");
    }

    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        if (states == null) 
            return;
        
        for (String sid : states) {
            stateService.removeState(actor, sid);
        }
        
        // 物品减少
        actor.removeObjectData(data, 1);    
    }
    
}
