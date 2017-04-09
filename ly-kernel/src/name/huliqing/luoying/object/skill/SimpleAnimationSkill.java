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
package name.huliqing.luoying.object.skill;

import com.jme3.animation.LoopMode;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.ChannelModule;

/**
 * 基本动画技能，这种技能绑定一个角色动画。并可以指定使用哪些通道执行动画。
 * @author huliqing
 */
public class SimpleAnimationSkill extends AbstractSkill {
    private ChannelModule channelModule;
    
    /** 技能动画名称 */
    protected String animation;
    
    /** 执行这个技能的角色动画通道名称，角色必须配置有这些动画骨骼通道才有用。*/
    protected String[] channels;
    
    /**
     * 当执行动画时是否锁定动画通道，这可以避免当技能交叉重叠执行时动画通道被互相覆盖。
     * 比如在执行取武器的动画时，这时的手部通道的动画不能被重新执行的“跑路”动画的相关通道覆盖。
     * 被锁定的通道应该在退出技能时(cleanup时)重新解锁，避免其它技能无法使用。
     */
    protected boolean channelLockAll;
    
    /**
     * 指定要锁定那些特定的动画通道,如果指定了channelLockAll则这个参数会被忽略
     */
    protected String[] channelLocks;
    
    // 这两个参数标记useTime中可以剔除掉的<b>最高</b>时间比率.
    // 分别标记可剔除的前面和后面的时间.比如: useTime=5秒,
    // cutTimeStartMax=0.1,cutTimeEndMax=0.1, 则最高允许剔除的时间 = 5 * (0.1 + 0.1) = 1秒
    // cutTime的影响不只是技能的实际使用时间,与speed作用不同的地方在于:speed只会影响动画的
    // 播放速度,但是cutTime除了影响动画速度之外还影响动画长度.cutTimeStart和cutTimeEnd同时会剪裁
    // 掉动画的前面和后面一部分的片段,这可以用于在一些"攻击"招式上去除掉"起招"和"收招"动作,实现"连招"
    // 的效果.
    // 这两个值加起来不应该超过1.0
    protected float cutTimeStartMax;
    protected float cutTimeEndMax;
    
    /** 用于剪裁cutTimeEndMax的角色属性。*/
    protected String bindCutTimeEndAttribute;

    @Override
    public void setData(SkillData data) {
        super.setData(data);
        animation = data.getAsString("animation");
        channels = data.getAsArray("channels");
        channelLockAll = data.getAsBoolean("channelLockAll", false);
        channelLocks = data.getAsArray("channelLocks");
        
        // 时间\动画剪裁参数
        cutTimeStartMax = data.getAsFloat("cutTimeStartMax", 0);
        cutTimeEndMax = data.getAsFloat("cutTimeEndMax", 0);
        
        // CutTimeEnd的剪裁
        bindCutTimeEndAttribute = data.getAsString("bindCutTimeEndAttribute");
    }
    
    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        channelModule = actor.getModuleManager().getModule(ChannelModule.class);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        // 1.start animation
        // 注B：cutTime只影响动画的开始时间点，动画的结束时间点不需要设置。
        // 结束时间点只受“是否有后续技能”的影响，
        // 1.如果有后续技能，则当前技能时间结束后
        // 会立即执行后续技能，所以当前动画会被立即停止（切换到新技能动画）。所以无需手动设置
        // 2.如果没有后续技能，则让当前技能动画自行结束，这也是比较合理的。如武功中的“收式”，如果
        // 没有后续连招，则应该让当前技能动画的“收式”正常播放。
        if (animation != null) {
            doUpdateAnimation(animation
                    , loop
                    , getAnimFullTime()
                    , 0);
        }
    }
    
    @Override
    public void cleanup() {
        // 如果有锁定过动画通道则必须解锁，否则角色的动画通道将不能被其它技能使用。
        if (channelLockAll) {
            channelModule.setChannelLock(false, null);
        } else if (channelLocks != null) {
            channelModule.setChannelLock(false, channelLocks);
        }
        super.cleanup();
    }
    

    @Override
    protected void doSkillUpdate(float tpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void restoreAnimation() {
        if (animation != null) {
            channelModule.restoreAnimation(animation, channels
                    , loop ? LoopMode.Loop : LoopMode.DontLoop
                    , getAnimFullTime(), 0);
        }
    }
    
    /**
     * 执行动画
     * @param animation 动画名称
     * @param loop
     * @param animFullTime 动画的完整执行时间
     * @param animStartTime 指定动画的起始执行时间 
     */
    protected void doUpdateAnimation(String animation, boolean loop
            , float animFullTime, float animStartTime) {
        channelModule.playAnim(animation
                , channels
                , loop ? LoopMode.Loop : LoopMode.DontLoop
                , animFullTime
                , animStartTime
        );
        if (channelLockAll) {
            channelModule.setChannelLock(true, null);
        } else if (channelLocks != null) {
            channelModule.setChannelLock(true, channelLocks);
        }
    }

//    /**
//     * 获取技能的CutTimeEndRate,这个值是对技能执行时间的剪裁，即对技能的结束阶段
//     * 的时间进行剪裁，这个值受角色属性影响，并且不会大于CutTimeEndMax.
//     * 如果技能没有指定影响该值的角色属性，或者角色没有指定的属性值，则这个值应
//     * 返回0.<br >
//     * 注：这个值返回的是一个比率，取值为[0.0,1.0]之间，即表示要剪裁掉的技能总时间
//     * 的比率。例如：当返回值为0.5时，即表示技能的总执行时间要剪裁掉一半（时间的后半部分）
//     * @return 
//     */
//    private float getCutTimeEndRate() {
//        float cutTime = 0;
//        if (bindCutTimeEndAttribute != null) {
//            cutTime = (cutTimeEndMax * MathUtils.clamp(getNumberAttributeValue(actor, bindCutTimeEndAttribute, 0), 0, 1.0f));
//        }
//        return cutTime;
//    }
    
}
