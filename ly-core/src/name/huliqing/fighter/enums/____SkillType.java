///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.enums;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.Config;
//
///**
// * 定义技能类型,目标只支持1~31类技能，暂不支持超过31的技能。
// * @author huliqing
// */
//public enum SkillType {
//    
//    
//    /** 等待 */
//    wait(1  , makeBits(15)           // make overlaps
//            , makeBits(3,4,6,7)),    // make interrupts
//    
//    /** 坐下 */
//    sit(2   , makeBits(15)
//            , makeBits(3,4)),
//    
//    /** 走动 */
//    walk(3  , makeBits(15)
//            , makeBits(1,2,3,4,6,7)),
//    
//    /** 跑动 */
//    run(4   , makeBits(15)
//            , makeBits(1,2,3,4,6,7)),
//    
//    /** 跳跃 */
//    jump(5  , makeBits(15)
//            , makeBits(3,4,6,7)),
//    
//    /** 跳舞 */
//    dance(6 , makeBits(0)
//            , makeBits(3,4,6,7)),
//    
//    /** 休闲行为 */
//    idle(7  , makeBits(0)
//            , makeBits(3,4,6)),
//    
//    /** 受伤 */
//    hurt(8  , makeBits(0)
//            , makeBits(1,2,3,4,5,6,7,8,11,12,13,14,15,16,17)),
//    
//    /** 死亡 */
//    dead(9  , makeBits(0)
//            , makeBits(1,2,3,4,5,6,7,8,10,11,12,13,14,15,16,17)),
//    
//    /** Reset静止技能 */
//    reset(10    , makeBits(0)
//                , makeBits(1,2,3,4,5,6,7,8,11,12,13,14,15,16,17)),
//    
//    /** 攻击技能:普通攻击,如刀剑攻击，射击等 */
//    attack(11   , makeBits(0)
//                , makeBits(3,4,6,7,15)),
//    
//    /** 攻击技能:特殊攻击,大招等 */
//    trick(12    , makeBits(0)
//                , makeBits(3,4,6,7,15)),
//    
//    /** 攻击技能:特殊攻击,如普通魔法，加血加BUFF等 */
//    magic(17    , makeBits(0)
//                , makeBits(3,4,6,7,15)),
//    
//    /** 闪避 */
//    duck(13     , makeBits(0)
//                , makeBits(3,4,6,7,11)),
//    
//    /** 防守技能 */
//    defend(14   , makeBits(0)
//                , makeBits(3,4,6,7,11)),
//    
//    /** 换装,换武器技能 */
//    skin(15     , makeBits(1,2,3,4,5,15)
//                , makeBits(0)),
//
//    /** 普通技能，所有未分类或不知如何分类的技能 */
//    common(16   , makeBits(0)
//                , makeBits(3,4,6,7)),
//    ;
//    private static final Logger LOG = Logger.getLogger(SkillType.class.getName());
//    private int value;
//    private int overlaps;
//    private int interrupts;
//    
//    private SkillType(int value, int overlaps, int interrupts) {
//        this.value = value;
//        this.overlaps = overlaps;
//        this.interrupts = interrupts;
//    }
//
//    public int getValue() {
//        return value;
//    }
//    
//    /**
//     * 获取当前技能可覆盖的目标技能的类型，二进制位表示，每个二进制位表示一个
//     * 技能类型
//     * @return 
//     */
//    public int getOverlaps() {
//        return overlaps;
//    }
//    
//    /**
//     * 获取当前技能可中断的目标技能的类型，二进制位表示，每个二进制位表示一个
//     * 技能类型
//     * @return 
//     */
//    public int getInterrupts() {
//        return interrupts;
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
//     * 判断当前技能是否可以覆盖目标技能执行，即在不需要打断目标技能的情况下
//     * 执行当前技能。
//     * @param target
//     * @return 
//     */
//    public boolean isOverlaps(SkillType target) {
//        boolean result = (overlaps & (1 << target.getValue())) != 0;
//        if (Config.debug) {
//            LOG.log(Level.INFO, "skill type overlaps check: {0} overlaps {1} = {2}", new Object[] {name(), target.name(), result});
//        }
//        return result;
//    }
//    
//    /**
//     * 判断当前技能是否可以打断目标技能
//     * @param target 目标技能
//     * @return 
//     */
//    public boolean isInterrupts(SkillType target) {
//        boolean result = (interrupts & (1 << target.getValue())) != 0;
//        if (Config.debug) {
//            LOG.log(Level.INFO, "skill type interrupts check: {0} interrupts {1} = {2}", new Object[] {name(), target.name(), result});
//        }
//        return result;
//    }
//    
//    private static int makeBits(int... values) {
//        int result = 0;
//        for (int i : values) {
//            result |= 1 << i;
//        }
//        return result;
//    }
//    
//    // remove20151212
////    /**
////     * 判断是否为idle行为，包含：wait,sit,walk,run,jump,idle(base)
////     * @param st
////     * @return 
////     */
////    public static boolean isIdle(SkillType st) {
////        return st.getValue() >= 10 && st.getValue() <= 19;
////    }
////    
////    /**
////     * 判断是否为基本idle行为，idle value=15 SkillType=idle
////     * @param st
////     * @return 
////     */
////    public static boolean isIdleBase(SkillType st) {
////        return st == idle;
////    }
////    
////    public static boolean isHurt(SkillType st) {
////        return st.getValue() >= 20 && st.getValue() <= 29;
////    }
////    
////    public static boolean isDuck(SkillType st) {
////        return st.getValue() >= 30 && st.getValue() <= 39;
////    }
////    
////    public static boolean isDefend(SkillType st) {
////        return st.getValue() >= 40 && st.getValue() <= 49;
////    }
////    
////    public static boolean isReady(SkillType st) {
////        return st.getValue() == 50;
////    }
////    
////    /**
////     * 判断是否为攻击类技能:攻击类技能包含普通攻击技、特技攻击、射击、魔法攻击
////     * @param st
////     * @return 
////     */
////    public static boolean isAttack(SkillType st) {
////        return st.getValue() >= 60 && st.getValue() <= 69;
////    }
////    
////    /**
////     * 判断是否为普通魔法技能，这类魔法不是攻击技能。
////     * @param st
////     * @return 
////     */
////    public static boolean isMagic(SkillType st) {
////        return st.getValue() >= 70 && st.getValue() <= 79;
////    }
////    
////    /**
////     * reset技能
////     * @param st
////     * @return 
////     */
////    public static boolean isReset(SkillType st) {
////        return st == SkillType.reset;
////    }
//
//}
