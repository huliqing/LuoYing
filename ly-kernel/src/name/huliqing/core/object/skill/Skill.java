/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.constants.SkillConstants;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.xml.DataProcessor;

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
     * 获取技能的实际执行时间,技能的实际执行时间受：技能总时间、技能执行速度、
     * 技能的剪裁时间等影响
     * @return 
     */
    float getTrueUseTime();
        
    /**
     * 判断角色在当前状态下是否可以执行这个技能,该方法返回一个状态码{@link SkillConstants}，来判断当前技能是否可以执行.
     * @return 
     * @see SkillConstants
     */
    int checkState();

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

