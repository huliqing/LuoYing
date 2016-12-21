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
