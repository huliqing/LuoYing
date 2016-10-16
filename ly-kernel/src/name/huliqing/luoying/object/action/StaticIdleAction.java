/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.action;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 静态的IDLE行为，对于不能动，静止的所有物体的行为。
 * @author huliqing
 */
public class StaticIdleAction extends AbstractAction implements IdleAction {
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private SkillModule skillModule;
    
    // 缓存技能id
    private Skill waitSkill;
    
    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getEntityModule().getModule(SkillModule.class);
        
        List<Skill> waitSkills = skillModule.getSkillWait(null);
        if (waitSkills != null && !waitSkills.isEmpty()) {
            waitSkill = waitSkills.get(0);
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (!skillModule.isWaiting()) {
            if (waitSkill != null) {
                skillNetwork.playSkill(actor, waitSkill, false);
            }
            end();
        }
    }
    
}
