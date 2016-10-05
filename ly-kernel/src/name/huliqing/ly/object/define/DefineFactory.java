/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.define;

import name.huliqing.ly.object.Loader;

/**
 * 各种”定义“的工厂类。
 * @author huliqing
 */
public class DefineFactory {
    // 默认的技能标记定义的配置id.
    private final static String SKILL_TAG_DEFINE_ID = "defineSkillTag";
    
    // 默认的关于装备部位的定义配置的id
    private final static String SKIN_PART_DEFINE_ID = "defineSkinPart";
    
    // 武器类型的定义
    private final static String WEAPON_TYPE_DEFINE_ID = "defineWeaponType";
    
    // 物体构成成分（质的）的定义(id)。
    private final static String MAT_DEFINE_ID = "defineMat";
    
    private static SkillTagDefine skillTagDefine;
    private static SkinPartDefine skinPartDefine;
    private static WeaponTypeDefine weaponTypeDefine;
    private static MatDefine matDefine;
    
    /**
     * 获取技能标记定义
     * @return 
     */
    public static SkillTagDefine getSkillTagDefine() {
        if (skillTagDefine == null) {
            skillTagDefine = Loader.load(SKILL_TAG_DEFINE_ID);
        }
        return skillTagDefine;
    }
    
    /**
     * 装备部位的定义
     * @return 
     */
    public static SkinPartDefine getSkinPartDefine() {
        if (skinPartDefine == null) {
            skinPartDefine = Loader.load(SKIN_PART_DEFINE_ID);
        }
        return skinPartDefine;
    }
    
    /**
     * 获取武器类型的定义配置。
     * @return 
     */
    public static WeaponTypeDefine getWeaponTypeDefine() {
        if (weaponTypeDefine == null) {
            weaponTypeDefine = loadDefine(WEAPON_TYPE_DEFINE_ID);
        }
        return weaponTypeDefine;
    }
    
    /**
     * 获取物体质地定义
     * @return 
     */
    public static MatDefine getMatDefine() {
        if (matDefine == null) {
            matDefine = loadDefine(MAT_DEFINE_ID);
        }
        return matDefine;
    }
    
    private static <T extends Define> T loadDefine(String id) {
        Define define = Loader.load(id);
        if (define == null) {
            throw new NullPointerException("Could not find define by id: " + id);
        }
        return (T) define;
    }
}
