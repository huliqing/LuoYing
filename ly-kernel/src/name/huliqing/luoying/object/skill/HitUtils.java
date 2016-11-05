/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.SkillService;

/**
 * @deprecated 以后直接用EntityNetwork.applyNumberAttributeValue(...)代替
 * @author huliqing
 */
public class HitUtils {
    private final ActorService actorService = Factory.get(ActorService.class);
//    private final DropNetwork dropNetwork = Factory.get(DropNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    private final static HitUtils INSTANCE = new HitUtils();
    
    private HitUtils() {}
    
    public static HitUtils getInstance() {
        return INSTANCE;
    }
    
    // remove20161103
//    /**
//     * 执行HIT操作
//     * @param attacker HIT的施放者,attacker可能为null,当攻击者为一些特殊魔法或是状态时可能为null.
//     * @param target HIT的受者
//     * @param hitAttrName hit的是哪一个属性ID
//     * @param hitFinalValue 最终的hitValue
//     */
//    public void applyHit(Entity attacker, Entity target, String hitAttrName, float hitFinalValue) {
//        
//        NumberAttribute attribute = target.getAttributeManager().getAttribute(hitAttrName, NumberAttribute.class);
//        if (attribute == null)
//            return;
//        
//        // 记住HIT之前的死亡状态
//        boolean oldDead = actorService.isDead(target);
//        // 记住HIT之前属性值。
//        float oldAttrValue = attribute.floatValue();
//        
//        // Apply hit
//        actorNetwork.hitNumberAttribute(target, attacker, hitAttrName, hitFinalValue);
//        
//        // 注意：这里要判断之前是不是已经死亡了。如果已经死亡就不能再执行“死亡”或
//        // “受伤”技能
//        if (oldDead) {
//            return;
//        }
//        
//        // 如果之前“没死”，则在hit之后要判断:
//        // 1.“死了”: 有可能直接就死亡了
//        // 2.“受伤了”:有可能只是受伤
//        // 3.“增益了”:有可能是增益buff
//        if (actorService.isDead(target)) {
//            
//            // 1.让死亡目标执行死亡技能
//            List<Skill> deadSkills = skillService.getSkillDead(target);
//            if (deadSkills != null && !deadSkills.isEmpty()) {
//                skillNetwork.playSkill(target, deadSkills.get(0), false);
//            }
//
//            // 2.通知目标角色死亡
//            if (actorService.isPlayer(target)) {
//                // bak20161012
////                // 告诉所有玩家
////                String attackerName = attacker != null ? attacker.getData().getName() : "";
////                String targetName = target.getData().getName();
////                String killedMess = ResourceManager.get(ResConstants.COMMON_KILLED, new Object[] {targetName, attackerName});
////                playNetwork.addMessage(killedMess, MessageType.notice);
////                // 告诉目标:"你已经死亡"
////                playNetwork.addMessage(target, ResourceManager.get(ResConstants.COMMON_DEAD), MessageType.notice);
//            }
//            
//            // 3.对攻击者进行经验和物品奖励
//            if (attacker != null) {
//                // 掉落物品给attacker
//                dropNetwork.doDrop(target, attacker);
//            }
//        } else {
//            // 注：hit技能不一定是减益的。当目标受hit后属性值降低才是减益技能，
//            // 当减益发生时则判断为受伤并执行hurt.
//            float newAttrValue = attribute.floatValue();
//            if (newAttrValue < oldAttrValue) {
//                List<Skill> hurtSkills = skillService.getSkillHurt(target);
//                if (hurtSkills != null && !hurtSkills.isEmpty()) {
//                    skillNetwork.playSkill(target, hurtSkills.get(0), false);
//                }
//            }
//        }
//    }
    
}
