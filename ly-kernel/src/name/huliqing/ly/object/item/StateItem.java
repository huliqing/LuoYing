/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.item;

import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ItemData;
import name.huliqing.ly.layer.network.StateNetwork;
import name.huliqing.ly.layer.service.ItemService;
import name.huliqing.ly.object.actor.Actor;

/**
 * 可让角色获得某些状态的物品
 * @author huliqing
 */
public class StateItem extends AbstractItem {
    
//    private final StateService stateService = Factory.get(StateService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
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
        
        // 因为添加状态涉及到概率，所以需要使用network方式
        for (String sid : states) {
            stateNetwork.addState(actor, sid, null);
        }
        // 物品减少
        itemService.removeItem(actor, data.getId(), 1);
    }
    
}
