/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.BulletData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class BulletDataLoader implements DataLoader<BulletData>{

    @Override
    public BulletData loadData(Proto proto) {
        BulletData data = new BulletData(proto.getId());
        return data;
    }
    
}
