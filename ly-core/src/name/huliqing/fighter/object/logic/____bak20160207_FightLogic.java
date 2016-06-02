///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.logic;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.object.action.FightAction;
//import name.huliqing.fighter.object.action.IdleAction;
//import name.huliqing.fighter.data.LogicData;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.loader.Loader;
//
///**
// * 战斗逻辑,该类逻辑： 
// * 1.受到敌人攻击时会和敌人战斗(不会主动查找敌人)
// * 2.没有敌人时转入idle状态。
// * 触发战斗行为需要受到攻击或者有搜索敌人的相关逻辑配合
// * @author huliqing
// */
//public class FightLogic extends ActorLogic {
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    protected FightAction fightAction;
//    protected IdleAction idleAction;
//    
//    public FightLogic() {}
//    
//    public FightLogic(LogicData logicData) {
//        super(logicData);
//        fightAction = (FightAction) Loader.loadAction(logicData.getProto().getAttribute("fightAction"));
//        String idleActionId = logicData.getProto().getAttribute("idleAction");
//        if (idleActionId != null) {
//            idleAction = (IdleAction) Loader.loadAction(idleActionId);
//        }
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//        Actor t = actorService.getTarget(self);
//        if (t != null && !t.isDead() && t.isEnemy(self) && playService.isInScene(t)) {
//            fightAction.setEnemy(t);
//            playAction(fightAction);
//            return;
//        }
//        
//        if (idleAction != null) {
//            playAction(idleAction);
//        }
//    }
//    
//}
