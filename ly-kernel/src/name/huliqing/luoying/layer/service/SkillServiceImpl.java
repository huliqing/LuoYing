/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import com.jme3.math.Vector3f;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.luoying.constants.SkillConstants;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.define.DefineFactory;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillListener;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.module.SkillPlayListener;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.object.skill.SkillTag;
import name.huliqing.luoying.object.skill.Walk;

/**
 *
 * @author huliqing
 */
public class SkillServiceImpl implements SkillService {
    private final static Logger LOG = Logger.getLogger(SkillServiceImpl.class.getName());

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public Skill loadSkill(String skillId) {
        return Loader.load(skillId);
    }

    @Override
    public Skill loadSkill(SkillData skillData) {
        return Loader.load(skillData);
    }

    @Override
    public void addSkill(Entity actor, String skillId) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            module.addSkill(loadSkill(skillId));
        }
    }

    @Override
    public boolean removeSkill(Entity actor, String skillId) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
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
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkill(skillId);
        }
        return null;
    }

    @Override
    public Skill getSkillWaitDefault(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
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
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
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
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
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
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillWait(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillHurt(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillHurt(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillDead(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillDead(null);
        }
        return null;
    }
    
    @Override
    public List<Skill> getSkillByTags(Entity actor, long skillTags) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillByTags(skillTags, null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkills(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getSkills();
        }
        return null;
    }
   
    @Override
    public void addSkillListener(Entity actor, SkillListener skillListener) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            module.addSkillListener(skillListener);
        }
    }

    @Override
    public void addSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            module.addSkillPlayListener(skillPlayListener);
        }
    }

    @Override
    public boolean removeSkillListener(Entity actor, SkillListener skillListener) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        return module != null && module.removeSkillListener(skillListener);
    }

    @Override
    public boolean removeSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        return module != null && module.removeSkillPlayListener(skillPlayListener);
    }
    
    @Override
    public boolean isPlayable(Entity actor, Skill skill) {
        return checkStateCode(actor, skill) == SkillConstants.STATE_OK;
    }
    
    @Override
    public int checkStateCode(Entity actor, Skill skill) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.checkStateCode(skill);
        }
        return SkillConstants.STATE_UNDEFINE;
    }

    @Override
    public boolean hasSkill(Entity actor, String skillId) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        return module != null && module.getSkill(skillId) != null;
    }

    @Override
    public boolean playSkill(SkillModule skillModule, Skill skill, boolean force, List<Long> wantNotInterruptSkills) {
        if (skill == null) {
            return false;
        }
        return skillModule.playSkill(skill, force, wantNotInterruptSkills);
    }
    
    @Override
    public boolean playSkill(Entity actor, Skill skill, boolean force) {
        SkillModule c = actor.getEntityModule().getModule(SkillModule.class);
        if (c != null) {
            return playSkill(c, skill, force, c.checkNotWantInterruptSkills(skill));
        }
        return false;
    }

    // remove20161001
//    @Override
//    public boolean playSkill(Entity actor, String skillId, boolean force) {
//        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
//        if (module == null) {
//            return false;
//        }
//        Skill skill = module.getSkill(skillId);
//        return playSkill(module, skill, force, module.checkNotWantInterruptSkills(skill));
//    }
    
    @Override
    public boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
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
        return playSkill(module, walkSkill, force, null);
    }
    
    @Override
    public boolean isPlayingSkill(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkills().size() > 0;
        }
        return false;
    }
    
    @Override
    public boolean isPlayingSkill(Entity actor, long skillTags) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return (module.getPlayingSkillTags() & skillTags) != 0;
        }
        return false;
    }
    
    @Override
    public long getPlayingSkillTags(Entity actor) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkillTags();
        }
        return -1;
    }
    
    @Override
    public void lockSkillTags(Entity actor, long skillTags) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            module.lockSkillTags(skillTags);
        }
    }

    @Override
    public void unlockSkillTags(Entity actor, long skillTags) {
        SkillModule module = actor.getEntityModule().getModule(SkillModule.class);
        if (module != null) {
            module.unlockSkillTags(skillTags);
        }
    }

    @Override
    public SkillTag getSkillTag(String skillTag) {
        return DefineFactory.getSkillTagDefine().getSkillTag(skillTag);
    }

    @Override
    public long convertSkillTags(String... tags) {
        return DefineFactory.getSkillTagDefine().convert(tags);
    }
    

}
