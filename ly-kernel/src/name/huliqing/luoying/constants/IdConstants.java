/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    /** 默认的关于技能标记定义的配置id */
    public final static String SYS_DEFINE_SKILL_TAG = "sysDefineSkillTag";
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
    // HitChecker
    // =============================================================================
    
    // remove20161031
//    public final static String SYS_HITCHECK_FIGHT_DEFAULT = "sysHitCheckerFightDefault";
    
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
    // Entity: Env
    // =============================================================================
    
    /** 内置的天空盒物体: sysEnvSky */
    public final static String SYS_ENV_SKY = "sysEnvSky";
    /** 内置直射光物体：sysEnvDirectionalLight */
    public final static String SYS_ENV_DIRECTIONAL_LIGHT = "sysEnvDirectionalLight";
    /** 内置环境光物体: sysEnvAmbientLight */
    public final static String SYS_ENV_AMBIENT_LIGHT = "sysEnvAmbientLight";
    
    // =============================================================================
    // El
    // =============================================================================
    
    /** sysElCheckEmpty, 空的CheckEl, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用*/
    public final static String SYS_EL_CHECK = "sysElCheckEmpty";
    /** sysElHitCheckEmpty, 空的HitCheckEl, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用 */
    public final static String SYS_EL_HIT_CHECK_EMPTY = "sysElHitCheckEmpty";
    /** sysElHitEmpty, 空的HitEl，用于动态创建表达式用, 载入这个后必须设置expression才可以使用 */
    public final static String SYS_EL_HIT_EMPTY = "sysElHitEmpty";
    /** sysElLevelEmpty, 空的LevelEl, 用于动态创建表达式用, 载入这个后必须设置expression才可以使用  */
    public final static String SYS_EL_LEVEL_EMPTY = "sysElLevelEmpty";
    
}
