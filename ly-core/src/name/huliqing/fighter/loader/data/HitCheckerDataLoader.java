/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.HitCheckerData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class HitCheckerDataLoader implements DataLoader<HitCheckerData>{

    @Override
    public HitCheckerData loadData(Proto proto) {
        HitCheckerData data = new HitCheckerData(proto.getId());
        return data;
    }
    
}
