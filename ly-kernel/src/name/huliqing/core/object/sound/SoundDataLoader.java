/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.sound;

import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.SoundData;
import name.huliqing.core.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class SoundDataLoader implements DataLoader<SoundData> {

    @Override
    public void load(Proto proto, SoundData data) {
        data.setVolume(proto.getAsFloat("volume", 1.0f));
        data.setOffset(proto.getAsFloat("offset", 0));
        data.setLoop(proto.getAsBoolean("loop", false));
        data.setInstance(proto.getAsBoolean("instance", false));
    }
    
}
