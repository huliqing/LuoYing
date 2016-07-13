/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.channel;

import name.huliqing.fighter.data.ChannelData;
import name.huliqing.fighter.data.Proto;
import name.huliqing.fighter.object.DataLoader;

/**
 *
 * @author huliqing
 */
public class ChannelDataLoader implements DataLoader<ChannelData> {

    @Override
    public void load(Proto proto, ChannelData data) {
        // ignore
        data.setFromRootBones(proto.getAsArray("fromRootBones"));
        data.setToRootBones(proto.getAsArray("toRootBones"));
        data.setBones(proto.getAsArray("bones"));
    }
    
}
