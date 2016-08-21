/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.loader.module;

import java.util.ArrayList;
import name.huliqing.core.data.ChannelData;
import name.huliqing.core.data.module.ChannelModuleData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.xml.DataLoader;
import name.huliqing.core.xml.Proto;

/**
 *
 * @author huliqing
 */
public class ChannelModuleDataLoader implements DataLoader<ChannelModuleData> {

    @Override
    public void load(Proto proto, ChannelModuleData data) {
//        String[] channels = proto.getAsArray("channels");
//        if (channels != null) {
//            data.setChannels(new ArrayList<ChannelData>(channels.length));
//            for (String channelId : channels) {
//                data.getChannels().add((ChannelData) DataFactory.createData(channelId));
//            }
//        }
    }

}
