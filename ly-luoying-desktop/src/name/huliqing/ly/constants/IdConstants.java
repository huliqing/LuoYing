/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.constants;

/**
 *
 * @author huliqing
 */
public class IdConstants {
    
    /** 野兽召唤术 */
    public final static String ITEM_BOOK_005 = "itemBook005";
    
    /** 闪电箭 */
    public final static String ITEM_BOOK_006 = "itemBook006";
    /** 星光传送术 */
    public final static String ITEM_BOOK_007 = "itemBook007";
    
    /** 金币的ID */
    public final static String ITEM_GOLD = "itemGold";
    /** 古柏的树根 */
    public final static String ITEM_GB_STUMP = "itemTreeStump";
    /** 生命药水 */
    public final static String ITEM_TONIC_HEALTH = "itemTonicRed";
    /** 魔法药水 */
    public final static String ITEM_TONIC_MANA = "itemTonicBlue";
    /** 火防御塔 */
    public final static String ITEM_TOWER_FIRE = "itemTower";

    // =========================================================================
    // Actor
    // =========================================================================
    
    /** 玩家角色 */
    public final static String ACTOR_PLAYER = "actorPlayer";
    /** 玩家测试角色 */
    public final static String ACTOR_PLAYER_TEST = "actorPlayerTest";
    /** 角色：困难 */
    public final static String ACTOR_HARD = "actorHard";
    /** 角色：艾琳 */
    public final static String ACTOR_AILIN = "actorAiLin";
    /** 角色：蒂娜 */
    public final static String ACTOR_DINA = "actorDiNa";
    /** 妖精角色 */
    public final static String ACTOR_FAIRY = "actorFairy";
    /** 宝箱 */
    public final static String ACTOR_TREASURE = "actorTreasure";
    /** 蜘蛛 */
    public final static String ACTOR_SPIDER = "actorSpider";
    /** 忍者 */
    public final static String ACTOR_NINJA = "actorNinja";
    /** wolf */
    public final static String ACTOR_WOLF = "actorWolf";
    /** bear */
    public final static String ACTOR_BEAR = "actorBear";
    /** scorpion */
    public final static String ACTOR_SCORPION = "actorScorpion";
    /** scorpion big */
    public final static String ACTOR_SCORPION_BIG = "actorScorpionBig";
    /** 古柏 */
    public final static String ACTOR_GB = "actorGb";
    /** 古柏子孙 */
    public final static String ACTOR_GB_SMALL = "actorGbSmall";
    /** 黑暗祭坛 */
    public final static String ACTOR_ALTAR = "actorAltar";
    /** 防御塔－火 */
    public final static String ACTOR_TOWER = "actorTower";
    /** 防御塔－石 */
    public final static String ACTOR_TOWER_STONE = "actorTowerStone";
    /** Sinbad */
    public final static String ACTOR_SINBAD = "actorSinbad";
    /** actorTrex */
    public final static String ACTOR_TREX = "actorTrex";
    /** actorRaptor */
    public final static String ACTOR_RAPTOR = "actorRaptor";
    /** actorJaime */
    public final static String ACTOR_JAIME = "actorJaime";
    
    // =========================================================================
    // Action
    // =========================================================================
    
    /** 简单的走路行为,无寻路 */
    public final static String ACTION_RUN_SIMPLE = "actionSimpleRun";
    /** 带有寻路功能的“走路”行为 */
    public final static String ACTION_RUN_BY_PATH = "actionRun";
    /** 动态的战斗行为 */
    public final static String ACTION_FIGHT_DYNAMIC = "actionDynamicFight";
    /** 静态的战斗行为 */
    public final static String ACTION_FIGHT_STATIC = "actionStaticFight";
    
    // =========================================================================
    // Sound
    // =========================================================================
    
    /** 获得金币时的声效 */
    public final static String SOUND_GET_COIN = "soundCoin1";
    /** 金币声音(小) */
    public final static String SOUND_COIN1 = "soundCoin2";
    /** 金币声音(大) */
    public final static String SOUND_COIN2 = "soundCoin3";
    /** 获得普通物品时的声效 */
    public final static String SOUND_GET_ITEM = "soundNote";
    
    // =========================================================================
    // Handler
    // =========================================================================
    
    /** 默认的用于处理技能执行的handler */
    public final static String HANDLER_SKILL = "handlerSkill";
    
    // =========================================================================
    // Logic
    // =========================================================================
    
    /** 默认的搜索敌人的逻辑设置 */
    public final static String LOGIC_SEARCH_ENEMY = "logicSearchEnemy";
    /** 跟随目标的行为逻辑 */
    public final static String LOGIC_FOLLOW = "logicFollow";
    /** 向目标位置移动的逻辑 */
    public final static String LOGIC_POSITION = "logicPosition";
    /** 防御逻辑 */
    public final static String LOGIC_DEFEND = "logicDefend";
    /** 打架逻辑 */
    public final static String LOGIC_FIGHT = "logicDynamicFight";
    
    // =========================================================================
    // Scene
    // =========================================================================
    
    /** 场景：黄金宝箱 */
    public final static String SCENE_TREASURE = "sceneTreasure";
    
    // =========================================================================
    // Game
    // =========================================================================

    /** 游戏：黄金宝箱 */
    public final static String GAME_STORY_TREASURE = "gameStoryTreasure";
    /** 游戏：千年古柏 */
    public final static String GAME_STORY_GB = "gameStoryGb";
    /** 游戏：守护之战 */
    public final static String GAME_STORY_GUARD = "gameStoryGuard";
    /** 游戏: 测试游戏 */
    public final static String GAME_LAB = "gameLab";
    
    // =========================================================================
    // Channel
    // =========================================================================
    
    /** 默认的包含完全骨骼的动画通道 */
    public final static String CHANNEL_FULL = "channelFull";
    
    // =========================================================================
    // Skill
    // =========================================================================
    
    /**
     * @deprecated 
     * 技能：回城技能 
     */
    public final static String SKILL_BACK = "skillBack";
    /** 
     * @deprecated 
     * 技能：默认的Reset技能，适应所有没有设置Reset技能的角色 
     */
    public final static String SKILL_RESET_DEFAULT = "skillResetBase";
    /** @deprecated 不再使用 */
    public final static String SKILL_SUMMON = "skillSummon";
    
    // =========================================================================
    // Attribute
    // =========================================================================
    
    /** 默认的魔法条属性id */
    public final static String ATTRIBUTE_MANA = "attributeMana";
    
    // =========================================================================
    // State
    // =========================================================================
    
    /** 保护状态，该状态可以让角色的生命值，物理防御值和魔法防御力大增 */
    public final static String STATE_SAFE = "stateSafe";
    
    // =========================================================================
    // Drop
    // =========================================================================
    
    /** 什么也不掉的drop配置ID */
    public final static String DROP_EMPTY = "dropEmpty";
    
    // =========================================================================
    // Effect
    // =========================================================================
    
    /** 升级时的效果 */
    public final static String EFFECT_LEVEL_UP = "effectLevelUp";
    
    // =========================================================================
    // Emitter
    // =========================================================================
    
    /** emitterSakura */
    public final static String EMITTER_SAKURA = "emitterSakura";
    
    // =========================================================================
    // Talent
    // =========================================================================
    
    public final static String TALENT_ATTACK = "talentAttack";
    public final static String TALENT_DEFENCE = "talentDefence";
    public final static String TALENT_DEFENCE_MAGIC = "talentMagicDefence";
    public final static String TALENT_LIFE_RESTORE = "talentLifeRestore";
    public final static String TALENT_MOVE_SPEED = "talentMoveSpeed";
    
    // =========================================================================
    // View
    // =========================================================================
    public final static String VIEW_WARN = "viewWarn";
    /** 任务成功 */
    public final static String VIEW_TEXT_SUCCESS = "viewTaskSuccess";
    /** 任务失败 */
    public final static String VIEW_TEXT_FAILURE = "viewTaskFailure";
    /** "黄金宝箱" 计时器 */
    public final static String VIEW_TIMER = "viewTimer";
    /** "千年古柏" 树根任务计数面板 */
    public final static String VIEW_TEXT_PANEL_VIEW_GB = "viewTPGb";
    
    // =========================================================================
    // Anim
    // =========================================================================
    
    /** 默认的View出现动画 */
    public final static String ANIM_VIEW_MOVE = "animViewMove";
    
    /** 古柏树根 */
    public final static String DROP_TREE_STUMP = "dropTreeStump";
    
    /** 掉落：星光传送术 */
    public final static String DROP_BOOK_007 = "dropBook007";
}
