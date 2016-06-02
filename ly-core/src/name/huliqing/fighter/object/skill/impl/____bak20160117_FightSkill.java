///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.skill.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.constants.IdConstants;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.game.network.ActorNetwork;
//import name.huliqing.fighter.game.network.PlayNetwork;
//import name.huliqing.fighter.game.network.SkillNetwork;
//import name.huliqing.fighter.game.network.StateNetwork;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.DropService;
//import name.huliqing.fighter.game.service.ElService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.object.hitchecker.HitChecker;
//import name.huliqing.fighter.object.skill.AbstractSkill;
//import name.huliqing.fighter.object.skill.FightUtils;
//import name.huliqing.fighter.object.skill.SkillStateWrap;
//import name.huliqing.fighter.utils.ConvertUtils;
//
///**
// * 战斗类技能
// * @author huliqing
// */
//public abstract class FightSkill extends AbstractSkill {
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);;
//    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
//    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final StateService stateService = Factory.get(StateService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final DropService dropService = Factory.get(DropService.class);
//    private final ElService elService = Factory.get(ElService.class);
//    
//    // 技能的伤害值
//    protected int damage;
//    // 伤害值计算公式
//    protected String damageEl;
//    
//    // 是否允许攻击到多个敌人，默认情况下只能攻击到当前的目标敌人。
//    // 如果打开该功能，则在攻击范围内的敌人都可能被攻击到。比如挥剑
//    // 的时候可能同时击中多个敌人。默认值false
//    protected boolean multHit;
//    
//    // remove20160117暂不支持这个参数
////    // 物理作用力,这个力可以用于造成目标飞出去的效果
////    protected float force;
//    
//    // 技能作用检查器
//    protected HitChecker hitChecker;
//    // 可用于添加到目标射上时的列表
//    protected List<SkillStateWrap> stateWraps;
//    
//    public FightSkill(SkillData data) {
//        super(data);
//        damage = data.getProto().getAsInteger("damage", damage);
//        damageEl = data.getProto().getAttribute("damageEl");
//        multHit = data.getProto().getAsBoolean("multHit", multHit);
//        
////        force = data.getProto().getAsFloat("force", force);
//        
//        hitChecker = Loader.loadHitChecker(data.getProto().getAttribute("hitChecker", IdConstants.HIT_CHECKER_FIGHT_DEFAULT));
//        
//        // 状态和机率，　格式："stateId1|factor, stateId2|factor"
//        String[] tempStates = data.getProto().getAsArray("states");
//        if (tempStates != null && tempStates.length > 0) {
//            stateWraps = new ArrayList<SkillStateWrap>(tempStates.length);
//            String[] tempArr;
//            for (String ts : tempStates) {
//                if (ts.trim().equals("")) {
//                    continue;
//                }
//                tempArr = ts.split("\\|");
//                SkillStateWrap sw = new SkillStateWrap();
//                sw.stateId = tempArr[0];
//                if (tempArr.length >= 2) {
//                    sw.factor = ConvertUtils.toFloat(tempArr[1], 1.0f);
//                } else {
//                    sw.factor = 1.0f;
//                }
//                stateWraps.add(sw);
//            }
//        }
//    }
//    
//    /**
//     * 作用伤害值,这个方法由子类调用
//     * @param target 被攻击者
//     */
//    protected void applyHit(Actor target) {
//
//        FightUtils.applyHit(actor, target, damage, damageEl, stateWraps);
//        
//    }
//    
////    /**
////     * 作用伤害值,这个方法由子类调用
////     * @param target 被攻击者
////     */
////    protected void applyHit(Actor target) {
////        if (force > MathUtils.ZERO_NEAR) {
////            TempVars tv = TempVars.get();
////            tv.vect1.set(target.getModel().getWorldBound().getCenter())
////                    .subtractLocal(actor.getModel().getWorldBound().getCenter())
////                    .normalizeLocal().multLocal(force);
////            applyHit(actor, target, damage, tv.vect1);
////            tv.release();
////        } else {
////            // 无作用力
////            applyHit(actor, target, damage, null);
////        }
////        
////        // 被击中时要添加状态
////        if (stateWraps != null && !target.isDead()) {
////            for (StateWrap sw : stateWraps) {
////                if (sw.factor >= FastMath.nextRandomFloat()) {
////                    stateNetwork.addState(target, sw.stateId);
////                }
////            }
////        }
////    }
//    
////    private void applyHit(Actor attacker, Actor target, int skillDamage, Vector3f force) {
////        if (damageEl == null) {
////            return;
////        }
////        int finalDamage = 0;
////        DamageEl de = elService.getDamageEl(damageEl);
////        if (de != null) {
////            finalDamage = (int) de.getValue(attacker, skillDamage, target);
////        }
////        
////        // 3.伤害计算．finalDamage 必须大于0才有意义。
////        if (finalDamage > 0) {
////            actorNetwork.applyDamage(target, attacker, finalDamage);
////        }
////        
////        if (actorService.isDead(target)) {
////            
////            skillNetwork.playDead(target, null, false);
////
////            // 通知角色死亡
////            if (target.isPlayer()) {
////                // 告诉所有玩家
////                String attackerName = actorService.getName(attacker);
////                String targetName = actorService.getName(target);
////                String killedMess = ResourceManager.get("common.killed", new Object[] {targetName, attackerName});
////                playNetwork.addMessage(killedMess, MessageType.warn);
////                // 告诉目标:"你已经死亡"
////                playNetwork.addMessage(target, ResourceManager.get("common.dead"), MessageType.warn);
////            }
////            
////            // 奖励经验
////            int xpReward = actorService.getXpReward(attacker, target);
////            actorNetwork.applyXp(attacker, xpReward);
////            
////            // 奖励物品
////            List<ProtoData> dropItems = dropService.getRandomDropFull(target, null);
////            for (ProtoData item : dropItems) {
////                actorNetwork.rewardItem(attacker, item.getId(), item.getTotal());
////            }
////            
////            if (attacker.isPlayer()) {
////                playNetwork.addMessage(actor, ResourceManager.get("common.getXp", new Object[]{xpReward}), MessageType.info);
////            }
////        } else {
////            // 执行受伤技能
////            skillNetwork.playHurt(target, null, false);
////        }
////        
////        // 播放攻击伤害数字效果
////        DamageManager.showDamage(target, finalDamage);
////    }
//    
//    @Override
//    public boolean isInRange(Actor target) {
//        if (target == null) {
//            return false;
//        }
////        Logger.getLogger(FightSkill.class.getName()).log(Level.INFO
////                , "FightSkill attacker={0}, enemy={1}, skillRadius={2}, distance={3}"
////                , new Object[] {actor.getData().getId(), target.getData().getId(), skillRadius, actor.getDistance(target)});
//        
//        if (actor.getDistance(target) < data.getRadius() 
//                || actor.getModel().getWorldBound()
//                .intersects(target.getModel().getWorldBound())) {
//            return true;
//        }
//        
//        return false;
//    }
//}
