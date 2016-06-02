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
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.object.action.Action;
//import name.huliqing.fighter.object.action.FollowAction;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// * 必须两个行为：FightAction, RunAction.
// * 角色逻辑：
// * 1.受到敌人攻击时会和敌人战斗
// * 2.没有敌人时就会去寻找目标角色,需要设置一个主要目标.这个目标可以是任何角色，包括敌人，友军等
// * @author huliqing
// */
//public class FollowLogic extends ActorLogic {
//    protected final ActorService actorService = Factory.get(ActorService.class);
//    protected final ActionService actionService = Factory.get(ActionService.class);
//    protected final PlayService playService = Factory.get(PlayService.class);
//    
//    private FollowAction followAction;
//    // 需要查找的目标角色
//    private Actor target;
//    // 走到多近距离
//    private float nearest = 5;
//    
//    public FollowLogic(LogicData data) {
//        super(data);
//        
//        // remove20160207
////        fightAction = (FightAction) Loader.loadAction(data.getProto().getAttribute("fightAction"));
////        runAction = (RunAction) Loader.loadAction(data.getProto().getAttribute("runAction"));
////        nearest = data.getProto().getAsFloat("nearest", nearest);
////        // 如果指定了目标角色，则从场景中查找该角色进行跟随,注：target有可能找不到。
////        String targetId = data.getProto().getAttribute("target");
////        if (targetId != null) {
////            PlayService playService = Factory.get(PlayService.class);
////            target = playService.findActor(targetId);
////        }
//        
//        followAction = (FollowAction) actionService.loadAction(data.getProto().getAttribute("followAction"));
//        nearest = data.getProto().getAsFloat("nearest", nearest);
//    }
//
//    @Override
//    protected void doLogic(float tpf) {
//        // 如果自在战斗,则不进行跟随.
//        if (actionService.isPlayingFight(self)) {
//            return;
//        }
//        
//        
//        long ft = self.getData().getFollowTarget(); 
//        
//        // 如果角色没有设置跟随的目标,则停止当前的跟随行为(注:只停止当前逻辑启动的followAction).
//        if (ft <= 0) {
//            Action current = actionService.getPlayingAction(self);
//            if (current == followAction) {
//                actionService.playAction(self, null);
//            }
//            return;
//        }
//        
//        // 如果跟随的目标发生变化则重新查找目标进行跟随.找不到指定目标则不处理
//        if (target == null || target.getData().getUniqueId() != ft) {
//            target = playService.findActor(ft);
//            if (target == null || target == self) {
//                return;
//            }
//        }
//        
//        followAction.setActor(self);
//        followAction.setFollow(target.getModel());
//        followAction.setNearest(nearest);
//        actionService.playAction(self, followAction);
//        
////        Actor current = actorService.getTarget(self);
////        if (current != null && !current.isDead() && current.isEnemy(self)) {
////            fightAction.setEnemy(current);
////            playAction(fightAction);
////        } else if (target != null) {
////            runAction.setNearest(nearest);
////            runAction.setPosition(target.getModel().getWorldTranslation());
////            if (!runAction.isEndPosition()) {
////                playAction(runAction);
////            }
////        }
//    }
//    
//}
