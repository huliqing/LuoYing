/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import com.jme3.animation.AnimControl;
import name.huliqing.fighter.data.ChannelData;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.object.channel.Channel;
import name.huliqing.fighter.object.channel.SimpleChannel;

/**
 *
 * @author huliqing
 */
class ChannelLoader {
    
    public static Channel load(ChannelData data, AnimControl ac) {
        SimpleChannel sac = new SimpleChannel(data, ac);
        return sac;
    }
    
    public static Channel load(String channelId, AnimControl ac) {
        return load(DataLoaderFactory.createChannelData(channelId), ac);
    }
}
