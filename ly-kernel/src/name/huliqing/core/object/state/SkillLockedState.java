/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.app.Application;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.StateData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.SkillListener;
import name.huliqing.core.object.skill.Skill;

/**
 * @author huliqing
 */
public class SkillLockedState extends State implements SkillListener {
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
//    private final StateService stateService = Factory.get(StateService.class);
//    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    private enum LockType {
        /** 锁定为Reset状态 */
        reset,
        
        /** 锁定当前动画帧 */
        frame;
        
        public static LockType identify(String name) {
            if (name == null)
                return null;
            
            for (LockType lt : values()) {
                if (lt.name().equals(name)) {
                    return lt;
                }
            }
            return null;
        }
    }
    
    // 表示要锁定的类型。
    private LockType lockType;
    
    // 是否锁定全部技能，如果为true,则忽略lockSkills.
    private boolean lockAll = false;
    
    // 要锁定的特定的技能列表.
    private SkillType[] lockSkillTypes;
    private List<String> lockSkillIds;
    private String[] lockChannels;
    // 这个选项可以防止角色被锁定后仍然可被其它角色“推动”的bug.(由于物理特性的原因)
    private boolean lockPhysics;
    
    // ---- inner

    @Override
    public void setData(StateData data) {
        super.setData(data); 
        lockType = LockType.identify(data.getAttribute("lockType"));
        lockAll = data.getAsBoolean("lockAll", lockAll);
        // 如果lockAll(锁定全部技能），则不需要理lockSkills。
        // 否则要获取特定的要锁定的技能类型列表。
        if (!lockAll) {
            String[] tempLockSkills = data.getAsArray("lockSkillTypes");
            if (tempLockSkills != null) {
                lockSkillTypes = new SkillType[tempLockSkills.length];
                for (int i = 0; i < tempLockSkills.length; i++) {
                    lockSkillTypes[i] = SkillType.identifyByName(tempLockSkills[i]);
                }
            }
            lockSkillIds = data.getAsList("lockSkillIds");
            lockChannels = data.getAsArray("lockChannels");
        }
        lockPhysics = data.getAsBoolean("lockPhysics", false);
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // 根据锁定类型来确定要锁定在reset状态还是当前动画帧。
        if (lockType != null && !actorService.isDead(actor)) {
            if (lockType == LockType.reset) {
                SkillData skillData = skillService.getSkill(actor, SkillType.reset);
                if (skillData != null) {
                    skillService.playSkill(actor, skillData.getId(), true);
                }
            } else if (lockType == LockType.frame) {
                // SKILL_RESET_DEFAULT会把当前动画帧定格住
                Skill skill = skillService.loadSkill(IdConstants.SKILL_RESET_DEFAULT);
                skillService.playSkill(actor, skill, true);
            }
        }

        // 锁定所有技能或特定技能。
        if (lockAll) {
            skillService.lockSkillAll(actor);
        } else {
            // 锁定特殊技能类型
            if (lockSkillTypes != null) {
                skillService.lockSkill(actor, lockSkillTypes);
            }
            // 锁定技能动画通道
            if (lockChannels != null) {
                skillService.lockSkillChannels(actor, lockChannels);
            }
            // 对于特定的技能ID需要通过技能侦听器来监听,在状态消失时要移除侦听器
            if (lockSkillIds != null) {
                skillService.addSkillListener(actor, this);
            }
        }
        
        if (lockPhysics) {
            actorService.setKinematic(actor, true);
        }
        
        // 削弱时间
        totalUseTime = data.getUseTime() - data.getUseTime() * data.getResist();
        
    }

    @Override
    public boolean onSkillHookCheck(Actor source, Skill skill) {
        // 如果要执行的技能刚才是被锁定的特定技能，则返回false,表示不能执行。
        if (lockSkillIds != null && lockSkillIds.contains(skill.getSkillData().getId())) {
            return false;
        }
        return true;
    }

    @Override
    public void onSkillStart(Actor source, Skill skill) {}

    @Override
    public void onSkillEnd(Actor source, Skill skill) {}

    @Override
    public void cleanup() {
        if (lockAll) {
            skillService.unlockSkillAll(actor);
        } else {
            if (lockSkillTypes != null) {
                skillService.unlockSkill(actor, lockSkillTypes);
            }
            if (lockChannels != null) {
                skillService.unlockSkillChannels(actor, lockChannels);
            }
            skillService.removeSkillListener(actor, this);
        }
        
        if (lockPhysics) {
            actorService.setKinematic(actor, false);
        }
        super.cleanup();
    }
    
}
