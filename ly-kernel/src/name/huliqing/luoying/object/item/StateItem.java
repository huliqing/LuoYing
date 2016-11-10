/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.layer.network.StateNetwork;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可让角色获得某些状态的物品
 * @author huliqing
 */
public class StateItem extends AbstractItem {
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    
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
        
        // 因为添加状态涉及到概率，所以需要使用network方式
        for (String sid : states) {
            stateNetwork.addState(actor, sid, null);
        }
        // 物品减少
//        itemService.removeItem(actor, data.getId(), 1);

        actor.removeObjectData(data, 1);        
    }
    
}
