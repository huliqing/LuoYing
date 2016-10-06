/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.channel;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import name.huliqing.ly.data.ChannelData;

/**
 * 用于封装JME3的动画通道
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractChannel <T extends ChannelData> implements Channel <T> {
    
    protected T data;
    private String[] fromRootBones;
    private String[] toRootBones;
    private String[] bones;
    
    // 动画通道是否被锁定
    protected boolean locked;
    // 动画控制器
    protected AnimControl animControl;
    // JME的原始动画通道
    protected AnimChannel animChannel;
    
    @Override
    public void setData(T data) {
        this.data = data;
        fromRootBones = data.getAsArray("fromRootBones");
        toRootBones = data.getAsArray("toRootBones");
        bones = data.getAsArray("bones");
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void setAnimControl(AnimControl animControl) {
        this.animControl = animControl;
        this.animChannel = createAnimChannel(this.animControl);
    }

    @Override
    public void playAnim(String animName, LoopMode loop, float speed, float time, float blendTime) {
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
     * @param ac
     * @return 
     */
    protected final AnimChannel createAnimChannel(AnimControl ac) {
        AnimChannel channel = ac.createChannel();
        
        // 如果未配置其它任何参数，则默认创建一个完全通道
        if (fromRootBones == null && toRootBones == null && bones == null) {
            return channel;
        }
        
        // From root bones
        if (fromRootBones != null) {
            for (String bone : fromRootBones) {
                channel.addFromRootBone(bone);
            }
        }
        
        // To root bones
        if (toRootBones != null) {
            for (String bone : toRootBones) {
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
