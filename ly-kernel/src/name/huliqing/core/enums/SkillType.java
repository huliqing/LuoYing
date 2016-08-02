/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.enums;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;

/**
 * 定义技能类型,目标只支持1~31类技能，暂不支持超过31的技能。
 * @author huliqing
 */
public enum SkillType {
    
    
    /** 等待 */
    wait(1  , makeBits(13)           // make overlaps
            , makeBits(3,4,6,7)),    // make interrupts
    
    /** 坐下 */
    sit(2   , makeBits(13)
            , makeBits(3,4)),
    
    /** 走动 */
    walk(3  , makeBits(13)
            , makeBits(1,2,3,4,6,7)),
    
    /** 跑动 */
    run(4   , makeBits(13)
            , makeBits(1,2,3,4,6,7)),
    
    /** 跳跃 */
    jump(5  , makeBits(13)
            , makeBits(3,4,6,7)),
    
    /** 跳舞 */
    dance(6 , makeBits(0)
            , makeBits(3,4,6,7)),
    
    /** 休闲行为 */
    idle(7  , makeBits(0)
            , makeBits(3,4,6)),
    
    /** 受伤 */
    hurt(8  , makeBits(0)
            , makeBits(1,2,3,4,5,6,7,8,11,12,13,20,21,22,63)),
    
    /** 死亡 */
    dead(9  , makeBits(0)
            , makeBits(1,2,3,4,5,6,7,8,10,11,12,13,20,21,22,63)),
    
    /** Reset静止技能 */
    reset(10    , makeBits(0)
                , makeBits(1,2,3,4,5,6,7,8,11,12,13,20,21,22,63)),
    
    /** 闪避 */
    duck(11     , makeBits(0)
                , makeBits(3,4,6,7,20)),
    
    /** 防守技能 */
    defend(12   , makeBits(0)
                , makeBits(3,4,6,7,20)),
    
    /** 换装,换武器技能 */
    skin(13     , makeBits(1,2,3,4,5,13)
                , makeBits(0)),
    
    /** 攻击技能:普通攻击,如刀剑攻击，射击等 */
    attack(20   , makeBits(0)
                , makeBits(3,4,6,7)),
    
    /** 攻击技能:特殊攻击,大招等 */
    trick(21    , makeBits(0)
                , makeBits(3,4,6,7,20)),
    
    /** 普通魔法，加血加BUFF等 */
    magic(22    , makeBits(0)
                , makeBits(3,4,6,7,20)),
    
    /** 普通技能，所有未分类或不知如何分类的技能 */
    common(63   , makeBits(0)
                , makeBits(3,4,6,7)),
    ;
    private static final Logger LOG = Logger.getLogger(SkillType.class.getName());
    // 技能类型的10进制值
    private int value;
    // 可覆盖的所有其它技能类型的二进制表示,每1个二进制位表示一个特定技能
    private long overlaps;
    // 可中断的所有其它技能类型的二进制表示,每1个二进制位表示一个特定技能
    private long interrupts;
    
    private SkillType(int value, int overlaps, int interrupts) {
        this.value = value;
        this.overlaps = overlaps;
        this.interrupts = interrupts;
    }

    public int getValue() {
        return value;
    }
    
    /**
     * 获取当前技能可覆盖的目标技能的类型，二进制位表示，每个二进制位表示一个
     * 技能类型
     * @return 
     */
    public long getOverlaps() {
        return overlaps;
    }
    
    /**
     * 获取当前技能可中断的目标技能的类型，二进制位表示，每个二进制位表示一个
     * 技能类型
     * @return 
     */
    public long getInterrupts() {
        return interrupts;
    }
        
    /**
     * 判断当前技能是否可以覆盖目标技能执行，即在不需要打断目标技能的情况下
     * 执行当前技能。
     * @param target
     * @return 
     */
    public boolean isOverlaps(SkillType target) {
        boolean result = (overlaps & (1 << target.getValue())) != 0;
        if (Config.debug) {
            LOG.log(Level.INFO, "skill type overlaps check: {0} overlaps {1} = {2}", new Object[] {name(), target.name(), result});
        }
        return result;
    }
    
    /**
     * 判断当前技能是否可以打断目标技能
     * @param target 目标技能
     * @return 
     */
    public boolean isInterrupts(SkillType target) {
        boolean result = (interrupts & (1 << target.getValue())) != 0;
        if (Config.debug) {
            LOG.log(Level.INFO, "skill type interrupts check: {0} interrupts {1} = {2}", new Object[] {name(), target.name(), result});
        }
        return result;
    }
    
    public static SkillType identify(int value) {
        SkillType[] pts = SkillType.values();
        for (SkillType pt : pts) {
            if (pt.getValue() == value) {
                return pt;
            }
        }
        throw new UnsupportedOperationException("不支持的value:" + value);
    }

    /**
     * 通过名称识别技能
     *
     * @param name
     * @return
     */
    public static SkillType identifyByName(String name) {
        SkillType[] pts = SkillType.values();
        for (SkillType pt : pts) {
            if (pt.name().equals(name)) {
                return pt;
            }
        }
        throw new UnsupportedOperationException("不支持的name:" + name);
    }

    /**
     * 为给定的技能类型生成一个状态值。返回值中的每一个二制进位表示一个技能类型
     * @param types
     * @return 
     */
    public static long createSkillStates(SkillType... types) {
        int[] values = new int[types.length];
        for (int i = 0; i < types.length; i++) {
            values[i] = types[i].value;
        }
        return makeBits(values);
    }
    
    private static int makeBits(int... values) {
        int result = 0;
        for (int i : values) {
            result |= 1 << i;
        }
        return result;
    }
}
