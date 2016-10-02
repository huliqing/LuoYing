/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.ItemConstants;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;

/**
 * 技能书, 使用后消耗技能书,并获得一个指定的技能
 * @author huliqing
 */
public class BookItem extends AbstractItem {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    // 要学习的技能ID
    private String skill;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data);
        this.skill = data.getAsString("skill");
    }
   
    // remove20161002
//    @Override
//    public boolean canUse(Actor actor) {
//        if (skill == null) {
//            return false;
//        }
//        if (skillService.hasSkill(actor, skill)) {
////            playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_SKILL_LEARNED), MessageType.notice);
//            return false;
//        }
//        return super.canUse(actor);
//    }
    
    @Override
    public int checkStateCode(Actor actor) {
        if (skill == null) {
            return ItemConstants.STATE_UNDEFINE;
        }
        if (skillService.hasSkill(actor, skill)) {
            return ItemConstants.STATE_UNDEFINE;
        }
        return super.checkStateCode(actor); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void use(Actor actor) {
        super.use(actor);
        
        // 学习技能
        skillService.addSkill(actor, skill);
        
        // 学习后减少物品
        itemService.removeItem(actor, data.getId(), 1);
        
        // 添加文字提示
//        String skillName = ResourceManager.getObjectName(skill);
        // playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_LEARN_SKILL, new Object[] {skillName}), MessageType.info);
    }
    
}
