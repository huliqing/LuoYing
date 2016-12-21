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

import com.jme3.math.FastMath;
import name.huliqing.luoying.enums.Sex;
import name.huliqing.luoying.manager.ResManager;

/**
 * 随机生成NPC名字
 * @author huliqing
 */
public class NpcNameUtils {
    private static String[] prefixes;
    private static String[] suffixes;
    private static boolean init;
    
    public static String createRandomName(Sex sex) {
        if (sex == Sex.female) {
            return NpcNameUtils.createFemale();
        } else {
            throw new RuntimeException("暂不支持其它性别的随机名字!");
        }
    }
    
    /**
     * 生成一个女性随机名字.
     */
    public static String createFemale() {
        if (!init) {
            prefixes = ResManager.get("npc.name.prefix").split(",");
            suffixes = ResManager.get("npc.name.suffix").split(",");
            init = true;
        }
        String prefix = prefixes[FastMath.nextRandomInt(0, prefixes.length - 1)];
        return createInner(prefix, 0);
    }
    
    private static String createInner(String prefix, int loopCount) {
        loopCount++;
        String suffix = suffixes[FastMath.nextRandomInt(0, suffixes.length - 1)];
        String temp = prefix + suffix;
        
        // 一般最多loop 2-3次就可以找到一个合适的名字，无限loop的可能性几乎不存在.
        // 这个限制只是为了万一,比如人为设置的prefix和suffix可选太少，则有可能发生。
        if (loopCount > 5) {
            return temp;
        }
        
        // 如果只有2个字，则直接返回.
        if (temp.length() <= 2) {
            return temp;
        } else {
            // 如果大于或等于3个字，则后缀中的字符不能与前缀中的字符重复
            for (int i = 0; i < suffix.length(); i++) {
                if (prefix.indexOf(suffix.charAt(i)) != -1) {
                    // 如果后缀中存在字符与前缀相同，则重新获取。
                    return createInner(prefix, loopCount);
                }
            }
        }
        return temp;
    }
    
//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 20000; i++) {
//            System.out.println(i + ":" + createFemale());
//        }
//        System.out.println("use time=" + (System.currentTimeMillis() - start));
//    }
}
