/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import java.util.ArrayList;
import name.huliqing.fighter.data.AttributeApply;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SkinData;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 *
 * @author huliqing
 */
public class SkinDataLoader implements DataLoader<SkinData>{

    @Override
    public SkinData loadData(Proto proto) {
        SkinData data = new SkinData(proto.getId());
        data.setRaceLimit(proto.getAsList("raceLimit"));
        data.setSexLimit(Sex.identifyByName(proto.getAttribute("sexLimit")));
        
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
        data.setSlots(proto.getAsList("slots"));
        
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
        
        // data.setUsing(false);不需要
        
        return data;
    }
    
}
