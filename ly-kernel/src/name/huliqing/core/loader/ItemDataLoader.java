/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.AttributeMatch;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ItemDataLoader implements DataLoader<ItemData> {

    @Override
    public void load(Proto proto, ItemData data) {
        // remove20160831
//        data.setRaceLimit(proto.getAsList("raceLimit"));
//        data.setSexLimit(Sex.identifyByName(proto.getAsString("sexLimit")));

        data.setDeletable(proto.getAsBoolean("deletable", true));
        
        // 属性限制，这些限制定义了：只有角色的属性与这些限制完全匹配时才可以使用这件物品
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
    }
    
}
