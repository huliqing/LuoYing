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
     * 重新修复被其它技能重置的动画
     * add20151212
     */
    void restoreAnimation();
    
    /**
     * 判断角色在当前状态下是否可以执行这个技能,该方法返回一个状态码{@link SkillConstants}，来判断当前技能是否可以执行.
     * @return 
     * @see SkillConstants
     */
    int checkState();
    
    /**
     * 判断在指定的武器状态下是否可以执行这个技能。这个方法即用来判断使用指定的武器类型是否可以执行这个技能。
     * weaponState包含了指定的武器类型。
     * @param weaponState
     * @return 
     */
    boolean isPlayable(long weaponState);
    
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
    
//    /**
//     * 获取技能的CutTimeEndRate,这个值是对技能执行时间的剪裁，即对技能的结束阶段
//     * 的时间进行剪裁，这个值受角色属性影响，并且不会大于CutTimeEndMax.
//     * 如果技能没有指定影响该值的角色属性，或者角色没有指定的属性值，则这个值应
//     * 返回0.<br >
//     * 注：这个值返回的是一个比率，取值为[0.0,1.0]之间，即表示要剪裁掉的技能总时间
//     * 的比率。例如：当返回值为0.5时，即表示技能的总执行时间要剪裁掉一半（时间的后半部分）
//     * @return 
//     */
//    float getCutTimeEndRate();

    /**
     * 获取技能的实际执行时间,技能的实际执行时间受：技能总时间、技能执行速度、
     * 技能的剪裁时间等影响
     * @return 
     */
    float getTrueUseTime();
}

