/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.constants;

/**
 * Message的键值，对应资源文件resource的键值
 * @author huliqing
 */
public class ResConstants {
    
    // -------------------------------------------------------------------------
    // Common
    // -------------------------------------------------------------------------
    
    /** 获得%s经验值 */
    public final static String COMMON_GET_XP = "common.getXp";
    
    /** 获得物品奖励 */
    public final static String COMMON_REWARD_ITEM = "common.rewardItem";
    
    /** LEVELUP 等级提升 */
    public final static String COMMON_LEVEL_UP = "common.levelUp";
    
    /** 获得天赋点数 */
    public final static String COMMON_GET_TALENT = "common.getTalent";
    /** 小提示 */
    public final static String COMMON_TIP = "common.tip";
    /** 确定 */
    public final static String COMMON_CONFIRM = "common.confirm";
    
    /** 需要等级 */
    public final static String COMMON_NEED_LEVEL = "common.needLevel";
    /** 返回并重试 */
    public final static String COMMON_BACK_TO_TRY_AGAIN = "common.backToTryAgain";
    /** 返回并继续 */
    public final static String COMMON_BACK_TO_CONTINUE = "common.backToContinue";
    /** 未知 */
    public final static String COMMON_UNKNOW = "common.unknow";
    /** 等级 */
    public final static String COMMON_LEVEL = "common.level";
    /** 物品名称 */
    public final static String COMMON_NAME = "common.name";
    /** 数量 */
    public final static String COMMON_TOTAL = "common.total";
    /** 价值 */
    public final static String COMMON_COST = "common.cost";
    /** 发送 */
    public final static String COMMON_SEND = "common.send";
    /** 包裹物品 */
    public final static String COMMON_ITEMS = "common.items";
    /** 对话 */
    public final static String COMMON_CHAT = "common.chat";
    /** common.killed */
    public final static String COMMON_KILLED = "common.killed";
    /** common.dead */
    public final static String COMMON_DEAD = "common.dead";
    
    // -------------------------------------------------------------------------
    // Lan
    // -------------------------------------------------------------------------
    
    /** 无法连接到主机 */
    public final static String LAN_SERVER_UNKNOW="lan.serverUnknow";
    
    /** 客户端退出 */
    public final static String LAN_CLIENT_EXISTS="lan.leaveGame";
    
    // -------------------------------------------------------------------------
    // Talent
    // -------------------------------------------------------------------------
    
    /** 剩余多少天赋点数 */
    public final static String TALENT_TALENT_POINTS_REMAIN = "talent.talentPointsRemain";
    
    // -------------------------------------------------------------------------
    // skill 
    // -------------------------------------------------------------------------
    
    /** 技能未定义错误 */
    public final static String SKILL_UNDEFINE = "skill.undefine";
    
    /** 技能不存在或找不到指定技能 */
    public final static String SKILL_NOT_FOUND = "skill.notFound";
    
    /** 角色的技能处于锁定状态 */
    public final static String SKILL_LOCKED = "skill.locked";
    
    /** 技能正处于冷却状态 */
    public final static String SKILL_COOLDOWN = "skill.cooldown";
    
    /** 角色所使用的当前武器不能执行该技能 */
    public final static String SKILL_WEAPON_NOT_ALLOW = "skill.weaponNotAllow";
    
    /** 角色武器没有取出，必须先取出武器 */
    public final static String SKILL_WEAPON_NEED_TAKE_ON = "skill.needTakeOn";
    
    /** 技能所需要的能量不足，如魔法值等，但不一定只表示魔法值不足。 */
    public final static String SKILL_MANA_NOT_ENOUGH = "skill.manaNotEnough";
    
    /** 没有目标，技能需要目标才能执行，而当前没有目标 */
    public final static String SKILL_TARGET_NOT_FOUND = "skill.targetNotFound";
    
    /** 目标不在技能的射程范围内 */
    public final static String SKILL_TARGET_NOT_IN_RANGE = "skill.targetNotInRange";
    
    /** 不能对指定目标使用该技能 */
    public final static String SKILL_TARGET_UNSUITABLE = "skill.targetUnsuitable";
    
    /** 需要指定的技能，带一个参数%:技能名称 */
    public final static String SKILL_NEED_SKILL = "skill.needSkill";
    
    /** 技能已经学会 */
    public final static String SKILL_SKILL_LEARNED = "skill.skillLearned";
    
    /** 学习指定技能,带一个参数 */
    public final static String SKILL_LEARN_SKILL = "skill.learnSkill";
    
    /** 技能等级提升，带一个数数: 技能名称 */
    public final static String SKILL_LEVEL_UP = "skill.levelUp";
    
    /** 需要达到指定等级才能使用技能 */
    public final static String SKILL_NEED_LEVEL = "skill.needLevel";
    
    // -------------------------------------------------------------------------
    // Chat
    // -------------------------------------------------------------------------
    /** 对话 */
    public final static String CHAT_TITLE = "chat.title";
    /** 库存 */ 
    public final static String CHAT_SHOP_STOCK = "chat.shop.stock";
    /** 售价 */ 
    public final static String CHAT_SHOP_PRICE = "chat.shop.price";
    /** 估价 */
    public final static String CHAT_SHOP_ASSESS = "chat.shop.assess";
    /** 购买 */
    public final static String CHAT_SHOP_BUY = "chat.shop.buy";
    /** 折扣 */
    public final static String CHAT_SHOP_DISCOUNT = "chat.shop.discount";
    /** 剩余金币 */
    public final static String CHAT_SHOP_GOLDS_REMAIN="chat.shop.goldsRemain";
    /** 待售物品 */
    public final static String CHAT_SHOP_SELL_ITEMS="chat.shop.sellItems";
    /** 确认数量 */
    public final static String CHAT_SHOP_CONFIRM_COUNT="chat.shop.confirmCount";
    /** 确认售出 */
    public final static String CHAT_SHOP_CONFIRM_SELL="chat.shop.confirmSell";
    /** 金币不足 */
    public final static String CHAT_SHOP_WARN_GOLD_NOT_ENOUGH = "chat.shop.warn.goldNotEnough";
    /** 库存不足 */
    public final static String CHAT_SHOP_WARN_PRODUCT_NOT_ENOUGH = "chat.shop.warn.productNotEnough";
    
    // -------------------------------------------------------------------------
    // Task
    // -------------------------------------------------------------------------
    /** 任务 */
    public final static String TASK_TASK = "task.task";
    /** 接受任务 */
    public final static String TASK_ACCEPT = "task.accept";
    /** 拒绝任务 */
    public final static String TASK_REJECT = "task.reject";
    /** 任务完成*/
    public final static String TASK_SUCCESS = "task.success";
    /** 任务失败 */
    public final static String TASK_FAILURE = "task.failure";
    /** 任务报酬 */
    public final static String TASK_REWARD = "task.reward";
    /** 任务进度 */
    public final static String TASK_PROGRESS = "task.progress";
    /** 只显示进行中的任务 */
    public final static String TASK_FILTER = "task.filter";
    /** 点击任务查看详情 */
    public final static String TASK_TIPS = "task.tips";
    /** 获得任务物品 */
    public final static String TASK_GET_TASK_ITEM = "task.getTaskItem";
    /** 已经做过这个任务 */
    public final static String TASK_COMPLETED = "task.completed";
    /** 已结束 */
    public final static String TASK_OVER = "task.over";
    /** 没有接过任务 */
    public final static String TASK_NO_TASKS = "task.noTasks";
    
    
}
