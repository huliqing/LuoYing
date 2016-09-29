/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.SkillListener;
import name.huliqing.core.object.module.SkillPlayListener;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.object.skill.SkillTag;

/**
 * 执行技能，如果没有特别说明，如果返回false则说明执行失败。返回true则说明
 * 执行成功.
 * @author huliqing
 */
public interface SkillService extends SkillNetwork {
    
    /**
     * 载入技能。
     * @param skillId
     * @return 
     */
    Skill loadSkill(String skillId);
    
    /**
     * 载入技能
     * @param skillData
     * @return 
     */
    Skill loadSkill(SkillData skillData);
    
    /**
     * 从角色身上移除一个技能
     * @param actor
     * @param skillId
     * @return 
     */
    boolean removeSkill(Actor actor, String skillId);
    
    /**
     * 获取角色的技能，如果角色身上不存在该技能则返回null
     * @param actor
     * @param skillId
     * @return 
     */
    Skill getSkill(Actor actor, String skillId);
    
    Skill getSkillWaitDefault(Actor actor);
    
    Skill getSkillHurtDefault(Actor actor);
    
    Skill getSkillDeadDefault(Actor actor);
            
    /**
     * 获取角色当前身上的所有技能，
     * @param actor
     * @return 
     */
    List<Skill> getSkills(Actor actor);
    
    List<Skill> getSkillWait(Actor actor);
    
    List<Skill> getSkillHurt(Actor actor);
    
    List<Skill> getSkillDead(Actor actor);
    
    /**
     * 通过技能标记来获取角色身上的技能
     * 的就可以。
     * @param actor
     * @param skillTags
     * @return 
     */
    List<Skill> getSkillByTags(Actor actor, long skillTags);
    
    /**
     * 获取角色当前正在执行的技能状态，返回值中每一个二进制位表示一个技能类
     * 型。如果没有正在技能的技能则返回0.
     * @param actor
     * @return 
     */
    long getPlayingSkillTags(Actor actor);
    
    /**
     * 给角色添加一个技能侦听器
     * @param actor
     * @param skillListener 
     */
    void addSkillListener(Actor actor, SkillListener skillListener);
    
    /**
     * 给角色添加一个技能侦听器
     * @param actor
     * @param skillPlayListener 
     */
    void addSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener);
    
    /**
     * 移除角色身上的技能侦听器
     * @param actor
     * @param skillListener
     * @return 
     */
    boolean removeSkillListener(Actor actor, SkillListener skillListener);
    
    /**
     * 移除角色身上的技能侦听器
     * @param actor
     * @param skillPlayListener
     * @return 
     */
    boolean removeSkillPlayListener(Actor actor, SkillPlayListener skillPlayListener);
    
    /**
     * 是否可以使用指定的技能，该方法返回一个状态码。使用该状态码来判断是否可以
     * 执行该技能。
     * @param actor
     * @param skill
     * @return stateCode {@link SkillConstants#STATE_XXX}
     */
    int checkStateCode(Actor actor, Skill skill);
    
    /**
     * 检查技能是否可以执行.
     * @param actor
     * @param skill
     * @return 
     */
    boolean isPlayable(Actor actor, Skill skill);

    /**
     * 判断技能是否处于冷却中
     * @param skill
     * @return 
     */
    boolean isCooldown(Skill skill);
    
    /**
     * 判断角色是否拥有指定的技能
     * @param actor
     * @param skillId
     * @return 
     */
    boolean hasSkill(Actor actor, String skillId);
    
    /**
     * 判断当前角色是否正在执行任何技能,包含所有已经定义的技能。注：对于正在
     * 执行animation动画的技能都应该返回true,如果技能已经执行结束，处于静止状态，
     * 则为false,比如某些非loop模式的wait技能，在wait技能执行结束后该方法应该
     * 返回false, 如果wait技能是循环的则该方法应该返回true.
     * @param actor 技能执行角色
     * @return 
     */
    boolean isPlayingSkill(Actor actor);
    
    /**
     * 判断目标角色是否正在执行的技能中是否包含有skillTags中所指定的技能。角色可能同时正在执行多个技能，
     * 如果其中有任何一个技能标记包含于skillTags中所指定的技能,则该方法返回true.
     * @param actor
     * @param skillTags
     * @return 
     */
    boolean isPlayingSkill(Actor actor, long skillTags);
    
    /**
     * 锁定指定角色的技能类型,当这些技能类型被锁定后，属于这些类型的技能将不
     * 能再执行，直到进行unlockSkill
     * @param actor 
     * @param skillTags 
     */
    void lockSkillTags(Actor actor, long skillTags);
    
    /**
     * 解锁指定角色的技能类型。
     * @param actor
     * @param skillTags 
     */
    void unlockSkillTags(Actor actor, long skillTags);

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
