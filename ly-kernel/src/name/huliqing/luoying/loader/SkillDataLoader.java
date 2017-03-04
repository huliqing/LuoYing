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
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.object.define.WeaponTypeDefine;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * @author huliqing
 */
public class SkillDataLoader implements DataLoader<SkillData> {
    private final DefineService defineService = Factory.get(DefineService.class);
    
    @Override
    public void load(Proto proto, SkillData data) {
//        data.setUseTime(proto.getAsFloat("useTime", 1));
//        data.setCooldown(proto.getAsFloat("cooldown", 0));
        
        // 格式： weaponStateLimit="rightSword,leftSword|rightSword|..." 
        String[] wslArr = proto.getAsArray("weaponStateLimit");
        if (wslArr != null && wslArr.length > 0) {
            long[] weaponStates = new long[wslArr.length];
            WeaponTypeDefine wtDefine = defineService.getWeaponTypeDefine();
            for (int i = 0; i < wslArr.length; i++) {
                weaponStates[i] = wtDefine.convert(wslArr[i].split("\\|"));
            }
            data.setWeaponStateLimit(weaponStates);
        }
        
        // 技能消耗的属性值
        String[] useAttributesStr = proto.getAsArray("useAttributes");
        if (useAttributesStr != null) {
            ArrayList<AttributeUse> useAttributes = new ArrayList<AttributeUse>(useAttributesStr.length);
            for (int i = 0; i < useAttributesStr.length; i++) {
                String[] temp = useAttributesStr[i].split("\\|");
                useAttributes.add(new AttributeUse(temp[0], ConvertUtils.toFloat(temp[1], 0)));
            }
            data.setUseAttributes(useAttributes);
        }
        
//        data.setLevel(proto.getAsInteger("level", 1));
//        data.setMaxLevel(proto.getAsInteger("maxLevel", 1));
//        data.setPlayCount(proto.getAsInteger("playCount", 0));
        data.setTypes(defineService.getSkillTypeDefine().convert(proto.getAsArray("types")));
        data.setOverlapTypes(defineService.getSkillTypeDefine().convert(proto.getAsArray("overlapTypes")));
        data.setInterruptTypes(defineService.getSkillTypeDefine().convert(proto.getAsArray("interruptTypes")));
//        data.setPrior(proto.getAsInteger("prior", 0));
        
    }
    
    
}
