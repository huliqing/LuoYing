/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.log;

/**
 *
 * @author huliqing
 */
public final class StateCode {
    
    // =============================================================================
    // 通用消息状态码
    // =============================================================================
    
    /** 消息状态OK。 */
    public final static int OK = 0;
    
    /** 未定义的错误状态。 */
    public final static int UNDEFINE = -1;
    
    // =============================================================================
    // 物品类消息状态码
    // =============================================================================
    
    /** 使用物品时，物品数量不足 */
    public final static int ITEM_NOT_ENOUGH = 002;
    
    /** 使用物品、书籍学习技能时，技能已经存在 */
    public final static int ITEM_SKILL_EXISTS = 003; 
    
    /** 使用物品时无法通过CheckEl检查 */
    public final static int ITEM_CHECK_EL = 004;
    
    // =============================================================================
    // 技能类消息状态码
    // =============================================================================
    
    /** 技能不存在或找不到指定技能 */
    public final static int SKILL_NOT_FOUND = 101;
    
    /** 角色的技能处于锁定状态 */
    public final static int SKILL_LOCKED = 102;
    
    /** 技能正处于冷却状态 */
    public final static int SKILL_COOLDOWN = 103;
    
    /** 角色所使用的当前武器不能执行该技能 */
    public final static int SKILL_WEAPON_NOT_ALLOW = 104;
    
    /** 角色武器没有取出，必须先取出武器 */
    public final static int SKILL_WEAPON_NEED_TAKE_ON = 105;
    
     /** 执行技能时，需要消耗角色的一些属性值，而角色的属性值不足. */
    public final static int SKILL_ATTRIBUTE_NOT_ENOUGH = 106;
    
    /** 技能使用时，ELCheck检查器检测到不能使用指定技能。 */
    public final static int SKILL_ELCHECK = 107;
    
    /** 没有目标，技能需要目标才能执行，而当前没有目标 */
    public final static int SKILL_TARGET_NOT_FOUND = 108;
    
    /** 目标不在技能的射程范围内 */
    public final static int SKILL_TARGET_OUT_OF_RANGE = 109;
    
    /** 不能对指定目标使用该技能(通过El检查器） */
    public final static int SKILL_TARGET_UNSUITABLE_BY_ELCHECK = 110;
    
    /** 不能中断正在执行的技能 */
    public final static int SKILL_CAN_NOT_INTERRUPT = 111;
    
    /** 某些侦听器钩子不允许执行技能，比如一些状态效果会侦听角色技能的执行，并限制技能的执行。*/
    public final static int SKILL_HOOK = 112;
    
    /** 角色自身已经死亡 */
    public final static int SKILL_DEAD = 113;
    
}
