/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.ActorAnimData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class ActorAnimDataLoader implements DataLoader<ActorAnimData>{

    @Override
    public ActorAnimData loadData(Proto proto) {
        ActorAnimData data = new ActorAnimData(proto.getId());
        return data;
    }
    
}
