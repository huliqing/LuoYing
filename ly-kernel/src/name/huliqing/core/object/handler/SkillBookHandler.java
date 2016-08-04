/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.manager.ResourceManager;

/**
 * 技能书的学习handler
 * @author huliqing
 * @since 1.2
 */
public class SkillBookHandler extends AbstractHandler {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    // 要学习的技能ID
    private String skill;

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
        this.skill = data.getAttribute("skill");
    }

    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        if (skill == null) {
            return false;
        }
        if (skillService.isSkillLearned(actor, skill)) {
            playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_SKILL_LEARNED), MessageType.notice);
            return false;
        }
        return true;
    }

    @Override
    protected void useObject(Actor actor, ObjectData data) {
        // 学习技能
        skillNetwork.addSkill(actor, skill);
        
        // 学习后减少物品
        itemService.removeItem(actor, data.getId(), 1);
        
        // 添加文字提示
        String skillName = ResourceManager.getObjectName(skill);
        playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_LEARN_SKILL, new Object[] {skillName}), MessageType.info);
    }
    
}
