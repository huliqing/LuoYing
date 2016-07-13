/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.channel;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import name.huliqing.fighter.data.ChannelData;

/**
 * 用于封装JME3的动画通道
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractChannel <T extends ChannelData> implements Channel <T> {
    
    protected T data;
    // 动画通道是否被锁定
    protected boolean locked;
    // 动画控制器
    protected AnimControl animControl;
    // JME的原始动画通道
    protected AnimChannel animChannel;
        
    // remove20160713
//    public AbstractChannel(AnimControl animControl) {
//        this.data = data;
//        this.animControl = animControl;
//        this.animChannel = createAnimChannel(data, animControl);
//    }
    
    @Override
    public void initData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void setAnimControl(AnimControl animControl) {
        this.animControl = animControl;
        this.animChannel = createAnimChannel(this.data, this.animControl);
    }

    @Override
    public void playAnim(String animName, float blendTime, LoopMode loop, float speed, float time) {
        if (locked) {
            return;
        }
        
        // 如果当前正在播放的是循环动画，并且与给定的要播放的动画一致，则不要重新从头播放起，
        // 只要更新循环模式和速度就可以。
        // 否则循环动画看起来会有一种被不停切断的感觉
        if (animChannel.getLoopMode() == LoopMode.Loop && animName.equals(animChannel.getAnimationName())) {
            animChannel.setLoopMode(loop);
            animChannel.setSpeed(speed);
            return;
        }
        
        // 注意：setAnim(xx) 必须放在最前面，否则其它参数会被覆盖掉。
        animChannel.setAnim(animName, blendTime);
        animChannel.setLoopMode(loop);
        animChannel.setSpeed(speed);
        animChannel.setTime(time);
    }

    @Override
    public void updateLoopMode(LoopMode loop) {
        animChannel.setLoopMode(loop);
    }

    @Override
    public void updateSpeed(float speed) {
        animChannel.setSpeed(speed);
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getAnimationName() {
        return animChannel.getAnimationName();
    }

    @Override
    public void reset() {
        // 不要使用该reset操作，该方法可能会让动画速度出现异常。
        // 该问题出现在没有“死亡”动画的蜘蛛身上，使用该方法reset后，
        // 通过recycleManager 回收再利用的时候有一定机率出现"走路”动画不正常
        // animChannel.reset(true);
        
        // 使用speed=0.0001来代替reset更合理.
        // 注意不要把speed设置为0，这会导致除0出错。使用一个接近0的值就可以。
        animChannel.setSpeed(0.0001f);
        animChannel.setTime(0);
    }

    @Override
    public void resetToAnimationTime(String anim, float timeInter) {
        animChannel.setAnim(anim);
        animChannel.setSpeed(0);
        animChannel.setTime(animChannel.getAnimMaxTime() * timeInter);
    }

    @Override
    public void setLock(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public AnimChannel getAnimChannel() {
        return animChannel;
    }

    /**
     * 创建一个AnimChannel
     * @param channelData
     * @param ac
     * @return 
     */
    protected final AnimChannel createAnimChannel(ChannelData channelData, AnimControl ac) {
        AnimChannel channel = ac.createChannel();
        String[] fromRoot = channelData.getFromRootBones();
        String[] toRoot = channelData.getToRootBones();
        String[] bones = channelData.getBones();
        
        // 如果未配置其它任何参数，则默认创建一个完全通道
        if (fromRoot == null && toRoot == null && bones == null) {
            return channel;
        }
        
        // From root bones
        if (fromRoot != null) {
            for (String bone : fromRoot) {
                channel.addFromRootBone(bone);
            }
        }
        
        // To root bones
        if (toRoot != null) {
            for (String bone : toRoot) {
                channel.addToRootBone(bone);
            }
        }
        
        // Other single bones.
        if (bones != null) {
            for (String bone : bones) {
                channel.addBone(bone);
            }
        }
        
        return channel;
    }
}
