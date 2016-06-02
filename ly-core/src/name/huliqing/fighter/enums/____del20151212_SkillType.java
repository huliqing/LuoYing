///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.enums;
//
///**
// * 定义技能类型
// * @author huliqing
// */
//public enum SkillType {
//    
//    /** 等待 */
//    idle_wait(10),
//    /** 坐下 */
//    idle_sit(11),
//    /** 走路 */
//    idle_walk(12),
//    /** 跑步 */
//    idle_run(13),
//    /** 跳跃 */
//    idle_jump(14),
//    /** 默认的基本的IDLE行为,所有未分类的idle行为 */
//    idle_base(15),
//    
//    /** 死亡 */
//    hurt_dead(20),
//    /** 前面受伤 */
//    hurt_front(21),
//    /** 后面受伤 */
//    hurt_back(22),
//    /** 左边受伤 */
//    hurt_left(23),
//    /** 右边受伤 */
//    hurt_right(24),
//    
//    /** 向左闪避 */
//    duck_left(30),
//    /** 向右闪避 */
//    duck_right(31),
//    /** 向前闪避 */
//    duck_front(32),
//    /** 向后闪避 */
//    duck_back(33),
//    
//    /** 防守左边,即敌人从左边攻击过来 */
//    defend_left(40),
//    /** 防守右边,即敌人从右边攻击过来 */
//    defend_right(41),
//    /** 防守前面,即敌人从前面攻击过来 */
//    defend_front(42),
//    /** 防守后面,即敌人从后面攻击过来 */
//    defend_back(43),
//    
//    /** 准备攻击 */
//    ready(50),
//    
//    /**战斗类技能**************************************************************/
//    
//    /**
//     * 普通攻击： 主要是近战攻击
//     */
//    attack_common(60),
//    
//    /**
//     * 攻击:特技技能，技能攻击
//     */
//    attack_trick(61),
//    
//    /**
//     * // TODO: 考虑是否把attack_shot合并到attack_common
//     * 普通攻击：射击，注：射击类也属于普通类攻击，但容易被打断
//     */
//    attack_shot(62),
//    
//    // 非战斗类魔法技能********************************************************/
//    
//    /**
//     * TODO: 考虑将该类型合并到独立的magic类型中
//     * 召唤技能 
//     */
//    magic_summon(70),
//    
//    /** 
//     * TODO: 考虑将该类型合并到独立的magic类型中
//     * 回城技能
//     */
//    magic_back(71),
//    
//    /** 换装,换武器技能 */
//    skin(80),
//    
//    /** 让角色处于reset状态的技能，如处于晕眩状态时的技能 */
//    reset(90),
//    
//    ;
//    
//    private int value;
//    
//    private SkillType(int value) {
//        this.value = value;
//    }
//
//    public int getValue() {
//        return value;
//    }
//        
//    public static SkillType identify(int value) {
//       SkillType[] pts = SkillType.values();
//       for (SkillType pt : pts) {
//           if (pt.getValue() == value) {
//               return pt;
//           }
//       }
//       throw new UnsupportedOperationException("不支持的Pose类型:" + value);
//    }
//    
//    /**
//     * 判断是否为idle行为，包含：wait,sit,walk,run,jump,idle(base)
//     * @param st
//     * @return 
//     */
//    public static boolean isIdle(SkillType st) {
//        return st.getValue() >= 10 && st.getValue() <= 19;
//    }
//    
//    /**
//     * 判断是否为基本idle行为，idle value=15 SkillType=idle
//     * @param st
//     * @return 
//     */
//    public static boolean isIdleBase(SkillType st) {
//        return st == idle_base;
//    }
//    
//    public static boolean isHurt(SkillType st) {
//        return st.getValue() >= 20 && st.getValue() <= 29;
//    }
//    
//    public static boolean isDuck(SkillType st) {
//        return st.getValue() >= 30 && st.getValue() <= 39;
//    }
//    
//    public static boolean isDefend(SkillType st) {
//        return st.getValue() >= 40 && st.getValue() <= 49;
//    }
//    
//    public static boolean isReady(SkillType st) {
//        return st.getValue() == 50;
//    }
//    
//    /**
//     * 判断是否为攻击类技能:攻击类技能包含普通攻击技、特技攻击、射击、魔法攻击
//     * @param st
//     * @return 
//     */
//    public static boolean isAttack(SkillType st) {
//        return st.getValue() >= 60 && st.getValue() <= 69;
//    }
//    
//    /**
//     * 判断是否为普通魔法技能，这类魔法不是攻击技能。
//     * @param st
//     * @return 
//     */
//    public static boolean isMagic(SkillType st) {
//        return st.getValue() >= 70 && st.getValue() <= 79;
//    }
//    
//    /**
//     * reset技能
//     * @param st
//     * @return 
//     */
//    public static boolean isReset(SkillType st) {
//        return st == SkillType.reset;
//    }
//    
////    public static boolean checkCanBroken(SkillType source, SkillType other) {
////        
////    }
//}
