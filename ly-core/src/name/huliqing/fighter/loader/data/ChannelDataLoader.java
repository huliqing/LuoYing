/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader.data;

import name.huliqing.fighter.data.ChannelData;
import name.huliqing.fighter.data.Proto;

/**
 *
 * @author huliqing
 */
public class ChannelDataLoader implements DataLoader<ChannelData>{

    @Override
    public ChannelData loadData(Proto proto) {
        ChannelData data = new ChannelData(proto.getId());
        data.setFromRootBones(proto.getAsArray("fromRootBones"));
        data.setToRootBones(proto.getAsArray("toRootBones"));
        data.setBones(proto.getAsArray("bones"));
        return data;
    }
    
}
