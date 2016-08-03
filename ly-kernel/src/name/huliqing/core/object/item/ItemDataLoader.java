/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.data.ItemData;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.enums.Sex;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ItemDataLoader implements DataLoader<ItemData> {

    @Override
    public void load(Proto proto, ItemData data) {
        data.setRaceLimit(proto.getAsList("raceLimit"));
        data.setSexLimit(Sex.identifyByName(proto.getAttribute("sexLimit")));
        data.setDeletable(proto.getAsBoolean("deletable", true));
    }
    
}
