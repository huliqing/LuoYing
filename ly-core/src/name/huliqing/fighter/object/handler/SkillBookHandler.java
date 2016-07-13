/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.manager.ResourceManager;

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
    public boolean canUse(Actor actor, ProtoData data) {
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
    protected void useObject(Actor actor, ProtoData data) {
        // 学习技能
        skillNetwork.addSkill(actor, skill);
        
        // 学习后减少物品
        itemService.removeItem(actor, data.getId(), 1);
        
        // 添加文字提示
        String skillName = ResourceManager.getObjectName(skill);
        playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_LEARN_SKILL, new Object[] {skillName}), MessageType.info);
    }
    
}
