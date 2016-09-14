///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.handler;
//
//import name.huliqing.core.Factory;
//import name.huliqing.core.data.HandlerData;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.mvc.service.ItemService;
//import name.huliqing.core.mvc.service.StateService;
//import name.huliqing.core.object.actor.Actor;
//
///**
// * 可用于删除角色身上某些状态的Handler
// * @author huliqing
// */
//public class StateRemoveHandler extends AbstractItemHandler {
//    private final StateService stateService = Factory.get(StateService.class);
//    private final ItemService itemService = Factory.get(ItemService.class);
//
//    private String[] states;
//    
//    @Override
//    public void setData(HandlerData data) {
//        super.setData(data);
//        states = data.getAsArray("states");
//    }
//    
//    @Override
//    protected void useObject(Actor actor, ObjectData data) {
//        if (states == null) 
//            return;
//        
//        for (String sid : states) {
//            stateService.removeState(actor, sid);
//        }
//        // 物品减少
//        itemService.removeItem(actor, data.getId(), 1);
//    }
//    
//}
