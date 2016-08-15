/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.network.ProtoNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.DropService;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.manager.DamageManager;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class HitUtils {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final DropService dropService = Factory.get(DropService.class);
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
//    private final ItemNetwork itemNetwork = Factory.get(ItemNetwork.class);
    private final ProtoNetwork protoNetwork = Factory.get(ProtoNetwork.class);
    private final ProtoService protoService = Factory.get(ProtoService.class);
    
    private final static HitUtils INSTANCE = new HitUtils();
    
    private HitUtils() {}
    
    public static HitUtils getInstance() {
        return INSTANCE;
    }
    
    /**
     * 执行HIT操作
     * @param attacker HIT的施放者,attacker可能为null,当攻击者为一些特殊魔法或是状态时可能为null.
     * @param target HIT的受者
     * @param hitAttribute hit的是哪一个属性ID
     * @param hitFinalValue 最终的hitValue
     */
    public void applyHit(Actor attacker, Actor target, String hitAttribute, float hitFinalValue) {
        // 记住HIT之前的死亡状态
        boolean oldDead = actorService.isDead(target);
        // 记住HIT之前属性值。
        float oldAttrValue = attributeService.getDynamicValue(target, hitAttribute);
        
        // Apply hit
        actorNetwork.hitAttribute(target, attacker, hitAttribute, hitFinalValue);
        
        // 注意：这里要判断之前是不是已经死亡了。如果已经死亡就不能再执行“死亡”或
        // “受伤”技能
        if (oldDead) {
            return;
        }
        
        // 如果之前“没死”，则在hit之后要判断:
        // 1.“死了”: 有可能直接就死亡了
        // 2.“受伤了”:有可能只是受伤
        // 3.“增益了”:有可能是增益buff
        if (actorService.isDead(target)) {
            
            // 1.让死亡目标执行死亡技能
            SkillData deadSkill = skillService.getSkill(target, SkillType.dead);
            if (deadSkill != null) {
                skillNetwork.playSkill(target, deadSkill.getId(), false);
            }

            // 2.通知目标角色死亡
            if (actorService.isPlayer(target)) {
                // 告诉所有玩家
                String attackerName = attacker != null ? actorService.getName(attacker) : "";
                String targetName = actorService.getName(target);
                String killedMess = ResourceManager.get(ResConstants.COMMON_KILLED, new Object[] {targetName, attackerName});
                playNetwork.addMessage(killedMess, MessageType.notice);
                // 告诉目标:"你已经死亡"
                playNetwork.addMessage(target, ResourceManager.get(ResConstants.COMMON_DEAD), MessageType.notice);
            }
            
            // 3.对攻击者进行经验和物品奖励
            if (attacker != null) {
                // 奖励经验
                int xpReward = actorService.getXpReward(attacker, target);
                actorNetwork.applyXp(attacker, xpReward);
                
                // 奖励物品
                List<ObjectData> dropItems = dropService.getRandomDropFull(target, null);
                for (ObjectData item : dropItems) {
//                    itemNetwork.addItem(attacker, item.getId(), item.getTotal());
                    protoNetwork.addData(attacker, protoService.createData(item.getId()), item.getTotal());
                }
            }
            
            // remove0221由 actorNetwork.applyXp(attacker, xpReward);内部处理
//            // 5.对玩家提示经验获得,注：这里的玩家可能为其他客户端的玩家
//            // 需要向特定的客户端发送通知"经验获得"。
//            if (attacker.isPlayer()) {
//                playNetwork.addMessage(attacker, ResourceManager.get(ResConstants.COMMON_GET_XP, new Object[]{xpReward}), MessageType.info);
//            }
        } else {
            // 注：hit技能不一定是减益的。当目标受hit后属性值降低才是减益技能，
            // 当减益发生时则判断为受伤并执行hurt.
            float newAttrValue = attributeService.getDynamicValue(target, hitAttribute);
            if (newAttrValue < oldAttrValue) {
                SkillData sd = skillService.getSkill(target, SkillType.hurt);
                if (sd != null) {
                    skillNetwork.playSkill(target, sd.getId(), false);
                }
            }
        }
        
        // TODO: 重构，因为hit不一定是伤害值。
        // 播放攻击伤害数字效果
        DamageManager.getInstance().show(target, hitFinalValue);
    }
    
}
