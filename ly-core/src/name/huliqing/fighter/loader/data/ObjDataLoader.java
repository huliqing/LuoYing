/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.ItemData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.enums.Sex;

/**
 *
 * @author huliqing
 */
public class ObjDataLoader implements DataLoader<ItemData>{

    @Override
    public ItemData loadData(Proto proto) {
        ItemData data = new ItemData(proto.getId());
        data.setRaceLimit(proto.getAsList("raceLimit"));
        data.setSexLimit(Sex.identifyByName(proto.getAttribute("sexLimit")));
        return data;
    }
    
}
