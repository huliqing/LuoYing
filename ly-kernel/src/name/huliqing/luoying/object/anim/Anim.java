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
package name.huliqing.luoying.object.anim;

import name.huliqing.luoying.data.AnimData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 动画功能接口
 * @author huliqing
 * @param <E>
 */
public interface Anim<E> extends DataProcessor<AnimData>{

    @Override
    public void setData(AnimData data);

    @Override
    public AnimData getData();
    
    /**
     * @see #setTarget(java.lang.Object) 
     * @return 
     */
    E getTarget();
    
    /**
     * 设置动画所执行的目标对象.
     * @param target 
     */
    void setTarget(E target);
    
    /**
     * 开始执行动画
     */
    void start();
    
    /**
     * 设置暂停当前动画或继续执行
     * @param paused 
     */
    void pause(boolean paused);
    
    /**
     * 自动更新动画逻辑。
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 将动画更新到指定的插值位置进行显示,如：interpolation设置为0.5f,则动画将显示在一半的位置处。
     * @param interpolation 
     */
    void display(float interpolation);
    
    /**
     * 清理当前动画，以便下次执行。当动画正常结束或者被打断，该方法都应该执行。
     */
    void cleanup();
    
    /**
     * @see #setSpeed(float) 
     * @return 
     */
    float getSpeed();
    
    /**
     * 设置动画速度倍率，大于1为加速，小于1为降速，默认情况下，动画会在1秒
     * 内完成执行。如果速度设置为2，则动画会在0.5秒内完成执行。如果设置为
     * 0.5则动画会在2秒内完成执行。
     * @param speed 
     */
    void setSpeed(float speed);
    
    /**
     * 获取动画的执行时间，注：该时间是在动画标准速度为1的情况下的时间。如果速度
     * 不等于1，则动画的实际运行时间与该时间会不一样。realTime = useTime / speed
     * @return 
     */
    float getUseTime();
    
    /**
     * 设置动画的执行时间，该时间是在标准速度为1的情况下的时间
     * @param useTime 
     * @see #getUseTime() 
     */
    void setUseTime(float useTime);
    
    /**
     * 获取当前的动画最终插值，注意：这个插值不是“时间”插值，而是根据动画的实际情况计算出的最终插值，这个插值可能
     * 受各种因素影响，比如动画运动类型。
     * @return 
     */
    float getInterpolation();
    
    /**
     * 获取动画的循环模式
     * @return 
     */
    Loop getLoop();
    
    /**
     * 设置动画的循环模式，默认为dontloop.
     * @param loop 
     */
    void setLoop(Loop loop);
    
    /**
     * 判断<b>当前</b>动画是否以反向执行。默认情况下动画总是以正向执行。
     * 只有loop为cycle方式的动画才有反向的执行，类似于电影的倒带。
     * @return 
     */
    boolean isInverse();
    
    /**
     * 判断当前动画是否暂停中
     * @return 
     */
    boolean isPaused();
    
    /**
     * 判断当前动画是否结束，未在执行或者已经执行结束都应该返回true.
     * @return 
     */
    boolean isEnd();
    
    /**
     * 添加动画侦听器
     * @param listener 
     */
    void addListener(Listener listener);
    
    /**
     * 移除侦听器
     * @param listener
     * @return 
     */
    boolean removeListener(Listener listener);
}
