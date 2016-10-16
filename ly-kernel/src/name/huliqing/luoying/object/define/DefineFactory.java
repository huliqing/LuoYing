/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.define;

import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;

/**
 * 各种”定义“的工厂类。
 * @author huliqing
 */
public class DefineFactory {
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
            skillTagDefine = Loader.load(IdConstants.SYS_DEFINE_SKILL_TAG);
        }
        return skillTagDefine;
    }
    
    /**
     * 装备部位的定义
     * @return 
     */
    public static SkinPartDefine getSkinPartDefine() {
        if (skinPartDefine == null) {
            skinPartDefine = Loader.load(IdConstants.SYS_DEFINE_SKIN_PART);
        }
        return skinPartDefine;
    }
    
    /**
     * 获取武器类型的定义配置。
     * @return 
     */
    public static WeaponTypeDefine getWeaponTypeDefine() {
        if (weaponTypeDefine == null) {
            weaponTypeDefine = loadDefine(IdConstants.SYS_DEFINE_WEAPON_TYPE);
        }
        return weaponTypeDefine;
    }
    
    /**
     * 获取物体质地定义
     * @return 
     */
    public static MatDefine getMatDefine() {
        if (matDefine == null) {
            matDefine = loadDefine(IdConstants.SYS_DEFINE_MAT);
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
