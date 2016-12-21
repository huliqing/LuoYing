/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.message;

/**
 *
 * @author huliqing
 */
public final class StateCode {
    
    // =============================================================================
    // 通用消息状态码
    // =============================================================================
    
    /** 未定义的错误状态 */
    public final static int UNDEFINE = -1;
    
    // =============================================================================
    // 通用状态码：物品添加、移除、使用
    // =============================================================================
    
    /** 添加物品 */
    public final static int DATA_ADD = 100;
    /** 不能添加物品 */
    public final static int DATA_ADD_FAILURE = 101;
    /** 不能添加物品：因为物品已经存在，例如技能不能重复添加 */
    public final static int DATA_ADD_FAILURE_DATA_EXISTS = 102;
    
    
    /** 物体可以移除\物体移除成功 */
    public final static int DATA_REMOVE = 900;
    /** 物体不能移除 */
    public final static int DATA_REMOVE_FAILURE = 901;
    /** 物体不能移除：因找不到物体 */
    public final static int DATA_REMOVE_FAILURE_NOT_FOUND = 902;
    /** 物体不能移除：因为物体不允许删除，例如，任务物品等特殊类型的物品 */
    public final static int DATA_REMOVE_FAILURE_UN_DELETABLE = 903;
    /** 物体不能移除：因为物体正在使用中，例如穿在身上的装备，可能不允许删除 */
    public final static int DATA_REMOVE_FAILURE_IN_USING = 904;
    
    /** 物体可以使用 */
    public final static int DATA_USE = 200;
    /** 物体不能使用 */
    public final static int DATA_USE_FAILURE = 201;
    /** 物体不能使用：因物体不存在 */
    public final static int DATA_USE_FAILURE_NOT_FOUND = 202;
    /** 物体不能使用：因为数量不足 */
    public final static int DATA_USE_FAILURE_NOT_ENOUGH = 203;
    /** 物体不能使用：无法通过El检查的检查验证 */
    public final static int DATA_USE_FAILURE_CHECK_EL = 204;
    
    // =============================================================================
    // Skill Module: 技能模块状态码
    // =============================================================================
    
    /** 技能可以正常执行 */
    public final static int SKILL_USE_OK = 300;
    
    /** 技能不能使用(原因不确定) */
    public final static int SKILL_USE_FAILURE = 301;
    
    /**不能执行技能： 技能不存在或找不到指定技能 */
    public final static int SKILL_USE_FAILURE_NOT_FOUND = 302;
    
    /** 不能执行技能：角色的技能处于锁定状态 */
    public final static int SKILL_USE_FAILURE_LOCKED = 303;
    
    /** 不能执行技能：技能正处于冷却状态 */
    public final static int SKILL_USE_FAILURE_COOLDOWN = 304;
    
    /** 不能执行技能：角色所使用的当前武器不能执行该技能 */
    public final static int SKILL_USE_FAILURE_WEAPON_NOT_ALLOW = 305;
    
    /** 不能执行技能：角色武器没有取出，必须先取出武器 */
    public final static int SKILL_USE_FAILURE_WEAPON_NEED_TAKE_ON = 306;
    
    /** 不能执行技能：执行技能时，需要消耗角色的一些属性值，而角色的属性值不足 */
    public final static int SKILL_USE_FAILURE_ATTRIBUTE_NOT_ENOUGH = 307;
    
    /** 不能执行技能：技能使用时，ELCheck检查器检测到不能使用指定技能。 */
    public final static int SKILL_USE_FAILURE_ELCHECK = 308;
    
    /** 不能执行技能：没有目标，技能需要目标才能执行，而当前没有目标 */
    public final static int SKILL_USE_FAILURE_TARGET_NOT_FOUND = 309;
    
    /** 不能执行技能：目标不在技能的射程范围内 */
    public final static int SKILL_USE_FAILURE_TARGET_OUT_OF_RANGE = 310;
    
    /** 不能执行技能：不能对指定目标使用该技能(通过El检查器） */
    public final static int SKILL_USE_FAILURE_TARGET_UNSUITABLE_BY_ELCHECK = 311;
    
    /** 不能执行技能：不能中断正在执行的技能 */
    public final static int SKILL_USE_FAILURE_CAN_NOT_INTERRUPT = 312;
    
    /** 不能执行技能：某些侦听器钩子不允许执行技能，比如一些状态效果会侦听角色技能的执行，并限制技能的执行。*/
    public final static int SKILL_USE_FAILURE_BY_HOOK = 313;
    
    /** 不能执行技能： 因为角色已经死亡 */
    public final static int SKILL_USE_FAILURE_ACTOR_DEAD = 314;
    
    // =============================================================================
    // Level Module
    // =============================================================================
    
    /** 等级提升 */
    public final static int LEVEL_UP = 400;
        
    // =============================================================================
    // Talent Module
    // =============================================================================
    
    /** 获得天赋点数 */
    public final static int TALENT_POINTS_ADDED = 500;
}
