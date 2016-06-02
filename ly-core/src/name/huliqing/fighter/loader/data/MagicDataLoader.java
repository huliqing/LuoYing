/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.MagicData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class MagicDataLoader implements DataLoader<MagicData>{

    @Override
    public MagicData loadData(Proto proto) {
        MagicData data = new MagicData(proto.getId());
        data.setUseTime(proto.getAsFloat("useTime", 1));
        return data;
    }
    
}
