/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import com.jme3.math.FastMath;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 * 让目标角色执行一个技能
 * @author huliqing
 */
public class SkillState extends AbstractState {
    private final DefineService defineService = Factory.get(DefineService.class);
//    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    private SkillModule skillModule;
    
    private long skillTypes;
    
    // remove20161217
//    private boolean force;

    @Override
    public void setData(StateData data) {
        super.setData(data);
        skillTypes = defineService.getSkillTypeDefine().convert(data.getAsArray("skillTypes"));
        
        // remove20161217
        // 不再支持这个参数
//        force = data.getAsBoolean("force", force);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        skillModule = entity.getModuleManager().getModule(SkillModule.class);
        
        if (skillModule == null)
            return;
        
        List<Skill> tagSkills = skillModule.getSkillByTypes(skillTypes, null);
        if (tagSkills == null || tagSkills.isEmpty())
            return;
        
        // 这里使用随机数，所以要用skillNetwork.
//        skillNetwork.playSkill(entity, tagSkills.get(FastMath.nextRandomInt(0, tagSkills.size() - 1)), force);
        
        Skill skill = tagSkills.get(FastMath.nextRandomInt(0, tagSkills.size() - 1));
        entityNetwork.useObjectData(entity, skill.getData().getUniqueId());
    }
    
}
