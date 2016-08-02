/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.data.Proto;
import name.huliqing.core.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class ActorLogicDataLoader implements DataLoader<ActorLogicData> {

    @Override
    public void load(Proto proto, ActorLogicData data) {
        data.setInterval(proto.getAsFloat("interval", 1.0f));
    }
    
}
