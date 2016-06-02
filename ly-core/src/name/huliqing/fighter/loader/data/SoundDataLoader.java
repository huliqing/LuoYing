/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.data.SoundData;

/**
 *
 * @author huliqing
 */
public class SoundDataLoader implements DataLoader<SoundData>{

    @Override
    public SoundData loadData(Proto proto) {
        SoundData data = new SoundData(proto.getId());
        
        data.setVolume(proto.getAsFloat("volume", 1.0f));
        data.setOffset(proto.getAsFloat("offset", 0));
        data.setLoop(proto.getAsBoolean("loop", false));
        
        return data;
    }
    
}
