/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.enums.SkillType;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.Skill;

/**
 * 执行技能，如果没有特别说明，如果返回false则说明执行失败。返回true则说明
 * 执行成功.
 * @author huliqing
 */
public interface SkillService extends Inject {
    
    /**
     * 载入一个技能。
     * @param skillId
     * @return 
     */
    Skill loadSkill(String skillId);
    
    /**
     * 给角色添加技能
     * @param actor
     * @param skillId 
     */
    void addSkill(Actor actor, String skillId);
    
    /**
     * 获取角色的技能，如果不存在该技能则返回null
     * @param actor
     * @param skillId
     * @return 
     */
    SkillData getSkill(Actor actor, String skillId);
    
    /**
     * 获取角色身上指定的技能，如果存在多个相同类型的技能，则返回第一个找到
     * 的就可以。
     * @param actor
     * @param skillType
     * @return 
     */
    SkillData getSkill(Actor actor, SkillType skillType);
    
    /**
     * 获得一个可用于直接执行的技能,角色必须拥有该技能，否则该方法将返回null.
     * @param actor
     * @param skillId
     * @return 
     */
    Skill getSkillInstance(Actor actor, String skillId);
    
    /**
     * 获取角色所有技能,注意：返回列表只作为只读使用，不要增删其中的数据
     * @param actor
     * @return 
     */
    List<SkillData> getSkill(Actor actor);
    
    /**
     * @deprecated 以后不要使用随机获取技能，这会在客户端和服务端执行的时候不一致。
     * 获取一个随机指定类型的技能,如果不存在指定类型的技能则返回null.
     * @param actor
     * @param skillType
     * @return 
     */
    SkillData getSkillRandom(Actor actor, SkillType skillType);
    
    /**
     * @deprecated 以后不要使用随机获取技能，这会在客户端和服务端执行的时候不一致。
     * 获取一个随机防御技能,如果没有该技能则返回null.
     * @param actor
     * @return 
     */
    SkillData getSkillRandomDefend(Actor actor);
    
    /**
     * @deprecated 以后不要使用随机获取技能，这会在客户端和服务端执行的时候不一致。
     * 获取一个随机躲闪技能,如果没有则返回null
     * @param actor
     * @return 
     */
    SkillData getSkillRandomDuck(Actor actor);
    
    /**
     * @deprecated 以后不要使用随机获取技能，这会在客户端和服务端执行的时候不一致。
     * @param actor
     * @return 
     */
    SkillData getSkillRandomWalk(Actor actor);
    
    /**
     * 执行一个技能实例
     * @param actor
     * @param skill
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能即，该方法将保证返回true。
     * @return 
     */
    boolean playSkill(Actor actor, Skill skill, boolean force);
    
    /**
     * 执行技能,技能的执行会受角色属性状态的影响
     * @param actor
     * @param skillId
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能即，该方法将保证返回true。
     * @return 
     */
    boolean playSkill(Actor actor, String skillId, boolean force);
    
    /**
     * 执行“步行”技能，步行的速度等受角色属性的影响
     * @param actor
     * @param walkSkillId 必须是一个WalkSkill或WalkRun类型的ID
     * @param dir
     * @param faceToDir
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能即，该方法将保证返回true。
     * @return 
     */
    boolean playWalk(Actor actor, String skillId, Vector3f dir, boolean faceToDir, boolean force);
    
    /**
     * 执行朝向目标位置，注意是位置．
     * @param actor
     * @param position
     * @return 
     */
    boolean playFaceTo(Actor actor, Vector3f position);
    
    /**
     * 检查技能是否可以执行.
     * @param actor
     * @param data
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能,该方法将保证返回true。
     * @return 
     */
    boolean isPlayable(Actor actor, Skill skill, boolean force);
    
    /**
     * 是否可以使用指定的技能，该方法返回一个状态码。使用该状态码来判断是否可以
     * 执行该技能。
     * @param actor
     * @param data
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，该方法将保证返回SkillConstants.STATE_OK
     * @return stateCode {@link SkillConstants#STATE_XXX}
     */
    int checkStateCode(Actor actor, Skill skill, boolean force);

    /**
     * 判断技能是否处于冷却中
     * @param skillData
     * @return 
     */
    boolean isCooldown(SkillData skillData);
    
    /**
     * 判断角色是否已经学习了指定的技能
     * @param actor
     * @param skillId
     * @return 
     */
    boolean isSkillLearned(Actor actor, String skillId);
    
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
     * 判断目标角色是否正在执行某一类型的技能。
     * @param actor
     * @param skillType
     * @return 
     */
    boolean isPlayingSkill(Actor actor, SkillType skillType);
    
    /**
     * 判断指定角色是否处理“等待”状态，注：不管该“等待”技能是循环或者非
     * 循环的，只要角色最后一个执行的是“等待”技能，则该方法应该返回true.
     * @param actor
     * @return 
     */
    boolean isWaiting(Actor actor);
    
    /**
     * 获取角色当前正在执行的技能,如果没有找到该技能，则返回null.
     * @param actor 
     * @param skillType 正在执行的技能类型 
     * @return 
     */
    Skill getPlayingSkill(Actor actor, SkillType skillType);
    
    /**
     * 获取角色当前正在执行的技能状态，返回值中每一个二进制位表示一个技能类
     * 型。如果没有正在技能的技能则返回0.
     * @param actor
     * @return 
     */
    long getPlayingSkillStates(Actor actor);
    
    /**
     * 锁定指定角色的技能类型,当这些技能类型被锁定后，属于这些类型的技能将不
     * 能再执行，直到进行unlockSkill
     * @param actor
     * @param skillType
     * @return 
     * @see #unlockSkill(name.huliqing.fighter.object.actor.Actor, name.huliqing.fighter.enums.SkillType[]) 
     */
    void lockSkill(Actor actor, SkillType... skillType);
    
    /**
     * 锁定角色的所有技能。
     * @param actor 
     */
    void lockSkillAll(Actor actor);
    
    /**
     * 解锁指定角色的技能类型。
     * @param actor
     * @param skillType 
     * @see #lockSkill(name.huliqing.fighter.object.actor.Actor, name.huliqing.fighter.enums.SkillType[]) 
     */
    void unlockSkill(Actor actor, SkillType... skillType);
    
    /**
     * 解锁角色的所有技能。
     * @param actor 
     */
    void unlockSkillAll(Actor actor);
    
    /**
     * 判断指定角色的某个技能类型是否被锁定.
     * @param actor
     * @param skillType
     * @return 
     * @see #lockSkill(name.huliqing.fighter.object.actor.Actor, name.huliqing.fighter.enums.SkillType[]) 
     * @see #unlockSkill(name.huliqing.fighter.object.actor.Actor, name.huliqing.fighter.enums.SkillType[]) 
     */
    boolean isLocked(Actor actor, SkillType skillType);
    
    /**
     * 锁定角色的技能通道（AnimChannel），注：锁定技能通道并不会阻止技能的
     * 执行，只会阻止角色某些动画通道执行角色动画而已。
     * @param actor
     * @param channels 技能通道名称列表
     */
    void lockSkillChannels(Actor actor, String... channels);
    
    /**
     * 解锁角色的技能通道
     * @param actor
     * @param channels 
     */
    void unlockSkillChannels(Actor actor, String... channels);
    
//    /**
//     * 获取技能的CutTimeEndRate,这个值是对技能执行时间的剪裁，即对技能的结束阶段
//     * 的时间进行剪裁，这个值受角色属性影响，并且不会大于CutTimeEndMax.
//     * 如果技能没有指定影响该值的角色属性，或者角色没有指定的属性值，则这个值应
//     * 返回0.<br />
//     * 注：这个值返回的是一个比率，取值为[0.0,1.0]之间，即表示要剪裁掉的技能总时间
//     * 的比率。例如：当返回值为0.5时，即表示技能的总执行时间要剪裁掉一半（时间的后半部分）
//     * @param actor
//     * @param skillData
//     * @return 
//     */
//    float getSkillCutTimeEndRate(Actor actor, SkillData skillData);
//    
//    /**
//     * 获取技能的执行速度,技能的执行速度受角色属性的影响，当技能指定了speedAttribute
//     * 后，角色的这个属性值将影响技能的执行速度。如果技能没有指定这个属性或
//     * 者角色没有这个属性，则这个方法应该返回1.0,即原始速度。
//     * @param actor
//     * @param skillData
//     * @return 返回的最小值为0.0001f，为避免除0错误，速度不能小于或等于0
//     */
//    float getSkillSpeed(Actor actor, SkillData skillData);
    
    /**
     * 获取技能的实际执行时间,技能的实际执行时间受：技能总时间、技能执行速度、
     * 技能的剪裁时间等影响
     * @param actor
     * @param skillData
     * @return 
     */
    float getSkillTrueUseTime(Actor actor, SkillData skillData);
    
}
