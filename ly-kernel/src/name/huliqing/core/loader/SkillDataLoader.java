/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.util.ArrayList;
import name.huliqing.core.GameException;
import name.huliqing.core.data.AttributeUse;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.define.DefineFactory;
import name.huliqing.core.object.define.WeaponTypeDefine;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.utils.ConvertUtils;

/**
 * @author huliqing
 */
public class SkillDataLoader implements DataLoader<SkillData> {

    @Override
    public void load(Proto proto, SkillData data) {
        if (!(data instanceof SkillData)) {
            throw new GameException("Not a skill data. proto=" + proto);
        }
        
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
        data.setPlayCount(proto.getAsInteger("skillPoints", 0));
        data.setLevelLimit(proto.getAsInteger("needLevel", 0));
        data.setTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("tags")));
        data.setOverlapTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("overlapTags")));
        data.setInterruptTags(DefineFactory.getSkillTagDefine().convert(proto.getAsArray("interruptTags")));
        data.setPrior(proto.getAsInteger("prior", 0));
        
    }
    
    
}
