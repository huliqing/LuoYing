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
package name.huliqing.luoying.constants;

/**
 * 这些定义的ID应该与item.xml中定义的ID相对应
 * @author huliqing
 */
public class IdConstants {
    
    // =============================================================================
    // Config
    // =============================================================================
    
    /** 全局配置 */
    public final static String SYS_CONFIG = "sysConfig";
    
    // =============================================================================
    // Define
    // =============================================================================
    
    /** 默认的关于技能类型定义的配置id */
    public final static String SYS_DEFINE_SKILL_TYPE = "sysDefineSkillType";
    /** 默认的关于装备部位定义的配置id */
    public final static String SYS_DEFINE_SKIN_PART = "sysDefineSkinPart";
    /** 默认的关于武器类型定义的配置id */
    public final static String SYS_DEFINE_WEAPON_TYPE = "sysDefineWeaponType";
    /** 默认的关于物体构成成分定义的配置id */
    public final static String SYS_DEFINE_MAT = "sysDefineMat";
    
    // =============================================================================
    // Action
    // =============================================================================
    
    /** 内置的跑步行为 */
    public final static String SYS_ACTION_SIMPLE_RUN = "sysActionSimpleRun";
    /** 内置的跟随行为 */
    public final static String SYS_ACTION_SIMPLE_FOLLOW = "sysActionSimpleFollow";
    /** 内置的战斗行为 */
    public final static String SYS_ACTION_SIMPLE_FIGHT = "sysActionSimpleFight";
    
    // =============================================================================
    // Channel
    // =============================================================================
    
    /** 内置的骨骼通道，这个通道作为所有<b>未配置</b>分通道的角色的默认通道 */
    public final static String SYS_CHANNEL_FULL = "sysChannelFull";
    
    // =============================================================================
    // Game
    // =============================================================================
    
    /** 默认的游戏及场景，无任何场景物体，需要自己添加 */
    public final static String SYS_GAME = "sysGame";
    
    /** 测试用的场景 */
    public final static String SYS_GAME_TEST = "sysGameTest";
    
    // =============================================================================
    // Scene 
    // =============================================================================
    
    /** 默认的游戏场景，无任何场景物体 */
    public final static String SYS_SCENE = "sysScene";
    /** 默认的GUI场景，无任何内容 */
    public final static String SYS_SCENE_GUI = "sysSceneGui";
    /** 默认的测试场景 */
    public final static String SYS_SCENE_TEST = "sysSceneTest";
    
    // =============================================================================
    // Scene Progress
    // =============================================================================

    /** 简单的场景载入器 */
    public final static String SYS_PROGRESS_SIMPLE = "sysProgressSimple";
    
    // =============================================================================
    // Entity: Env
    // =============================================================================
    
    /** 内置的天空盒物体: sysEntitySky */
    public final static String SYS_ENTITY_SKY = "sysEntitySky";
    /** 内置直射光物体：sysEntityDirectionalLight */
    public final static String SYS_ENTITY_DIRECTIONAL_LIGHT = "sysEntityDirectionalLight";
    /** 内置环境光物体: sysEntityAmbientLight */
    public final static String SYS_ENTITY_AMBIENT_LIGHT = "sysEntityAmbientLight";
    /** 普通地形只有一个Box：sysEntityTerrain */
    public final static String SYS_ENTITY_TERRAIN = "sysEntityTerrain";
    /** sysEntityTree */
    public final static String SYS_ENTITY_TREE = "sysEntityTree";
    /** sysEntityPhysics */
    public final static String SYS_ENTITY_PHYSICS = "sysEntityPhysics";
    /** sysEntityShadow */
    public final static String SYS_ENTITY_SHADOW = "sysEntityShadow";
    /** sysEntityChaseCamera */
    public final static String SYS_ENTITY_CHASE_CAMERA = "sysEntityChaseCamera";
    /** sysEntitySimpleWater */
    public final static String SYS_ENTITY_SIMPLE_WATER = "sysEntitySimpleWater";
    /** sysEntityAdvanceWater */
    public final static String SYS_ENTITY_ADVANCE_WATER = "sysEntityAdvanceWater";
    /** sysEntityBoundary */
    public final static String SYS_ENTITY_BOUNDARY = "sysEntityBoundary";
    /** sysEntityAudio */
    public final static String SYS_ENTITY_AUDIO = "sysEntityAudio";
    /** sysEntityModel */
    public final static String SYS_ENTITY_MODEL = "sysEntityModel";
    /**  sysEntityEffect, 效果实体，空的，没有任何效果，需要动态设置。 */
    public final static String SYS_ENTITY_EFFECT = "sysEntityEffect";
    
    // =============================================================================
    // El
    // =============================================================================
    
    /** sysElCustom, 空表达式, 用于动态创建表达式用, 载入这个后必须设置expression,并根据表达式设置参数值后才可以计算 */
    public final static String SYS_EL_CUSTOM  = "sysElCustom";
    /** sysElSBoolean, 空表达式, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用*/
    public final static String SYS_EL_SBOOLEAN  = "sysElSBoolean";
    /** sysElLNumber, 空表达式, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用  */
    public final static String SYS_EL_LNUMBER    = "sysElLNumber";
    /** sysElSTBoolean, 空表达式, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用 */
    public final static String SYS_EL_STBOOLEAN = "sysElSTBoolean";
    /** sysElSTNumber, 空表达式，用于动态创建表达式用, 载入这个后必须设置expression才可以使用 */
    public final static String SYS_EL_STNUMBER  = "sysElSTNumber";
    /** sysElSkillHitNumber,  空表达式，用于动态创建表达式用, 载入这个后必须设置expression才可以使用*/
    public final static String SYS_EL_SKILLHITNUMBER  = "sysElSkillHitNumber";
    
    // =============================================================================
    // DelayAnim
    // =============================================================================
    
    /** 自定义的数据类型_sysCustomAnimDelay_ */
    public final static String SYS_CUSTOM_ANIM_DELAY = "_sysCustomAnimDelay_";

    // =============================================================================
    // Anim
    // =============================================================================
    /** 曲线移动动画： sysAnimCurveMove */
    public final static String SYS_ANIM_CURVE_MOVE = "sysAnimCurveMove";
    
    /** 插值旋转动画：sysAnimInterpolateRotation */
    public final static String SYS_ANIM_INTERPOLATE_ROTATION = "sysAnimInterpolateRotation";
    
    // =============================================================================
    // Sound
    // =============================================================================
    /** 声音 */
    public final static String SYS_SOUND = "sysSound";
    

    
}
