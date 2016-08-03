/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.mvc.network.StateNetwork;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.actor.Actor;

/**
 * 可让角色获得某些状态的handler
 * @author huliqing
 */
public class StateGainHandler extends AbstractHandler {
    private final StateService stateService = Factory.get(StateService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    private String[] states;

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
        states = data.getAsArray("states");
    }

    // remove20160521
//    @Override
//    public boolean canUse(Actor actor, ProtoData data) {
//        if (!super.canUse(actor, data)) {
//            return false;
//        }
//        // 不存在指定ID的状态
//        if (!stateService.existsState(stateId)) {
//            if (Config.debug) {
//                Logger.getLogger(StateGainHandler.class.getName()).log(Level.WARNING, "State not found: {0}", stateId);
//            }
//            return false;
//        }
//        return true;
//    }

    @Override
    protected void useObject(Actor actor, ProtoData data) {
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
