/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.Skill;

/**
 * 消耗物品来使用一个技能，示例：如使用复活卷轴,在使用这个物品的时候会调用一
 * 个技能来让角色执行。
 * @author huliqing
 */
public class SkillItem extends AbstractItem {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    private String skillId;

    //----
    private Skill skill;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data); 
        skillId = data.getAsString("skill");
    }
    
    @Override
    public boolean canUse(Actor actor) {
        if (!super.canUse(actor))
            return false;
        
        // 判断所要使用的技能是否可以执行
        if (skill == null) {
            skill = skillService.loadSkill(skillId);
            if (skill == null) {
                return false;
            }
        }
        int skillStateCode = skillService.checkStateCode(actor, skill);
        if (skillStateCode != SkillConstants.STATE_OK) {
            if (actorService.isPlayer(actor)) {
                switch (skillStateCode) {
                    case SkillConstants.STATE_MANA_NOT_ENOUGH:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_MANA_NOT_ENOUGH), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_COOLDOWN:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_COOLDOWN), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_LOCKED:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_LOCKED), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_NOT_FOUND:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_NOT_FOUND), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_NOT_FOUND:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_FOUND), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_NOT_IN_RANGE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_IN_RANGE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_UNSUITABLE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_UNSUITABLE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_UNDEFINE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_UNDEFINE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_WEAPON_NEED_TAKE_ON:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NEED_TAKE_ON), MessageType.notice);
                        break;
                    case SkillConstants.STATE_WEAPON_NOT_ALLOW:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NOT_ALLOW), MessageType.notice);
                        break;
                    case SkillConstants.STATE_NEED_LEVEL:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_NEED_LEVEL, new Object[] {skill.getData().getLevelLimit()})
                                , MessageType.notice);
                        break;
                    case SkillConstants.STATE_CAN_NOT_INTERRUPT:
//                        Logger.getLogger(ItemSkillHandler.class.getName()).log(Level.INFO, "Could not interrupt current skill!");
                        break;
                    case SkillConstants.STATE_HOOK:
//                        Logger.getLogger(ItemSkillHandler.class.getName()).log(Level.INFO, "Could not play skill by Hook listener!");
                        break;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public void use(Actor actor) {
        super.use(actor);
        
        if (skill == null) {
            skill = skillService.loadSkill(skillId);
        }
        if (skill != null) {
            boolean result = skillService.playSkill(actor, skill, false);
            if (result) {
                itemService.removeItem(actor, data.getId(), 1);
            }
        }
    }
    
    
}
