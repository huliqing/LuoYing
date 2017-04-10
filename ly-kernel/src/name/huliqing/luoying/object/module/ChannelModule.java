/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.module;
 
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.ChannelData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.AssetLoader;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.channel.Channel;
import name.huliqing.luoying.object.channel.ChannelControl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.utils.Temp;

/**
 * 用于控制角色动画通道的“通道控制器”
 * @author huliqing 
 */
public class ChannelModule extends AbstractModule implements ChannelControl {
    private static final Logger LOG = Logger.getLogger(ChannelModule.class.getName());

    // 角色的扩展动画的目录,如：Models/actor/ext_anim
    private String extAnimDir;
    
    // ---- inner
    // 通道处理器列表
    private final SafeArrayList<Channel> channels = new SafeArrayList<Channel>(Channel.class);
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
    public void setData(ModuleData data) {
        super.setData(data);
        extAnimDir = data.getAsString("extAnimDir");
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        for (Channel c : channels.getArray()) {
            c.updateDatas();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        animControl = entity.getSpatial().getControl(AnimControl.class);
        
        List<ChannelData> ods = entity.getData().getObjectDatas(ChannelData.class, new ArrayList<ChannelData>());
        if (ods != null && !ods.isEmpty()) {
            for (ChannelData od : ods) {
                addChannel((Channel) Loader.load(od));
            }
        }
        
        //  如果角色没有指定任何通道则自动创建一个默认的全通道
        if (channels.isEmpty()) {
            addChannel((Channel) Loader.load(IdConstants.SYS_CHANNEL_FULL));
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
        // 注：如果角色不存在AnimControl则添加通道就没有意义，还会报错
        if (animControl == null || channels.contains(channel))
            return;
        
        channels.add(channel);
        entity.getData().addObjectData(channel.getData());
        channel.initialize(this, animControl);

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
        for (Channel ch : channels.getArray()) {
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
    public void playAnim(String animName, String[] channelIds, LoopMode loop, float useTime, float startTime) {
        playAnim(animName, channelIds, loop, useTime, startTime, DEFAULT_BLEND_TIME);
    }
    
    @Override
    public void playAnim(String animName, String[] channelIds, LoopMode loop, float useTime, float startTime, float blendTime) {
        if (!initialized) {
            return;
        }
        if (animControl == null) {
            return;
        }
        
        // 检查动画是否存在
        checkAndLoadAnim(animName);
        
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
     * 检查是否存在指定的骨骼动画,如果没有则偿试载入该动画。如果找不到动画文件
     * 则返回false, 如果已经存在动画或载入动画成功则返回true.
     * @param animName　动画名称
     * @return 
     */
    public boolean checkAndLoadAnim(String animName) {
        if (animName == null || animControl == null) {
            return false;
        }
        if (animControl.getAnim(animName) != null) {
            return true;
        } else {
            return loadExtAnim(entity, animName);
        }
    }
    
    /**
     * 载入扩展的动画,该方法从角色所配置的extAnim目录中查找动画文件并进行加
     * 载。
     * @param actor
     * @param animName
     * @return 
     */
    private boolean loadExtAnim(Entity actor, String animName) {
        if (extAnimDir == null) {
            LOG.log(Level.WARNING, "Entity {0} no have a extAnim defined"
                    + ", could not load anim {1}", new Object[] {actor.getData().getId(), animName});
            return false;
        }
        String animFile = extAnimDir + "/" + animName + ".mesh.j3o";
        try {
            Spatial animExtModel = AssetLoader.loadModel(animFile);
            GeometryUtils.addSkeletonAnim(animExtModel, actor.getSpatial());
            return true;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not load extAnim, actor={0}, animName={1}, exception={2}"
                    , new Object[] {actor.getData().getId(), animName, e.getMessage()});
        }
        return false;
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
            channel.playAnim(animName, loop, speed, time, blendTime);
        }
        resetState = false;
    }

    @Override
    public void reset() {
        for (Channel channel : channels.getArray()) {
            channel.reset();
        }
        resetState = true;
    }

    @Override
    public void resetToAnimationTime(String anim, float timeInter) {
        checkAndLoadAnim(anim);
        for (Channel ch : channels.getArray()) {
            ch.resetToAnimationTime(anim, timeInter);
        }
        resetState = true;
    }
    
    @Override
    public void setSpeed(float speed) {
        // 避免除0及不正常的速度
        if (speed <= 0) {
            speed = 0.00001f;
        }
        for (Channel ch : channels.getArray()) {
            ch.updateSpeed(speed);
        }
    }

    @Override
    public boolean isReset() {
        return resetState;
    }

    @Override
    public void setChannelLock(boolean locked, String[] channelIds) {
        if (channels == null) {
            return;
        }
        // 如果没有指定要锁定那些通道，则默认为全部通道
        if (channelIds == null) {
            for (Channel c : channels.getArray()) {
                c.setLock(locked);
            }
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
    public void restoreAnimation(String animName, String[] channelIds, LoopMode loop, float useTime, float startTime) {
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
                ch.playAnim(animName
                        , sampleChannel.getLoopMode()
                        , sampleChannel.getSpeed()
                        , sampleChannel.getTime()
                        , DEFAULT_BLEND_TIME);
            }
        } else {
            // 如果没有sampleChannel则直接重启所有通道就可以。
            playAnim(animName, ids, loop, useTime, startTime);
        }
        
        tp.release();
    }

    
}
