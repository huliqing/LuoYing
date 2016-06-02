/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.channel;

import com.jme3.animation.LoopMode;
import java.util.List;

/**
 * 角色动画通道处理器，一个角色可能存在多个动画通道，也可能没有任何动画通道
 * ，或者只有一个默认通道，也可能没有默认通道，而只有几个分别的通道。
 * @author huliqing
 */
public interface ChannelProcessor {
    
    /**
     * 添加动画通道
     * @param channelId 通道ID
     * @param channel 
     */
    void addChannel(Channel channel);
    
    /**
     * 获取动画通道，如果不存在指定通道则返回null.
     * @param channelId 通道ID
     * @return 
     */
    Channel getChannel(String channelId);
    
    /**
     * 获取角色所有定义的动画通道。
     * @return 
     */
    List<Channel> getChannels();
    
    /**
     * @see #playAnim(java.lang.String, com.jme3.animation.LoopMode, float, float, float, java.lang.String[]) 
     * @param animName 动画名称
     * @param loop 循环模式
     * @param useTime 动画的使用时间,单位秒
     * @param channelIds
     */
    void playAnim(String animName, LoopMode loop, float useTime, String... channelIds);
    
    /**
     * @see #playAnim(java.lang.String, com.jme3.animation.LoopMode, float, float, float, java.lang.String[]) 
     * @param animName
     * @param loop
     * @param useTime
     * @param startTime 
     * @param channelIds
     */
    void playAnim(String animName, LoopMode loop, float useTime, float startTime, String... channelIds);
    
    /**
     * 执行动画。
     * @param animName 动画名称
     * @param loop 循环模式
     * @param useTime 动画的使用时间,单位秒
     * @param startTime 动画的开始时间点，单位秒.该开始时间是以useTime为依据的,需要转换
     * 为动画的实际开始时间点。该参数影响动画的实际播放时间，比如useTime=4,
     * startTime=1,则实际动画时间为4-1=3秒。
     * @param blendTime
     * @param channelIds 指定使用哪些通道来执行该动画，如果没有特别指定，则偿试
     * 使用全部通道来执行动画或者使用默认通道来执行动画
     */
    void playAnim(String animName, float blendTime, LoopMode loop
            , float useTime, float startTime, String... channelIds);
        
    /**
     * 恢复动画，有时候当部分通道被打断执行了其它动画之后需要重新回到原来的
     * 动画上。如当角色在走路的时候所有通道都在执行“走路”动画，这时如果执
     * 行抽取武器的动画时，可能手部通道会打断走路所需要的一些通道动画。当抽取
     * 武器完毕之后，手部通道需要重新回到“走路”的动画中以便协调角色走路时的
     * 动画。
     * @param animName
     * @param loop
     * @param useTime
     * @param startTime
     * @param channelIds 
     */
    void restoreAnimation(String animName, LoopMode loop, float useTime, float startTime, String... channelIds);
    
    /**
     * 把骨骼动画定位在当前所播放动画的第一帧处．
     * 可使用该方法来使角色停止活动。如：当角色没有“死亡”动画时，
     * 角色在死后需要停止活动，则可使用该方法来停止正在执行的动画。
     * @see #resetToAnimationTime(java.lang.String, float) 
     */
    void reset();
    
    /**
     * 判断是否处于reset状态
     * @return 
     */
    boolean isReset();
    
    /**
     * 把骨骼动画定位在某一个动画中的某一个时间点(帧)．
     * @param anim 动画名称
     * @param timeInterpolation 定位的时间插值点，取值[0.0~1.0] 
     */
    void resetToAnimationTime(String anim, float timeInterpolation);
    
    /**
     * 设置当前所有通道动画的播放速度，注意: speed不能设置为0.只能设置为
     * 一个极小的值来代替0,否则可能会出现"除0"错误。
     * @param speed 
     */
    public void setSpeed(float speed);
    
    /**
     * 锁定或解锁指定的动画通道，当一个动画通道被锁定之后将不能再执行任何动画，包
     * 括reset，除非重新进行解锁
     * @param locked true锁定通道，false解锁通道
     * @param channelIds 指定的通道列表，如果为null或指定的通道不存在则什么也不做。
     */
    void setChannelLock(boolean locked, String... channelIds);

}
