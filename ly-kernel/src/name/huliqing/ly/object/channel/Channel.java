/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.channel;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import name.huliqing.ly.data.ChannelData;
import name.huliqing.ly.xml.DataProcessor;

/**
 * 角色动画通道
 * @author huliqing
 * @param <T>
 */
public interface Channel<T extends ChannelData> extends DataProcessor<T>{

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 获得通道ID标识
     * @return 
     */
    String getId();
    
    /**
     * 设置AnimControl控制器
     * @param animControl 
     */
    void setAnimControl(AnimControl animControl);
    
    /**
     * 执行动画。
     * @param animName 动画名称
     * @param loop 循环模式
     * @param speed 动画的执行速度
     * @param time 动画的开始时间点，单位秒.该开始时间是以useTime为依据的,需要转换
     * 为动画的实际开始时间点。该参数影响动画的实际播放时间，比如useTime=4,
     * startTime=1,则实际动画时间为4-1=3秒。
     * @param blendTime
     */
    void playAnim(String animName, LoopMode loop, float speed, float time, float blendTime);
    
    /**
     * 更新当前动画动道的循环模式
     * @param loop 
     */
    void updateLoopMode(LoopMode loop);
    
    /**
     * 更新当前动画通道的速度
     * @param speed 
     */
    void updateSpeed(float speed);
    
    /**
     * 获取通道当前正在执行的动画,如果没有正在执行的动画则返回null.
     * @return 
     */
    String getAnimationName();
    
    /**
     * 把骨骼动画定位在当前所播放动画的第一帧处．
     * 可使用该方法来使角色停止活动。如：当角色没有“死亡”动画时，
     * 角色在死后需要停止活动，则可使用该方法来停止正在执行的动画。
     * @see #resetToAnimationTime(java.lang.String, float) 
     */
    void reset();
    
    /**
     * 把骨骼动画定位在某一个动画中的某一个时间点(帧)．
     * @param anim 动画名称 
     * @param timeInter 
     */
    void resetToAnimationTime(String anim, float timeInter);

    /**
     * 锁定动画通道，使它不能执行动画
     * @param locked 
     */
    void setLock(boolean locked);
    
    /**
     * 判断动画通道是否被锁定。
     * @return 
     */
    boolean isLocked();
    
    /**
     * 获取内部channel
     * @return 
     */
    AnimChannel getAnimChannel();
   
}
