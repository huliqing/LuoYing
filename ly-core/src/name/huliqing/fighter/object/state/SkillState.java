/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.state;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.service.SkillService;

/**
 * 让目标角色执行一个技能
 * @author huliqing
 */
public class SkillState extends State {
    private final SkillService skillService = Factory.get(SkillService.class);
    private SkillType skillType;
    private boolean force;

    @Override
    public void initData(StateData data) {
        super.initData(data); 
        skillType = SkillType.identifyByName(data.getAttribute("skillType"));
        force = data.getAsBoolean("force", force);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        SkillData skillData = skillService.getSkill(actor, skillType);
        if (skillData != null) {
            skillService.playSkill(actor, skillData.getId(), force);
        }
    }
    
}
