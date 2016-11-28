/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.log.StateCode;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillListener;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.module.SkillPlayListener;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.object.skill.SkillType;
import name.huliqing.luoying.object.skill.Walk;

/**
 *
 * @author huliqing
 */
public class SkillServiceImpl implements SkillService {
//    private final static Logger LOG = Logger.getLogger(SkillServiceImpl.class.getName());
    private final DefineService defineService = Factory.get(DefineService.class);

    @Override
    public void inject() {
        // ignore
    }
    
    @Override
    public boolean removeSkill(Entity actor, String skillId) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            Skill rmSkill = module.getSkill(skillId);
            if (rmSkill != null) {
                return module.removeSkill(rmSkill);
            }
        }
        return false;
    }
    
    @Override
    public Skill getSkill(Entity actor, String skillId) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkill(skillId);
        }
        return null;
    }

    @Override
    public Skill getSkillWaitDefault(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            List<Skill> waitSkills = module.getSkillWait(null);
            if (waitSkills != null && !waitSkills.isEmpty()) {
                return waitSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public Skill getSkillHurtDefault(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            List<Skill> hurtSkills = module.getSkillHurt(null);
            if (hurtSkills != null && !hurtSkills.isEmpty()) {
                return hurtSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public Skill getSkillDeadDefault(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            List<Skill> deadSkills = module.getSkillDead(null);
            if (deadSkills != null && !deadSkills.isEmpty()) {
                return deadSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public List<Skill> getSkillWait(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillWait(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillHurt(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillHurt(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillDead(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillDead(null);
        }
        return null;
    }
    
    @Override
    public List<Skill> getSkillByTypes(Entity actor, long skillTypes) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillByTypes(skillTypes, null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkills(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkills();
        }
        return null;
    }
   
    @Override
    public void addSkillListener(Entity actor, SkillListener skillListener) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            module.addSkillListener(skillListener);
        }
    }

    @Override
    public void addSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            module.addSkillPlayListener(skillPlayListener);
        }
    }

    @Override
    public boolean removeSkillListener(Entity actor, SkillListener skillListener) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        return module != null && module.removeSkillListener(skillListener);
    }

    @Override
    public boolean removeSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        return module != null && module.removeSkillPlayListener(skillPlayListener);
    }
    
    @Override
    public boolean isPlayable(Entity actor, Skill skill) {
        return checkStateCode(actor, skill) == StateCode.OK;
    }
    
    @Override
    public int checkStateCode(Entity actor, Skill skill) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.checkStateCode(skill);
        }
        return StateCode.UNDEFINE;
    }

    @Override
    public boolean hasSkill(Entity actor, String skillId) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        return module != null && module.getSkill(skillId) != null;
    }

    @Override
    public boolean playSkill(SkillModule skillModule, Skill skill, boolean force) {
        if (skill == null) {
            return false;
        }
        return skillModule.playSkill(skill, force);
    }
    
    @Override
    public boolean playSkill(Entity actor, Skill skill, boolean force) {
        SkillModule c = actor.getModuleManager().getModule(SkillModule.class);
        if (c != null) {
            return playSkill(c, skill, force);
        }
        return false;
    }
    
    @Override
    public boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module == null) {
            return false;
        }
        if (walkSkill instanceof Walk) {
            Walk wSkill = (Walk) walkSkill;
            wSkill.setWalkDirection(dir);
            if (faceToDir) {
                wSkill.setViewDirection(dir);
            }
        }
        return playSkill(module, walkSkill, force);
    }
    
    @Override
    public boolean isPlayingSkill(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkills().size() > 0;
        }
        return false;
    }
    
    @Override
    public boolean isPlayingSkill(Entity actor, long skillTypes) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return (module.getPlayingSkillTypes() & skillTypes) != 0;
        }
        return false;
    }
    
    @Override
    public long getPlayingSkillTypes(Entity actor) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkillTypes();
        }
        return -1;
    }
    
    @Override
    public void lockSkillTypes(Entity actor, long skillTypes) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            module.lockSkillTypes(skillTypes);
        }
    }

    @Override
    public void unlockSkillTypes(Entity actor, long skillTypes) {
        SkillModule module = actor.getModuleManager().getModule(SkillModule.class);
        if (module != null) {
            module.unlockSkillTypes(skillTypes);
        }
    }

    @Override
    public SkillType getSkillTypes(String skillType) {
        return defineService.getSkillTypeDefine().getSkillType(skillType);
    }

    @Override
    public long convertSkillTypes(String... skillTypes) {
        return defineService.getSkillTypeDefine().convert(skillTypes);
    }
    

}
