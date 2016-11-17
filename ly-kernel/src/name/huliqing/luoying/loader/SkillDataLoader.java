/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import java.util.ArrayList;
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.define.DefineFactory;
import name.huliqing.luoying.object.define.WeaponTypeDefine;
import name.huliqing.luoying.xml.DataLoader;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * @author huliqing
 */
public class SkillDataLoader implements DataLoader<SkillData> {

    @Override
    public void load(Proto proto, SkillData data) {
        data.setUseTime(proto.getAsFloat("useTime", 1));
        data.setCooldown(proto.getAsFloat("cooldown", 0));
        
        // 格式： weaponStateLimit="rightSword,leftSword|rightSword|..." 
        String[] wslArr = proto.getAsArray("weaponStateLimit");
        if (wslArr != null && wslArr.length > 0) {
            long[] weaponStates = new long[wslArr.length];
            WeaponTypeDefine wtDefine = DefineFactory.getWeaponTypeDefine();
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
        
        data.setLevel(proto.getAsInteger("level", 1));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 1));
        data.setPlayCount(proto.getAsInteger("playCount", 0));
        data.setTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("tags")));
        data.setOverlapTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("overlapTags")));
        data.setInterruptTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("interruptTags")));
        data.setPrior(proto.getAsInteger("prior", 0));
        
    }
    
    
}
