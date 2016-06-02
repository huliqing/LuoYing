/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.TalentData;

/**
 *
 * @author huliqing
 */
public class TalentDataLoader implements DataLoader<TalentData>{

    @Override
    public TalentData loadData(Proto proto) {
        TalentData data = new TalentData(proto.getId());
        data.setInterval(proto.getAsFloat("interval", 10));
        data.setLevel(proto.getAsInteger("level", 0));
        data.setMaxLevel(proto.getAsInteger("maxLevel", 10));
        return data;
    }
    
}
