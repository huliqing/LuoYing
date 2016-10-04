/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.math.FastMath;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.object.define.DefineFactory;
import name.huliqing.core.object.module.SkillModule;
import name.huliqing.core.object.skill.Skill;

/**
 * 让目标角色执行一个技能
 * @author huliqing
 */
public class SkillState extends AbstractState {
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private SkillModule skillModule;
    
    private long skillTags;
    private boolean force;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        skillTags = DefineFactory.getSkillTagDefine().convert(data.getAsArray("skillTags"));
        force = data.getAsBoolean("force", force);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        skillModule = actor.getModule(SkillModule.class);
        
        if (skillModule == null)
            return;
        
        List<Skill> tagSkills = skillModule.getSkillByTags(skillTags, null);
        if (tagSkills == null || tagSkills.isEmpty())
            return;
        
        // 这里使用随机数，所以要用skillNetwork.
        skillNetwork.playSkill(actor, tagSkills.get(FastMath.nextRandomInt(0, tagSkills.size() - 1)), force);
    }
    
}
