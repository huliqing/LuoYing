/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.util.ArrayList;
import name.huliqing.core.data.AttributeApply;
import name.huliqing.core.data.AttributeMatch;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkinDataLoader implements DataLoader<SkinData> {

    @Override
    public void load(Proto proto, SkinData data) {
        // remove20160831
//        data.setRaceLimit(proto.getAsList("raceLimit"));
//        data.setSexLimit(Sex.identifyByName(proto.getAsString("sexLimit")));
//        data.setDeletable(proto.getAsBoolean("deletable", true));

        data.setBaseSkin(proto.getAsBoolean("baseSkin", false));
        data.setUsed(true);
        
        String[] aaStr = proto.getAsArray("applyAttributes");
        if (aaStr != null) {
            ArrayList<AttributeApply> aas = new ArrayList<AttributeApply>(aaStr.length);
            for (int i = 0; i < aaStr.length; i++) {
                String[] aaArr = aaStr[i].split("\\|");
                aas.add(new AttributeApply(aaArr[0], ConvertUtils.toFloat(aaArr[1], 0)));
            }
            data.setApplyAttributes(aas);
        }
        
        data.setWeaponType(proto.getAsInteger("weaponType", 0));
        data.setSlots(proto.getAsStringList("slots"));
        
        // type是使用二进制位来存储skin类型
        String[] typeStr = proto.getAsArray("type");
        int[] types = ConvertUtils.toIntegerArray(typeStr);
        int type = 0;
        for (int t : types) {
            type |= 1 << t;
        }
        data.setType(type);
        
        // conflictType可为null,所以需要判断
        String[] conflictTypeStr = proto.getAsArray("conflictType");
        if (conflictTypeStr != null) {
            int[] conflictInts = ConvertUtils.toIntegerArray(conflictTypeStr);
            int conflict = 0;
            for (int ci : conflictInts) {
                conflict |= 1 << ci;
            }
            data.setConflictType(conflict);
        }
        
        // 属性限制，这些限制定义了：只有角色的属性与这些限制完全匹配时才可以使用这件物品
        // 格式：attributeName|value,attributeName|value,...
        String[] maArr = proto.getAsArray("matchAttributes");
        if (maArr != null && maArr.length > 0) {
            data.setMatchAttributes(new ArrayList<AttributeMatch>(maArr.length));
            for (String ma : maArr) {
                String[] vArr = ma.split("\\|");
                AttributeMatch am = new AttributeMatch();
                am.setAttributeName(vArr[0].trim());
                am.setValue(vArr[1].trim());
                data.getMatchAttributes().add(am);
            }
        }
        
        // data.setUsing(false);不需要
        
    }
    
}
