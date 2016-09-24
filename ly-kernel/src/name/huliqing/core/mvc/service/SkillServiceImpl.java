/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.define.DefineFactory;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.module.SkillModule;
import name.huliqing.core.object.module.SkillPlayListener;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skill.SkillTag;
import name.huliqing.core.object.skill.Walk;

/**
 *
 * @author huliqing
 */
public class SkillServiceImpl implements SkillService {
    private final static Logger LOG = Logger.getLogger(SkillServiceImpl.class.getName());

    private AttributeService attributeService;
    private ElService elService;
    private PlayService playService;
//    private ActorService actorService;
    
    @Override
    public void inject() {
        attributeService = Factory.get(AttributeService.class);
        elService = Factory.get(ElService.class);
        playService = Factory.get(PlayService.class);
//        actorService = Factory.get(ActorService.class);
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
    public void addSkill(Actor actor, String skillId) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.addSkill(loadSkill(skillId));
        }
    }

    @Override
    public boolean removeSkill(Actor actor, String skillId) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            Skill rmSkill = module.getSkill(skillId);
            if (rmSkill != null) {
                return module.removeSkill(rmSkill);
            }
        }
        return false;
    }
    
    @Override
    public Skill getSkill(Actor actor, String skillId) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkill(skillId);
        }
        return null;
    }

    @Override
    public Skill getSkillWaitDefault(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            List<Skill> waitSkills = module.getSkillWait(null);
            if (waitSkills != null && !waitSkills.isEmpty()) {
                return waitSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public Skill getSkillHurtDefault(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            List<Skill> hurtSkills = module.getSkillHurt(null);
            if (hurtSkills != null && !hurtSkills.isEmpty()) {
                return hurtSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public Skill getSkillDeadDefault(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            List<Skill> deadSkills = module.getSkillDead(null);
            if (deadSkills != null && !deadSkills.isEmpty()) {
                return deadSkills.get(0);
            }
        }
        return null;
    }

    @Override
    public List<Skill> getSkillWait(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillWait(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillHurt(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillHurt(null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkillDead(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillDead(null);
        }
        return null;
    }
    
    @Override
    public List<Skill> getSkillByTags(Actor actor, long skillTags) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkillByTags(skillTags, null);
        }
        return null;
    }

    @Override
    public List<Skill> getSkills(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getSkills();
        }
        return null;
    }
   
    // remove20160919
//    @Override
//    public Skill getPlayingSkill(Actor actor, SkillType skillType) {
//        SkillModule module = actor.getModule(SkillModule.class);
//        if (module != null) {
//            for (Skill skill : module.getRunningSkills()) {
//                if (skill.getSkillType() == skillType) {
//                    return skill;
//                }
//            }
//        }
//        return null;
//    }

//    @Override
//    public long getPlayingSkillStates(Actor actor) {
//        SkillModule module = actor.getModule(SkillModule.class);
//        if (module != null) {
//            return module.getRunningSkillStates();
//        }
//        return 0;
//    }
    
    @Override
    public void addSkillListener(Actor actor, SkillListener skillListener) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.addSkillListener(skillListener);
        }
    }

    @Override
    public void addSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.addSkillPlayListener(skillPlayListener);
        }
    }

    @Override
    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
        SkillModule module = actor.getModule(SkillModule.class);
        return module != null && module.removeSkillListener(skillListener);
    }

    @Override
    public boolean removeSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener) {
        SkillModule module = actor.getModule(SkillModule.class);
        return module != null && module.removeSkillPlayListener(skillPlayListener);
    }

//    // 检查并升级技能
//    private void skillLevelUp(Actor actor, SkillData data) {
//        if (data.getLevel() >= data.getMaxLevel()) 
//            return;
//        if (data.getLevelUpEl() == null)
//            return;
//        int levelPoints = (int) elService.getLevelEl(data.getLevelUpEl(), data.getLevel() + 1);
//        if (data.getSkillPoints() >= levelPoints) {
//            data.setLevel(data.getLevel() + 1);
//            data.setSkillPoints(data.getSkillPoints() - levelPoints);
//            if (playService.getPlayer() == actor) {
//                playService.addMessage(
//                        ResourceManager.get(ResConstants.SKILL_LEVEL_UP, new Object[]{ResourceManager.getObjectName(data)})
//                        , MessageType.item);
//            }
//            skillLevelUp(actor, data);
//        }
//    }
    
    @Override
    public boolean isPlayable(Actor actor, Skill skill) {
        return checkStateCode(actor, skill) == SkillConstants.STATE_OK;
    }
    
    @Override
    public int checkStateCode(Actor actor, Skill skill) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.checkStateCode(skill);
        }
        return SkillConstants.STATE_UNDEFINE;
    }

    @Override
    public boolean isCooldown(Skill skill) {
        return LY.getGameTime() - skill.getData().getLastPlayTime() 
                < skill.getData().getCooldown() * 1000;
    }

    @Override
    public boolean hasSkill(Actor actor, String skillId) {
        SkillModule module = actor.getModule(SkillModule.class);
        return module != null && module.getSkill(skillId) != null;
    }
    
    @Override
    public boolean playSkill(Actor actor, Skill skill, boolean force) {
        if (skill == null) {
            return false;
        }
        SkillModule c = actor.getModule(SkillModule.class);
        if (c != null) {
            return c.playSkill(skill, force);
        }
        return false;
    }

    @Override
    public boolean playSkill(Actor actor, String skillId, boolean force) {
        SkillModule c = actor.getModule(SkillModule.class);
        if (c == null) {
            return false;
        }
        Skill skill = getSkill(actor, skillId);
        return playSkill(actor, skill, force);
    }

    @Override
    public boolean playWalk(Actor actor, String skillId, Vector3f dir, boolean faceToDir, boolean force) {
        SkillModule c = actor.getModule(SkillModule.class);
        if (c == null) {
            return false;
        }
        
        Skill skill = c.getSkill(skillId);        
        if (skill instanceof Walk) {
            Walk wSkill = (Walk) skill;
            wSkill.setWalkDirection(dir);
            if (faceToDir) {
                wSkill.setViewDirection(dir);
            }
        }
        return playSkill(actor, skill, force);
    }
    
    @Override
    public boolean isPlayingSkill(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkills().size() > 0;
        }
        return false;
    }
    
    @Override
    public boolean isPlayingSkill(Actor actor, long skillTags) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getPlayingSkillTags() & skillTags) != 0;
        }
        return false;
    }
    
    @Override
    public long getPlayingSkillTags(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getPlayingSkillTags();
        }
        return -1;
    }
    
    @Override
    public void lockSkillTags(Actor actor, long skillTags) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.lockSkillTags(skillTags);
        }
    }

    @Override
    public void unlockSkillTags(Actor actor, long skillTags) {
        SkillModule module = actor.getModule(SkillModule.class);
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
