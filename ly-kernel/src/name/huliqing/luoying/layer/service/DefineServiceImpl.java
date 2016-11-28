package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.define.Define;
import name.huliqing.luoying.object.define.MatDefine;
import name.huliqing.luoying.object.define.SkillTypeDefine;
import name.huliqing.luoying.object.define.SkinPartDefine;
import name.huliqing.luoying.object.define.WeaponTypeDefine;

/**
 *
 * @author huliqing
 */
public class DefineServiceImpl implements DefineService {
    
    private static SkillTypeDefine skillTypeDefine;
    private static SkinPartDefine skinPartDefine;
    private static WeaponTypeDefine weaponTypeDefine;
    private static MatDefine matDefine;
    
    @Override
    public void inject() {}

    @Override
    public SkillTypeDefine getSkillTypeDefine() {
        if (skillTypeDefine == null) {
            skillTypeDefine = loadDefine(IdConstants.SYS_DEFINE_SKILL_TYPE);
        }
        return skillTypeDefine;
    }

    @Override
    public SkinPartDefine getSkinPartDefine() {
        if (skinPartDefine == null) {
            skinPartDefine = loadDefine(IdConstants.SYS_DEFINE_SKIN_PART);
        }
        return skinPartDefine;
    }

    @Override
    public WeaponTypeDefine getWeaponTypeDefine() {
        if (weaponTypeDefine == null) {
            weaponTypeDefine = loadDefine(IdConstants.SYS_DEFINE_WEAPON_TYPE);
        }
        return weaponTypeDefine;
    }
    
    @Override
    public MatDefine getMatDefine() {
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
