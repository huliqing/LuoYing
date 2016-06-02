/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.SkillType;

/**
 *
 * @author huliqing
 */
public interface SkillProcessor {
    
    /**
     * 更新技能逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 停止所有正在执行的技能，则清理。
     */
    void cleanup();
    
    /**
     * 获取当前正在执行的所有技能的状态值，返回的整数值中每一个二进制位表示一
     * 个技能类型。
     * @return 
     */
    long getRunningSkillStates();
    
    /**
     * 根据给定的skillData找出一个可以用于处理该技能数据(skillData)的技能.
     * @param skillData
     * @return 
     */
    Skill findSkill(SkillData skillData);
    
    /**
     * 执行技能,如果执行成功则返回true,否则返回false,
     * @param skill 要执行的技能
     */
    void playSkill(Skill skill);
    
    /**
     * 当角色缺少某些技能，如“等待”、“死亡”技能时可使用该方法来代替。
     * @return 
     */
    boolean playReset();
    
    /**
     * 执行朝向目标,注意position是目标位置
     * @param position
     * @return 
     */
    boolean playFaceTo(Vector3f position);
    
    /**
     * 获取最近正在执行的技能，如果当前没有任何正在执行的技能，则该方法将返
     * 回null.
     * @return 
     */
    Skill getLastPlayingSkill();
    
    /**
     * 获取最近执行的技能,不管该技能是否正在执行，如果角色没有执行过任何技能，
     * 则该方法将返回null.
     * @return 
     */
    Skill getLastSkill();
    
    /**
     * 根据指定的技能类型获取当前正在执行的技能。注：同时正在执行的技能可能有
     * 多个。找到第一个合适的技能即可,如果没有则返回null.
     * @param skillType
     * @return 
     */
    Skill getPlayingSkill(SkillType skillType);
    
    /**
     * 获取当前正在执行的所有技能，返回值中每一个二进制位表示一个技能类型.
     * 这些技能是正在执行中的，还未结束的技能。
     * @return 
     */
    long getPlayingSkillStates();
    
    /**
     * 判断当前角色是否正在执行任何技能，只要正在执行任何技能则返回true.
     * 否则返回false。技能类型参考：{@link SkillType}
     * @return 
     */
    boolean isPlayingSkill();
    
    /**
     * 判断角色当前是否正在执行指定技能类型的技能。
     * @param skillType
     * @return 
     */
    boolean isPlayingSkill(SkillType skillType);
    
    boolean isWaiting();
    
    boolean isWalking();
    
    boolean isRunning();
    
    boolean isAttacking();
    
    boolean isDefending();
    
    boolean isDucking();
    
    
}
