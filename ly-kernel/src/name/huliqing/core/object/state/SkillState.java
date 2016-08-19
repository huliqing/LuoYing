/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.skill.Skill;

/**
 * 让目标角色执行一个技能
 * @author huliqing
 */
public class SkillState extends State {
    private final SkillService skillService = Factory.get(SkillService.class);
    private SkillType skillType;
    private boolean force;

    @Override
    public void setData(StateData data) {
        super.setData(data); 
        skillType = SkillType.identifyByName(data.getAsString("skillType"));
        force = data.getAsBoolean("force", force);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        Skill skill = skillService.getSkill(actor, skillType);
        if (skill != null) {
            skillService.playSkill(actor, skill, force);
        }
    }
    
}
