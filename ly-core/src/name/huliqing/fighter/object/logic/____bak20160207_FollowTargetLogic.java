///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.logic;
//
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.LogicData;
//import name.huliqing.fighter.game.network.ActorNetwork;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.action.FightAction;
//import name.huliqing.fighter.object.action.RunAction;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// * 必须两个行为：FightAction, RunAction.
// * 角色逻辑：
// * 1.受到敌人攻击时会和敌人战斗
// * 2.没有敌人时就会去寻找目标角色,需要设置一个主要目标.这个目标可以是任何角色，包括敌人，友军等
// * @author huliqing
// */
//public class FollowTargetLogic extends ActorLogic {
//    protected final ActorService actorService = Factory.get(ActorService.class);
//    private FightAction fightAction;
//    private RunAction runAction;
//    // 需要查找的目标角色
//    private Actor target;
//    // 走到多近距离
//    private float nearest = 5;
//    
//    public FollowTargetLogic() {}
//
//    public FollowTargetLogic(LogicData data) {
//        super(data);
//        fightAction = (FightAction) Loader.loadAction(data.getProto().getAttribute("fightAction"));
//        runAction = (RunAction) Loader.loadAction(data.getProto().getAttribute("runAction"));
//        nearest = data.getProto().getAsFloat("nearest", nearest);
//        // 如果指定了目标角色，则从场景中查找该角色进行跟随,注：target有可能找不到。
//        String targetId = data.getProto().getAttribute("target");
//        if (targetId != null) {
//            PlayService playService = Factory.get(PlayService.class);
//            target = playService.findActor(targetId);
//        }
//    }
//
//    public FightAction getFightAction() {
//        return fightAction;
//    }
//
//    public void setFightAction(FightAction fightAction) {
//        this.fightAction = fightAction;
//    }
//
//    public RunAction getRunAction() {
//        return runAction;
//    }
//
//    public void setRunAction(RunAction runAction) {
//        this.runAction = runAction;
//    }
//
//    public Actor getTarget() {
//        return target;
//    }
//
//    /**
//     * 设置一个要跟随的目标
//     * @param target
//     * @return 
//     */
//    public void setTarget(Actor target) {
//        this.target = target;
//    }
//
//    public float getNearest() {
//        return nearest;
//    }
//
//    /**
//     * 设置跟随的最近距离
//     * @param nearest 
//     */
//    public void setNearest(float nearest) {
//        this.nearest = nearest;
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//        Actor current = actorService.getTarget(self);
//        if (current != null && !current.isDead() && current.isEnemy(self)) {
//            fightAction.setEnemy(current);
//            playAction(fightAction);
//        } else if (target != null) {
//            runAction.setNearest(nearest);
//            runAction.setPosition(target.getModel().getWorldTranslation());
//            if (!runAction.isEndPosition()) {
//                playAction(runAction);
//            }
//        }
//    }
//    
//}
