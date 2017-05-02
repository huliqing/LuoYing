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
package name.huliqing.luoying.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 管理角色等级相关信息
 * @deprecated 不再使用
 * @author huliqing
 */
public class LevelUtils {
    
    // 注：防御力的提升速度必须尽量低于攻击力的提升速度，以避免由于等级差距太大导
    // 致低等级打不动高等级的角色。

//    // 等级经验计算公式
//    public final static Formula expLevelUpFormula = new DefaultFormula(0, 50, 1.2f);
//    // 等级经验奖励公式
//    public final static Formula expRewardFormula = new DefaultFormula(0, 15, 1.1f); // default
//    // 生命值
//    public final static Formula healthFormula = new DefaultFormula(50, 8, 1.1);
//    // 魔法值
//    public final static Formula magicFormula = new DefaultFormula(25, 8, 1.05);
//    // 等级与防御值计算公式
//    public final static Formula defenceFormula = new DefaultFormula(0, 2, 1.05);
//    // 等级与攻击力计算公式
//    public final static Formula attackFormula = new DefaultFormula(0, 4, 1.05);
    
    
    // ------------test-------------
    // 等级经验计算公式
    public final static Formula expLevelUpFormula = new DefaultFormula(0, 50, 1.2f);
    // 等级经验奖励公式
    public final static Formula expRewardFormula = new DefaultFormula(0, 15, 1.1f); // default
    // 生命值
    public final static Formula healthFormula = new DefaultFormula(-60, 120, 1.1);
    // 魔法值
    public final static Formula magicFormula = new DefaultFormula(-60, 120, 1.05);
    // 等级与防御值计算公式
    public final static Formula defenceFormula = new DefaultFormula(-20, 25, 1.06);
    // 等级与攻击力计算公式
    public final static Formula attackFormula = new DefaultFormula(-45, 50, 1.06);
    
    public static void main(String[] args) {
//        test("Exp", expLevelUpFormula);
//        test("ExpReward", expRewardFormula);
//        test("Attack", attackFormula);
//        test("Defence", defenceFormula);
        test("Health", healthFormula);
//        test("Magic", magicFormula);
    }
    
    /**
     * 检查可以升多少级和需要多少经验
     * @param currentLevel 当前的等级
     * @param currentXp 当前的经验值
     * @param store 存放结果的数组，store[2] {upCount, needXp} upCount表示可以
     * 升多少级，needXp表示需要多少xp.
     */
    public static void checkLevelUp(int currentLevel, int currentXp, int[] store) {
        if (currentLevel >= 60) {
            return;
        }
        long nextXp = expLevelUpFormula.getValueByStage(currentLevel + 1);
        if (currentXp >= nextXp) {
            currentLevel++;
            currentXp -= nextXp;
            store[0] = store[0] + 1;
            store[1] = store[1] + (int) nextXp;
            checkLevelUp(currentLevel, currentXp, store);
        }
    }
    
    /**
     * 封装等级值
     */
    public static class LevelData {
        /** 等级值 */
        public int level;
        /** 最高生命值 */
        public int maxHealth;
        /** 最高魔法值 */
        public int maxMagic;
        /** 防御值 */
        public int defence;
        /** 攻击力 */
        public int attack;
        /** 攻击速度 */
        public float attackSpeed;
        /** 鬼魅速率 */
        public float ghost;
        /** 防守概率 */
        public float aiDefend;
        /** 躲闪概率 */
        public float aiDuck;
    }
    
    public static LevelData createLevelData(int level) {
        LevelData data = new LevelData();
        data.level = level;
        data.maxHealth = (int) healthFormula.getValueByTotal(level);
        data.maxMagic = (int) magicFormula.getValueByTotal(level);
        data.defence = (int) defenceFormula.getValueByTotal(level);
        data.attack = (int) attackFormula.getValueByTotal(level);
        
        // 攻击速度
        data.attackSpeed = (float) level / 60 * 1 + 1;
        // 鬼魅速率
        data.ghost = (float) level / 60;
        // 防守概率
        data.aiDefend = (float) level / 60;
        // 躲闪概率
        data.aiDuck = (float) level / 60;
        return data;
    }
    
    /**
     * 根据死亡的目标等级计算该等级可以取得的经验奖励。
     * @param level
     * @return 
     */
    public static int getXpFromLevel(int level) {
        return (int) expRewardFormula.getValueByStage(level);
    }
    
    private static void test(String name, Formula f) {
        for (int i = 0; i < 60; i++) {
            long value = f.getValueByStage(i);
            System.out.println(String.format("%s -> Level=%s, stage value=%s", new Object[] {name, i, value}));
        }
        System.out.println("====");
        for (int i = 0; i < 60; i++) {
            long value = f.getValueByTotal(i);
            System.out.println(String.format("%s -> Level=%s, total value=%s", new Object[] {name, i, value}));
        }
    }
    

    
    
    /**
     * 等级计算公式
     * @author huliqing
     */
    public interface Formula {

        /**
         * 根据等级计算该等级阶段的值。
         *
         * @param level
         * @return
         */
        long getValueByStage(int level);

        /**
         * 根据等级计算该等级所有的总值。
         *
         * @param level
         * @return
         */
        long getValueByTotal(int level);
    }

    public static class DefaultFormula implements Formula {

        // 包含每个等级可获得的防御点数奖励
        private final Map<Integer, Long> stageMap = new LinkedHashMap<Integer, Long>(60 + 1);
        // 根据公式，每个等级最多会有多少个防御点数
        private final Map<Integer, Long> fullMap = new LinkedHashMap<Integer, Long>(60 + 1);
        // 计算公式的基值,该值为基础值
        protected double expBase = 0;
        // 计算公式的倍率系数
        protected double expFactor = 50;
        // 计算公式的指数级系数,该值越大，数值呈指数级增长。
        protected double expPow = 1.1;
        private boolean init;

        public DefaultFormula(double expBase, double expFactor, double expPow) {
            this.expBase = expBase;
            this.expFactor = expFactor;
            this.expPow = expPow;
        }

        protected void init() {
            double total = 0;
            for (int level = 0; level <= 60; level++) {
                //            double point = expBase * Math.pow(expFactor, level);
//                double value = expBase + expFactor * Math.pow(expPow, level);
                double value = expBase + expFactor * Math.pow(expPow, level);
                total += value;
                stageMap.put(level, (long) value);
                fullMap.put(level, (long) total);
            }
        }

        private void checkAndInit() {
            if (!init) {
                init();
                init = true;
            }
        }

        @Override
        public long getValueByStage(int level) {
            checkAndInit();
            if (!stageMap.containsKey(level) || level > 60) {
                throw new UnsupportedOperationException("Unsupported level: "
                        + level + ", only supported max level=" + 60);
            }
            return stageMap.get(level);
        }

        @Override
        public long getValueByTotal(int level) {
            checkAndInit();
            if (!fullMap.containsKey(level) || level > 60) {
                throw new UnsupportedOperationException("Unsupported level: "
                        + level + ", only supported max level=" + 60);
            }
            return fullMap.get(level);
        }
    }
}
