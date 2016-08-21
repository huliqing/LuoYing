/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.module.SkinModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class SkinModuleDataLoader implements DataLoader<SkinModuleData> {

    @Override
    public void load(Proto proto, SkinModuleData data) {
//        // skinBase 基本皮肤,
//        // 注1：可能部分角色没有基本皮肤，如不可换装备的角色类型
//        String[] skinBaseArr = proto.getAsArray("skinBase");
//        if (skinBaseArr != null) {
//            data.setSkinBase(new ArrayList<SkinData>(skinBaseArr.length));
//            for (String skinId : skinBaseArr) {
//                data.getSkinBase().add((SkinData)DataFactory.createData(skinId));
//            }            
//        }
//        
//        // 包裹中的装备，未穿在身上的，包含普通装备和武器
//        // 注：skins可配置数量
//        // 格式："skinId1|10, skinId2|2, ..."
//        String[] skinArr = proto.getAsArray("skins");
//        if (skinArr != null && skinArr.length > 0) {
//            data.setSkinDatas(new ArrayList<SkinData>(skinArr.length));
//            for (String skinStr : skinArr) {
//                String[] sArr = skinStr.split("\\|");
//                SkinData skinData = DataFactory.createData(sArr[0]);
//                skinData.setTotal(sArr.length >= 2 ? Integer.parseInt(sArr[1]) : 1);
//                data.getSkinDatas().add(skinData);
//            }
//        }
//        
//        // 穿在身上的装备,包含武器,注意：skinUsed同样是放在 data.SkinDatas中
//        String[] skinUsedArr = proto.getAsArray("skinUsed");
//        if (skinUsedArr != null) {
//            if (data.getSkinDatas() == null) {
//                data.setSkinDatas(new ArrayList<SkinData>(skinUsedArr.length));
//            }
//            for (String skinId : skinUsedArr) {
//                SkinData skinData = findInList(skinId, data.getSkinDatas());
//                if (skinData != null) {
//                    skinData.setTotal(skinData.getTotal() + 1);
//                } else {
//                    skinData = DataFactory.createData(skinId);
//                    data.getSkinDatas().add(skinData);
//                }
//                skinData.setUsing(true);
//            }            
//        }
    }

//    private SkinData findInList(String skinId, List<SkinData> skinDatas) {
//        for (SkinData sd : skinDatas) {
//            if (sd.getId().equals(skinId)) {
//                return sd;
//            }
//        }
//        return null;
//    }
}
