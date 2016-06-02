/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class HandlerDataLoader implements DataLoader<HandlerData>{

    @Override
    public HandlerData loadData(Proto proto) {
        HandlerData data = new HandlerData(proto.getId());        
        return data;
    }
    
}
