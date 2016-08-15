/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.enums.Sex;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.Skill;

/**
 * 消耗物品来使用一个技能，示例：如使用复活卷轴,在使用这个物品的时候会调用一
 * 个技能来让角色执行。
 * @author huliqing
 */
public class ItemSkillHandler extends AbstractHandler {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    private String skillId;
    private RaceSexSkill[] rsses;
    
    // ---- inner
    
    @Override
    public void setData(HandlerData data) {
        super.setData(data); 
        skillId = data.getAttribute("skill");
        // 格式：race|sex|skill,race|sex|skill,...
        // sex=f/m
        String[] tempRsses = data.getAsArray("special");
        if (tempRsses != null) {
            rsses = new RaceSexSkill[tempRsses.length];
            String[] rssArr;
            for (int i = 0; i < tempRsses.length; i++) {
                rssArr = tempRsses[i].split("\\|");
                RaceSexSkill rss = new RaceSexSkill();
                rss.race = rssArr[0];
                rss.sex = Sex.identifyByFM(rssArr[1]);
                rss.skillId = rssArr[2];
            }
        } else {
            rsses = new RaceSexSkill[0]; 
        }
    }

    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        boolean result = super.canUse(actor, data);
        if (!result)
            return false;
        
        Skill skill = getSkill(actor);
        if (skill == null)
            return false;
        
        // 判断所要使用的技能是否可以执行
        int skillStateCode = skillService.checkStateCode(actor, skill, false);
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
                                , ResourceManager.get(ResConstants.SKILL_NEED_LEVEL, new Object[] {skill.getSkillData().getNeedLevel()})
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
    protected void useObject(Actor actor, ObjectData data) {
        Skill skill = getSkill(actor);
        if (skill != null) {
            boolean result = skillService.playSkill(actor, skill, false);
            if (result) {
//                remove(actor, data.getId(), 1);// 不需要进行提示
                itemService.removeItem(actor, data.getId(), 1);
            }
        }
    }
    
    private Skill getSkill(Actor actor) {
        String useSkillId = findRaceSexSkill(actorService.getRace(actor), actorService.getSex(actor));
        if (useSkillId == null) {
            useSkillId = skillId;
        }
        return skillService.loadSkill(useSkillId);
    }
    
    private String findRaceSexSkill(String race, Sex sex) {
        if (rsses.length <= 0) 
            return null;
        
        for (RaceSexSkill rss : rsses) {
            if (rss.race.equals(race) && rss.sex == sex) {
                return rss.skillId;
            }
        }
        return null;
    }
    
    // 匹配种族，性别，技能
    private class RaceSexSkill {
        // 种族类型
        String race;
        // 性别
        Sex sex;
        // 技能id
        String skillId;
    }
}
