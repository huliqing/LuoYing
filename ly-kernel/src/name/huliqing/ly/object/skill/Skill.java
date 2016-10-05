/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import name.huliqing.ly.constants.SkillConstants;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.xml.DataProcessor;

/**
 * 接能接口
 * @author huliqing
 */
public interface Skill extends DataProcessor<SkillData>{
    
    /**
     * 开始执行技能
     */
    void initialize();
    
    /**
     * 判断技能是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新技能逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理技能产生的数据以释放资源,一般只处理内部数据，不要再去调用service之类
     * 以避免循环调用。
     */
    void cleanup();
    
    /**
     * 设置发起技能的角色。
     * @param actor 
     */
    void setActor(Actor actor);
    
    /**
     * 获取技能的执行角色
     * @return 
     */
    Actor getActor();
    
    /**
     * 重新修复被其它技能重置的动画
     */
    void restoreAnimation();
    
    /**
     * 获取技能的实际执行时间.
     * @return 
     */
    float getTrueUseTime();
        
    /**
     * 判断角色在当前状态下是否可以执行这个技能,该方法返回一个状态码{@link SkillConstants}，<br>
     * 来判断当前技能是否可以执行，当前技能是否可以执行会受到技能的各种约束限制，<br>
     * 如：技能要求角色持有特定武器才可以执行或者技能处于冷却中，或者技能要求消耗一些属性值而角色当前的属性值不足等。
     * @return 
     * @see SkillConstants
     */
    int checkState();
    
    /**
     * 判断当前状态下，技能是否可以被另一个技能打断.这个方法由SkillModule调用，
     * 主要用于判断当前正在执行的技能，是否可以被另一个正准备执行的技能打断。
     * 这个功能可以允许一些防守被强制打断的技能出现，比如对于一些特殊技能，
     * 如魔法技能在施法的时候可以根据概率来计算是否允许被打断。
     * @param newSkill
     * @return 
     */
    boolean canInterruptBySkill(Skill newSkill);
    
    /**
     * 判断技能是否正常结束或未启动
     * @return 
     */
    boolean isEnd();
    
    /**
     * 技能是否处于冷却中
     * @return 
     */
    boolean isCooldown();
    
    /**
     * 判断在指定的武器状态下是否可以执行这个技能, 一些技能在使用的时候会要求角色必须使用指定的武器,
     * 如果技能没有任何武器限制则该方法应该返回true,否则根据角色当前的武器状态来判断是否可以使用这个技能。
     * @return 
     */
    boolean isPlayableByWeapon();

    /**
     * 判断角色在当前的等级下是否可以使用这个技能，一些技能在使用的时候会有等级限制，如果角色的当前等级不足以使用
     * 这个技能，则该方法应该返回false, 否则返回true
     * @return 
     */
    boolean isPlayableByLevelLimit();

    /**
     * 判断角色当前的属性状态是否可以使用这个技能，有些技能在使用的时候会要求消耗角色的一些属性值，
     * 比如：魔法值、怒气值...等, 这个方法用于判断角色当前的各种属性的值是否满使用这个技能。
     * 如果技能没有任何属性限制则这个方法应该始终返回true.
     * @return 
     */
    boolean isPlayableByAttributeLimit();
}

