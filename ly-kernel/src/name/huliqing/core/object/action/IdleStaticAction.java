/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.skill.Skill;

/**
 * 静态的IDLE行为，对于不能动，静止的所有物体的行为。
 * @author huliqing
 */
public class IdleStaticAction extends AbstractAction implements IdleAction {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);

    // 缓存技能id
    private Skill waitSkillId;
    
    @Override
    public void initialize() {
        super.initialize();
        waitSkillId = skillService.getSkill(actor, SkillType.wait);
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (!skillService.isWaiting(actor)) {
            if (waitSkillId != null) {
                skillNetwork.playSkill(actor, waitSkillId, false);
            }
            end();
        }
    }

    
}
