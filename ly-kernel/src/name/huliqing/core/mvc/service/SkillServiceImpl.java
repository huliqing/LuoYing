/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.object.Loader;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.module.SkillModule;
import name.huliqing.core.object.module.SkillPlayListener;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skill.Walk;

/**
 *
 * @author huliqing
 */
public class SkillServiceImpl implements SkillService {
    private final static Logger LOG = Logger.getLogger(SkillServiceImpl.class.getName());

    private SkinService skinService;
    private AttributeService attributeService;
    private ElService elService;
    private PlayService playService;
    private ActorService actorService;
    
    @Override
    public void inject() {
        attributeService = Factory.get(AttributeService.class);
        skinService = Factory.get(SkinService.class);
        elService = Factory.get(ElService.class);
        playService = Factory.get(PlayService.class);
        actorService = Factory.get(ActorService.class);
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
    
    /**
     * 获取角色身上指定的技能，如果存在多个相同类型的技能，则返回第一个找到
     * 的就可以。
     * @param actor
     * @param skillType
     * @return 
     */
    @Override
    public Skill getSkill(Actor actor, SkillType skillType) {
        // remove
//        return skillDao.getSkillFirst(actor.getData(), skillType);

        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            for (Skill s : module.getSkills()) {
                if (s.getSkillType() == skillType) {
                    return s;
                }
            }
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
    
    @Override
    public Skill getPlayingSkill(Actor actor, SkillType skillType) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            for (Skill skill : module.getRunningSkills()) {
                if (skill.getSkillType() == skillType) {
                    return skill;
                }
            }
        }
        return null;
    }

    @Override
    public long getPlayingSkillStates(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return module.getRunningSkillStates();
        }
        return 0;
    }
    
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

    // 检查并升级技能
    private void skillLevelUp(Actor actor, SkillData data) {
        if (data.getLevel() >= data.getMaxLevel()) 
            return;
        if (data.getLevelUpEl() == null)
            return;
        int levelPoints = (int) elService.getLevelEl(data.getLevelUpEl(), data.getLevel() + 1);
        if (data.getSkillPoints() >= levelPoints) {
            data.setLevel(data.getLevel() + 1);
            data.setSkillPoints(data.getSkillPoints() - levelPoints);
            if (playService.getPlayer() == actor) {
                playService.addMessage(
                        ResourceManager.get(ResConstants.SKILL_LEVEL_UP, new Object[]{ResourceManager.getObjectName(data)})
                        , MessageType.item);
            }
            skillLevelUp(actor, data);
        }
    }
    
    @Override
    public boolean isPlayable(Actor actor, Skill skill, boolean force) {
        return checkStateCode(actor, skill, force) == SkillConstants.STATE_OK;
    }
    
    @Override
    public int checkStateCode(Actor actor, Skill skill, boolean force) {
        // 1.强制执行，则忽略所有任何检查,即使武器不符合条件
        if (force) {
            return SkillConstants.STATE_OK;
        }
        
        SkillData data = skill.getData();
        
        // 2.如果技能被锁定中，则不能执行
        if (isLocked(actor, data.getSkillType())) {
            return SkillConstants.STATE_SKILL_LOCKED;
        }
        
        // 3.武器状态检查,有一些技能需要拿特定的武器才能执行。
        List<Integer> wts = data.getWeaponStateLimit();
        if (wts != null) {
            int weaponState = skinService.getWeaponState(actor);
            if (!wts.contains(weaponState)) {
                return SkillConstants.STATE_WEAPON_NOT_ALLOW;
            }
            
            if (!skinService.isWeaponTakeOn(actor)) {
                return SkillConstants.STATE_WEAPON_NEED_TAKE_ON;
            }
        }
        
        // 6.角色需要达到指定等级才能使用技能
        if (actorService.getLevel(actor) < data.getNeedLevel()) {
            return SkillConstants.STATE_NEED_LEVEL;
        }
        
        // 7.冷却中
        if (isCooldown(skill)) {
            return SkillConstants.STATE_SKILL_COOLDOWN;
        }
        
        // 8.属性值不够用
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                if (attributeService.getNumberAttributeValue(actor, ua.getAttribute(), 0) < ua.getAmount()) {
                    return SkillConstants.STATE_MANA_NOT_ENOUGH;
                }
            }
        }
        
        // 9.如果新技能自身判断不能执行，例如加血技能或许就不可能给敌军执行。
        // 有很多特殊技能是不能对一些特定目标执行的，所以这里需要包含技能自身的判断
        skill.setActor(actor); // 有时候skill是从外部载入的，没有设置actor，所以这里必须设置。
        int stateCode = skill.canPlay();
        if (stateCode != SkillConstants.STATE_OK) {
            return stateCode;
        }
        
        // 10.侦听器监听，允许一些侦听器阻止技能的执行,比如角色身上存在的一些状态
        // 效果,这些状态可能会监听角色技能的执行，并判断是否允许执行这个技能。
        // 比如“晕眩”、“缠绕”等状态就可能监测并侦听角色的技能，以阻止一些技能
        // 的执行。
        SkillModule module = actor.getModule(SkillModule.class);
        if (module == null) {
            return SkillConstants.STATE_UNDEFINE;
        }
        List<SkillPlayListener> skillPlayListeners = module.getSkillPlayListeners();
        if (skillPlayListeners != null && !skillPlayListeners.isEmpty()) {
            for (SkillPlayListener sl : skillPlayListeners) {
                if (!sl.onSkillHookCheck(actor, skill)) {
                    return SkillConstants.STATE_HOOK;
                }
            }
        }
        
        // ---- 
        
        // 11.如果当前正在执行的所有技能都可以被新技能覆盖，则直接返回OK.
        // 注意：overlaps判断要放在interrupts判断之前，因为如果可以覆盖正在执行
        // 的技能就不需要中断它们
        long runningStates = module.getRunningSkillStates();
        if ((data.getOverlaps() & runningStates) == runningStates) {
            return SkillConstants.STATE_OK;
        }
        
        // 12.当正在执行中的某些技能不能被新技能中断则也不能执行新技能。
        // 只要有一个正在执行的技能不能被中断，则不能执行新技能
        if ((data.getInterrupts() & runningStates) != runningStates) {
            return SkillConstants.STATE_CAN_NOT_INTERRUPT;
        }
        
        return SkillConstants.STATE_OK;
    }

    @Override
    public boolean isCooldown(Skill skill) {
        return LY.getGameTime() - skill.getData().getLastPlayTime() 
                < skill.getData().getCooldown() * 1000;
    }

    @Override
    public boolean isSkillLearned(Actor actor, String skillId) {
        SkillModule module = actor.getModule(SkillModule.class);
        return module != null && module.getSkill(skillId) != null;
    }
    
    @Override
    public boolean playSkill(Actor actor, Skill skill, boolean force) {
        int code = checkStateCode(actor, skill, force);
        if (code != SkillConstants.STATE_OK) {
            if (Config.debug) {
                LOG.log(Level.INFO, "Could not PlaySkill, actor={0}, skill={1}, force={2}, errorCode={3}"
                        , new Object[]{actor.getData().getName(), skill.getData().getId(), force, code});
            }
            return false;
        }
        
        // --1. 执行技能
        SkillModule c = actor.getModule(SkillModule.class);
        if (c == null) {
            return false;
        }
        c.playSkill(skill);
        
        // --2. 技能消耗
        SkillData data = skill.getData();
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                attributeService.addAttributeValue(actor, ua.getAttribute(), -ua.getAmount());
            }
        }

        // --3.记录最近一次执行时间
        data.setLastPlayTime(LY.getGameTime());

        // --4.检查技能升级
        data.setSkillPoints(data.getSkillPoints() + 1);
        skillLevelUp(actor, data);
        
        return true;
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
            return module.getRunningSkillStates() != 0;
        }
        return false;
    }
    
    @Override
    public boolean isPlayingSkill(Actor actor, SkillType skillType) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getRunningSkillStates() & (1 << skillType.getValue())) != 0;
        }
        return false;
    }

    @Override
    public boolean isWaiting(Actor actor) {
        // 对于“等待”来说，这不是一个持续执行的技能，只要最后一次执行的是wait技能，则
        // 可以认为角色正处于等待中。
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            Skill lastSkill = module.getLastSkill();
            return lastSkill != null && lastSkill.getSkillType() == SkillType.wait;
        }
        return false;
    }

    @Override
    public boolean isRunning(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getRunningSkillStates() & (1 << SkillType.run.getValue())) != 0;
        }
        return false;
    }
    
    @Override
    public boolean isDucking(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getRunningSkillStates() & (1 << SkillType.duck.getValue())) != 0;
        }
        return false;
    }

    @Override
    public boolean isAttacking(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getRunningSkillStates() & (1 << SkillType.attack.getValue())) != 0;
        }
        return false;
    }

    @Override
    public boolean isDefending(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            return (module.getRunningSkillStates() & (1 << SkillType.defend.getValue())) != 0;
        }
        return false;
    }

    @Override
    public void lockSkill(Actor actor, SkillType... skillType) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            long lockedState = module.getSkillLockedState();
            for (SkillType st : skillType) {
                lockedState |= 1 << st.getValue();
            }
            module.setSkillLockedState(lockedState);
        }
    }

    @Override
    public void lockSkillAll(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.setSkillLockedState(0xFFFFFFFF);
        }
    }

    @Override
    public void unlockSkill(Actor actor, SkillType... skillType) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            long lockedState = module.getSkillLockedState();
            for (SkillType st : skillType) {
                lockedState &= ~(1 << st.getValue());
            }
            module.setSkillLockedState(lockedState);
        }
    }

    @Override
    public void unlockSkillAll(Actor actor) {
        SkillModule module = actor.getModule(SkillModule.class);
        if (module != null) {
            module.setSkillLockedState(0);
        }
    }
    
    @Override
    public boolean isLocked(Actor actor, SkillType skillType) {
        SkillModule module = actor.getModule(SkillModule.class);
        return module != null && (module.getSkillLockedState() & (1 << skillType.getValue())) != 0;
    }
    
    @Override
    public void lockSkillChannels(Actor actor, String... channels) {
        actorService.setChannelLock(actor, true, channels);
    }

    @Override
    public void unlockSkillChannels(Actor actor, String... channels) {
        actorService.setChannelLock(actor, false, channels);
    }    
}
