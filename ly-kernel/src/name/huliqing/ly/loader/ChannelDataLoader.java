/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.loader;

import name.huliqing.ly.data.ChannelData;
import name.huliqing.ly.xml.Proto;
import name.huliqing.ly.xml.DataLoader;

/**
 *
 * @author huliqing
 */
public class ChannelDataLoader implements DataLoader<ChannelData> {

    @Override
    public void load(Proto proto, ChannelData data) {
        // remove20160930
//        data.setFromRootBones(proto.getAsArray("fromRootBones"));
//        data.setToRootBones(proto.getAsArray("toRootBones"));
//        data.setBones(proto.getAsArray("bones"));
    }
    
}
