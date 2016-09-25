/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.define;

import name.huliqing.core.object.Loader;

/**
 * 
 * @author huliqing
 */
public class DefineFactory {
    // 默认的技能标记定义的配置id.
    private final static String SKILL_TAG_DEFINE_ID = "defineSkillTag";
    
    // 默认的关于装备部位的定义配置的id
    private final static String SKIN_PART_DEFINE_ID = "defineSkinPart";
    
    // 武器类型的定义
    private final static String WEAPON_TYPE_DEFINE_ID = "defineWeaponType";
    
    private static SkillTagDefine skillTagDefine;
    private static SkinPartDefine skinPartDefine;
    private static WeaponTypeDefine weaponTypeDefine;
    
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
    
    private static <T extends Define> T loadDefine(String id) {
        Define define = Loader.load(id);
        if (define == null) {
            throw new NullPointerException("Could not find define by id: " + id);
        }
        return (T) define;
    }
}
