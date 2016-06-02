/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 可用于删除角色身上某些状态的Handler
 * @author huliqing
 */
public class StateRemoveHandler extends AbstractHandler {
    private final StateService stateService = Factory.get(StateService.class);
    private final ItemService itemService = Factory.get(ItemService.class);

    private String[] states;
    
    @Override
    public void initData(HandlerData data) {
        super.initData(data);
        states = data.getAsArray("states");
    }
    
    @Override
    protected void useObject(Actor actor, ProtoData data) {
        if (states == null) 
            return;
        
        for (String sid : states) {
            stateService.removeState(actor, sid);
        }
        // 物品减少
        itemService.removeItem(actor, data.getId(), 1);
    }
    
}
