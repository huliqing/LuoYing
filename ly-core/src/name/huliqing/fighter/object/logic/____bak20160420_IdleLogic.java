///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.logic;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.LogicData;
//import name.huliqing.fighter.game.service.ActionService;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.object.action.Action;
//
///**
// *
// * @author huliqing
// */
//public class IdleLogic extends ActorLogic {
//    
//    private final ActionService actionService = Factory.get(ActionService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    
//    private final Action idleAction;
//
//    public IdleLogic(LogicData data) {
//        super(data);
//        this.idleAction = actionService.loadAction(data.getAttribute("idleAction"));
//    }
//
//    @Override
//    protected void doLogic(float tpf) {
//        // 只有空闲时才执行IDLE
//        Action current = actionService.getPlayingAction(self);
//        if (current == null && self.getData().getFollowTarget() <= 0) {
//            playAction(idleAction);
//        }
//    }
//    
//}
