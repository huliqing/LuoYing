/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.constants.SkillConstants;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillListener;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.module.SkillPlayListener;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.luoying.object.skill.SkillTag;

/**
 * 执行技能，如果没有特别说明，如果返回false则说明执行失败。返回true则说明
 * 执行成功.
 * @author huliqing
 */
public interface SkillService extends SkillNetwork {
    
    boolean playSkill(SkillModule skillModule, Skill skill, boolean force);
    
    /**
     * 从角色身上移除一个技能
     * @param actor
     * @param skillId
     * @return 
     */
    boolean removeSkill(Entity actor, String skillId);
    
    /**
     * 获取角色的技能，如果角色身上不存在该技能则返回null
     * @param actor
     * @param skillId
     * @return 
     */
    Skill getSkill(Entity actor, String skillId);
    
    Skill getSkillWaitDefault(Entity actor);
    
    Skill getSkillHurtDefault(Entity actor);
    
    Skill getSkillDeadDefault(Entity actor);
            
    /**
     * 获取角色当前身上的所有技能，
     * @param actor
     * @return 
     */
    List<Skill> getSkills(Entity actor);
    
    List<Skill> getSkillWait(Entity actor);
    
    List<Skill> getSkillHurt(Entity actor);
    
    List<Skill> getSkillDead(Entity actor);
    
    /**
     * 通过技能标记来获取角色身上的技能
     * 的就可以。
     * @param actor
     * @param skillTags
     * @return 
     */
    List<Skill> getSkillByTags(Entity actor, long skillTags);
    
    /**
     * 获取角色当前正在执行的技能状态，返回值中每一个二进制位表示一个技能类
     * 型。如果没有正在技能的技能则返回0.
     * @param actor
     * @return 
     */
    long getPlayingSkillTags(Entity actor);
    
    /**
     * 给角色添加一个技能侦听器
     * @param actor
     * @param skillListener 
     */
    void addSkillListener(Entity actor, SkillListener skillListener);
    
    /**
     * 给角色添加一个技能侦听器
     * @param actor
     * @param skillPlayListener 
     */
    void addSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener);
    
    /**
     * 移除角色身上的技能侦听器
     * @param actor
     * @param skillListener
     * @return 
     */
    boolean removeSkillListener(Entity actor, SkillListener skillListener);
    
    /**
     * 移除角色身上的技能侦听器
     * @param actor
     * @param skillPlayListener
     * @return 
     */
    boolean removeSkillPlayListener(Entity actor, SkillPlayListener skillPlayListener);
    
    /**
     * 是否可以使用指定的技能，该方法返回一个状态码。使用该状态码来判断是否可以
     * 执行该技能。
     * @param actor
     * @param skill
     * @return stateCode {@link SkillConstants#STATE_XXX}
     */
    int checkStateCode(Entity actor, Skill skill);
    
    /**
     * 检查技能是否可以执行.
     * @param actor
     * @param skill
     * @return 
     */
    boolean isPlayable(Entity actor, Skill skill);

    /**
     * 判断角色是否拥有指定的技能
     * @param actor
     * @param skillId
     * @return 
     */
    boolean hasSkill(Entity actor, String skillId);
    
    /**
     * 判断当前角色是否正在执行任何技能,包含所有已经定义的技能。注：对于正在
     * 执行animation动画的技能都应该返回true,如果技能已经执行结束，处于静止状态，
     * 则为false,比如某些非loop模式的wait技能，在wait技能执行结束后该方法应该
     * 返回false, 如果wait技能是循环的则该方法应该返回true.
     * @param actor 技能执行角色
     * @return 
     */
    boolean isPlayingSkill(Entity actor);
    
    /**
     * 判断目标角色是否正在执行的技能中是否包含有skillTags中所指定的技能。角色可能同时正在执行多个技能，
     * 如果其中有任何一个技能标记包含于skillTags中所指定的技能,则该方法返回true.
     * @param actor
     * @param skillTags
     * @return 
     */
    boolean isPlayingSkill(Entity actor, long skillTags);
    
    /**
     * 锁定指定角色的技能类型,当这些技能类型被锁定后，属于这些类型的技能将不
     * 能再执行，直到进行unlockSkill
     * @param actor 
     * @param skillTags 
     */
    void lockSkillTags(Entity actor, long skillTags);
    
    /**
     * 解锁指定角色的技能类型。
     * @param actor
     * @param skillTags 
     */
    void unlockSkillTags(Entity actor, long skillTags);

    /**
     * 获取技能标记
     * @param skillTag
     * @return 
     */
    SkillTag getSkillTag(String skillTag);
    
    /**
     * 将技能标记转换为使用二进制位表示的整数值，返回的数值中每个"1"位表示一个技能标记。
     * @param tags
     * @return 
     */
    long convertSkillTags(String... tags);
    
    
}
