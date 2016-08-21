/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.data.ChannelData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.module.ChannelModuleData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.channel.Channel;
import name.huliqing.core.object.channel.ChannelControl;
import name.huliqing.core.utils.Temp;

/**
 * 用于控制角色动画通道的“通道控制器”
 * @author huliqing
 * @param <T>
 */
public class ChannelModule<T extends ChannelModuleData> extends AbstractModule<T> implements ChannelControl {
    private static final Logger LOG = Logger.getLogger(ChannelModule.class.getName());

    private Actor actor;
    // 通道处理器列表
    private final List<Channel> channels = new LinkedList<Channel>();
    // 保存一个完整的id引用
    private String[] fullChannelsIds;
    // 默认blendTime,秒
    private final static float DEFAULT_BLEND_TIME = 0.15f;
    
    // 用于判断是否处于reset状态. reset状态主要为兼容处理一些model因为缺失停止动画
    // 而无法停止的问题.比如一些角色模型由于缺少wait动画，导致一执行动画后无法停止。
    // 如走路后无法停止，这时可通过调用reset方法来让角色静止。而resetState则标记了
    // 该状态。
    private boolean resetState;
    
    // 角色模型的动画控制器
    private AnimControl animControl;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        animControl = actor.getSpatial().getControl(AnimControl.class);
        
        List<ObjectData> ods = actor.getData().getObjectDatas();
        if (ods != null && !ods.isEmpty()) {
            for (ObjectData od : ods) {
                if (od instanceof ChannelData) {
                    addChannel((Channel) Loader.load(od));
                }
            }
        }
       
    }

    @Override
    public void cleanup() {
        channels.clear();
        fullChannelsIds = null;
        super.cleanup(); 
    }
    
    @Override
    public void addChannel(Channel channel) {
        if (channels.contains(channel))
            return;
        
        channel.setAnimControl(animControl);
        channels.add(channel);
        actor.getData().addObjectData(channel.getData());

        // 更新完整通道ID
        if (fullChannelsIds == null) {
            fullChannelsIds = new String[] {channel.getId()};
        } else {
            String[] ids = new String[fullChannelsIds.length + 1];
            System.arraycopy(fullChannelsIds, 0, ids, 0, fullChannelsIds.length);
            ids[ids.length - 1] = channel.getId();
            fullChannelsIds = ids;
        }
    }

    @Override
    public Channel getChannel(String channelId) {
        if (channels == null) 
            return null;
        for (Channel ch : channels) {
            if (ch.getId().equals(channelId)) {
                return ch;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public void playAnim(String animName, LoopMode loop, float useTime, String... channelIds) {
        playAnim(animName, loop, useTime, 0, channelIds);
    }

    @Override
    public void playAnim(String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        playAnim(animName, DEFAULT_BLEND_TIME, loop, useTime, startTime, channelIds);
    }

    @Override
    public void playAnim(String animName, float blendTime, LoopMode loop
            , float useTime, float startTime, String... channelIds) {
        
        Channel sampleChannel = channels.get(0);
        AnimControl ac = sampleChannel.getAnimChannel().getControl();
        Animation anim = ac.getAnim(animName);
        if (anim == null) {
            LOG.log(Level.WARNING, "Could not find animation:{0}", animName);
            return;
        }
        
        // 计算实际速度和实际时间点
        float speed = anim.getLength() / useTime;
        float trueStartTime = startTime / useTime * anim.getLength();
        
        // 播放新动画
        if (channelIds != null) {
            playAnimInner(animName, blendTime, loop, speed, trueStartTime, channelIds);
        } else {
            playAnimInner(animName, blendTime, loop, speed, trueStartTime, fullChannelsIds);
        }
        
    }
    
    /**
     * 播放动画
     * @param animName
     * @param blendTime
     * @param loop
     * @param speed
     * @param time
     * @param channelIds 
     */
    private void playAnimInner(String animName, float blendTime
            , LoopMode loop, float speed, float time,  String... channelIds) {
        Channel channel;
        for (String cid : channelIds) {
            channel = getChannel(cid);
            if (channel == null) {
                LOG.log(Level.WARNING, "Channel not found,channelId={0}", cid);
                continue;
            }
            channel.playAnim(animName, blendTime, loop, speed, time);
        }
        resetState = false;
    }

    @Override
    public void reset() {
        for (Channel channel : channels) {
            channel.reset();
        }
        resetState = true;
    }

    @Override
    public void resetToAnimationTime(String anim, float timeInter) {
        for (Channel ch : channels) {
            ch.resetToAnimationTime(anim, timeInter);
        }
        resetState = true;
    }
    
    @Override
    public void setSpeed(float speed) {
        // 避免除0及不正常的速度
        if (speed <= 0) {
            speed = 0.0001f;
        }
        for (Channel ch : channels) {
            ch.updateSpeed(speed);
        }
    }

    @Override
    public boolean isReset() {
        return resetState;
    }

    @Override
    public void setChannelLock(boolean locked, String... channelIds) {
        if (channelIds == null) {
            return;
        }
        Channel ch;
        for (String chId : channelIds) {
            ch = getChannel(chId);
            if (ch != null) {
                ch.setLock(locked);
            }
        }
    }

    @Override
    public void restoreAnimation(String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        String[] ids = channelIds != null ? channelIds : fullChannelsIds;
        Temp tp = Temp.get();
        // 存放所有需要fix的通道，哪些与animName相同名称的通道不需要修复
        List<Channel> needFixs = tp.list1;
        needFixs.clear();
        // 先找出一个标准通道，其它通道将同步fix到这一个通道，如果没有找到这样一个
        // 通道，则把全部通道都重新启动
        AnimChannel sampleChannel = null;
        
        for (String id : ids) {
            Channel tempChannel = getChannel(id);
            if (tempChannel == null) {
                continue;
            }
            if (animName.equals(tempChannel.getAnimationName())) {
                sampleChannel = tempChannel.getAnimChannel();
            } else {
                needFixs.add(tempChannel);
            }
        }
        
        if (sampleChannel != null) {
            for (Channel ch : needFixs) {
                ch.playAnim(animName, DEFAULT_BLEND_TIME
                        , sampleChannel.getLoopMode()
                        , sampleChannel.getSpeed()
                        , sampleChannel.getTime());
            }
        } else {
            // 如果没有sampleChannel则直接重启所有通道就可以。
            playAnim(animName, loop, useTime, startTime, ids);
        }
        
        tp.release();
    }

    
}
