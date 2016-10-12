/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.item;

import java.util.logging.Logger;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ItemData;
import name.huliqing.ly.layer.service.ItemService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.SkillModule;
import name.huliqing.ly.object.skill.Skill;

/**
 * 消耗物品来使用一个技能，示例：如使用复活卷轴,在使用这个物品的时候会调用一
 * 个技能来让角色执行。
 * @author huliqing
 */
public class SkillItem extends AbstractItem {
    private static final Logger LOG = Logger.getLogger(SkillItem.class.getName());
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
    private String skillId;

    //----
//    private Skill skill;
    
    @Override
    public void setData(ItemData data) {
        super.setData(data); 
        skillId = data.getAsString("skill");
    }
    
    @Override
    public boolean canUse(Entity actor) {
        return super.canUse(actor);
        
        // remove20161012,不再判断技能限制,只由Item自身的限制即可
//        if (!super.canUse(actor))
//            return false;
//        
//        // 判断所要使用的技能是否可以执行
//        if (skill == null) {
//            skill = Loader.load(skillId);
//            if (skill == null) {
//                return false;
//            }
//        }
//        skill.setActor(actor);
//        int skillStateCode = skillService.checkStateCode(actor, skill);
//        return skillStateCode == SkillConstants.STATE_OK;
    }
    
    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        Skill skill = Loader.load(skillId);
        if (skill != null) {
            SkillModule skillModule = actor.getEntityModule().getModule(SkillModule.class);
            if (skillModule != null) {
                skill.setActor(actor);
                skillModule.playSkill(skill, false, skillModule.checkNotWantInterruptSkills(skill));
            }
        }
        
        // remove20161012
//        if (skill == null) {
//            skill = skillService.loadSkill(skillId);
//            if (skill == null) {
//                LOG.log(Level.WARNING, "Skill not found by SkillItem! skillId={0}, SkillItem={1}, actorId={2}"
//                        , new Object[]{skillId, data.getId(), actor.getData().getId()});
//                return;
//            }
//        }
//        boolean result = skillService.playSkill(actor, skill, false);
//        if (result) {
//            itemService.removeItem(actor, data.getId(), 1);
//        }
    }
    
    
}
