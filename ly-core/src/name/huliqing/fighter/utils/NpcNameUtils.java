/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.utils;

import com.jme3.math.FastMath;
import name.huliqing.fighter.GameException;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.enums.Sex;

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
            throw new GameException("暂不支持其它性别的随机名字!");
        }
    }
    
    /**
     * 生成一个女性随机名字.
     */
    public static String createFemale() {
        if (!init) {
            prefixes = ResourceManager.get("npc.name.prefix").split(",");
            suffixes = ResourceManager.get("npc.name.suffix").split(",");
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
