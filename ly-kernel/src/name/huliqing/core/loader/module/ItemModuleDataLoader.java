/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.data.module.ItemModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class ItemModuleDataLoader implements DataLoader<ItemModuleData> {

    @Override
    public void load(Proto proto, ItemModuleData data) {
        
//        // 添加item物体
//        // 格式示例：items="itemMapWorld,item000|10,itemGold|10,itemTowerSnow|100,itemScrollLife|10"
//        String[] aArr = proto.getAsArray("items");
//        if (aArr != null && aArr.length > 0) {
//            data.setItems(new ArrayList<ItemData>(aArr.length));
//            for (String bStr : aArr) {
//                if (bStr == null || bStr.trim().equals("")) {
//                    continue;
//                }
//                String[] bArr = bStr.split("\\|");
//                ItemData itemData = DataFactory.createData(bArr[0]);
//                itemData.setTotal(bArr.length >= 2 ? Integer.parseInt(bArr[1]) : 1);
//                data.getItems().add(itemData);
//            }
//        }
    }

    
}
