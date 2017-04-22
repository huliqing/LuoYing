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
import com.jme3.math.FastMath;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actoranim.ActorAnim;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.impl.EffectEntity;
import name.huliqing.luoying.object.magic.Magic;
import name.huliqing.luoying.object.module.ChannelModule;
import name.huliqing.luoying.object.sound.SoundManager;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * 基本动画技能，这种技能绑定一个角色动画。并可以指定使用哪些通道执行动画。
 * @author huliqing
 */
public class SimpleAnimationSkill extends AbstractSkill {
    private ChannelModule channelModule;
    
    /** 技能动画名称 */
    protected String animation;

    /** 技能的使用时间,单位秒*/
    protected float useTime = 1.0f;

    /** 让技能循环执行 */
    protected boolean loop;
    
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
    
    /** 技能声效 */
    protected SoundWrap[] sounds;
    
    /** 技能特效: 直接绑定在角色身上的特效 */
    protected EffectWrap[] effects;
    
    /** 技能特效：绑定在角色某根骨骼上的特效 */
    protected EffectWrap[] boneEffects;
    
    /** 关联一些魔法物体，这些魔法物体会在角色施放技能的时候放置在角色所在的位置上,根据\魔法物体的设置*/
    protected MagicWrap[] magics;
    
    /** 角色动画 */
    protected ActorAnimWrap[] actorAnims;
    
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
    
    // 实际的技能使用时间。
    protected float trueUseTime;

    @Override
    public void setData(SkillData data) {
        super.setData(data);
        useTime = data.getAsFloat("useTime", useTime);
        animation = data.getAsString("animation");
        channels = data.getAsArray("channels");
        channelLockAll = data.getAsBoolean("channelLockAll", false);
        channelLocks = data.getAsArray("channelLocks");
        loop = data.getAsBoolean("loop", false);
        
        // Sounds 参数格式: "soundId|timePoint,soundId|timePoint..."
        String[] tempSounds = data.getAsArray("sounds");
        if (tempSounds != null) {
            sounds = new SoundWrap[tempSounds.length];
            for (int i = 0; i < tempSounds.length; i++) {
                String[] soundArr = tempSounds[i].split("\\|");
                SoundWrap sw = new SoundWrap();
                sw.soundId = soundArr[0];
                if (soundArr.length >= 2) {
                    sw.timePoint = ConvertUtils.toFloat(soundArr[1], 0f);
                }
                sounds[i] = sw;
            }
        }
        
        // Effects, 格式: "effectId|timePoint,effect|timePoint,effectId|timePoint..."
        String[] tempEffects = data.getAsArray("effects");
        if (tempEffects != null) {
            effects = new EffectWrap[tempEffects.length];
            for (int i = 0; i < tempEffects.length; i++) {
                String[] effectArr = tempEffects[i].split("\\|");
                EffectWrap ew = new EffectWrap();
                ew.effectId = effectArr[0];
                ew.boneName = null; // 绑定在骨骼上的特效才要设置这个参数
                if (effectArr.length >= 2) {
                    ew.timePoint = ConvertUtils.toFloat(effectArr[1], 0f);
                }
                effects[i] = ew;
            }
        }
        
        // boneEffects, 格式: "boneName|effectId|timePoint,boneName|effectId|timePoint,boneName|effectId|timePoint..."
        String[] tempBoneEffects = data.getAsArray("boneEffects");
        if (tempBoneEffects != null) {
            boneEffects = new EffectWrap[tempBoneEffects.length];
            for (int i = 0; i < tempBoneEffects.length; i++) {
                String[] boneEffectArr = tempBoneEffects[i].split("\\|");
                EffectWrap ew = new EffectWrap();
                ew.boneName = boneEffectArr[0];
                ew.effectId = boneEffectArr[1];
                if (boneEffectArr.length >= 3) {
                    ew.timePoint = ConvertUtils.toFloat(boneEffectArr[2], 0f);
                }
                boneEffects[i] = ew;
            }
        }
        
        // Magics, 格式: "magicId|timePoint,magic|timePoint,magicId|timePoint..."
        String[] tempMagics = data.getAsArray("magics");
        if (tempMagics != null) {
            magics = new MagicWrap[tempMagics.length];
            for (int i = 0; i < tempMagics.length; i++) {
                String[] magicArr = tempMagics[i].split("\\|");
                MagicWrap mw = new MagicWrap();
                mw.magicId = magicArr[0];
                if (magicArr.length >= 2) {
                    mw.timePoint = ConvertUtils.toFloat(magicArr[1], 0f);
                }
                magics[i] = mw;
            }
        }
        
        // Motions, 格式: actorAnimId|timeStart|timeEnd,actorAnimId|timeStart|timeEnd
        String[] tempActorAnims = data.getAsArray("actorAnims");
        if (tempActorAnims != null) {
            actorAnims = new ActorAnimWrap[tempActorAnims.length];
            for (int i = 0; i < tempActorAnims.length; i++) {
                String[] actorAnimArr = tempActorAnims[i].split("\\|");
                ActorAnimWrap aaw = new ActorAnimWrap();
                aaw.actorAnim = Loader.load(actorAnimArr[0]);
                if (actorAnimArr.length >= 2) {
                    aaw.timePointStart = ConvertUtils.toFloat(actorAnimArr[1], 0);
                }
                if (actorAnimArr.length >= 3) {
                    aaw.timePointEnd = ConvertUtils.toFloat(actorAnimArr[2], 1);
                }
                actorAnims[i] = aaw;
            }
        }
        
        // 时间\动画剪裁参数
        cutTimeStartMax = data.getAsFloat("cutTimeStartMax", 0);
        cutTimeEndMax = data.getAsFloat("cutTimeEndMax", 0);
        
        // CutTimeEnd的剪裁
        bindCutTimeEndAttribute = data.getAsString("bindCutTimeEndAttribute");
    }
    
    @Override
    public void setActor(Entity actor) {
        super.setActor(actor);
        channelModule = actor.getModule(ChannelModule.class);
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
            doUpdateAnimation(animation, loop, getAnimFullTime(), 0);
        }
        
        // 计算技能的实际使用时间
        trueUseTime = getTrueUseTime();
        
        float trueSpeed = getSpeed();
        
        // 计算实际的执行时间点，受cutTime影响
        if (sounds != null) {
            for (SoundWrap sw : sounds) {
                sw.trueTimePoint = fixTimePointByCutTime(sw.timePoint);
            }
        }
        if (effects != null) {
            for (EffectWrap ew : effects) {
                ew.trueTimePoint = fixTimePointByCutTime(ew.timePoint);
            }
        }
        if (boneEffects != null) {
            for (EffectWrap ew : boneEffects) {
                ew.trueTimePoint = fixTimePointByCutTime(ew.timePoint);
            }
        }
        if (magics != null) {
            for (MagicWrap mw : magics) {
                mw.trueTimePoint = fixTimePointByCutTime(mw.timePoint);
            }
        }
        if (actorAnims != null) {
            for (ActorAnimWrap aaw : actorAnims) {
                aaw.trueTimePointStart = fixTimePointByCutTime(aaw.timePointStart);
                aaw.trueTimePointEnd = fixTimePointByCutTime(aaw.timePointEnd);
            }
        }
        
    }
    
    @Override
    public void cleanup() {
        // 清理声效
        if (sounds != null) {
            for (SoundWrap sw : sounds) {
                sw.cleanup();
            }
        }
        // 清理效果
        if (effects != null) {
            for (EffectWrap ew : effects) {
                ew.cleanup();
            }
        }
        if (boneEffects != null) {
            for (EffectWrap ew : boneEffects) {
                ew.cleanup();
            }
        }
        if (magics != null) {
            for (MagicWrap mw : magics) {
                mw.cleanup();
            }
        }
        if (actorAnims != null) {
            for (ActorAnimWrap aaw : actorAnims) {
                aaw.cleanup();
            }
        }
        
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
        float interpolation = time / trueUseTime;
        if (interpolation > 1) {
            interpolation = 1;
        }
        
        // 2.update sounds
        if (sounds != null) {
            for (SoundWrap sw : sounds) {
                if (sw.started) continue;
                sw.update(interpolation);
            }
        }
        
        // 3.update effects
        if (effects != null) {
            for (EffectWrap ew : effects) {
                if (ew.started) continue;
                ew.update(interpolation);
            }
        }
        
        if (boneEffects != null) {
            for (EffectWrap ew : boneEffects) {
                if (ew.started) continue;
                ew.update(interpolation);
            }
        }
        
        // 4.update magics
        if (magics != null) {
            for (MagicWrap mw : magics) {
                if (mw.started) continue;
                mw.update(interpolation);
            }
        }
        
        // 5.update force;
        if (actorAnims != null) {
            for (ActorAnimWrap aaw : actorAnims) {
                aaw.update(tpf, interpolation);
            }
        }
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
    
    @Override
    public boolean isEnd() {
        if (loop) {
            return false;
        }
        return time >= useTime;
    }
    
    /**
     * 获取"动画"的完整执行时间,注：动画的完整时间并不等于动画的实际执行时间,
     * 实际动画的执行时间受cutTime的影响。该值返回: useTime / speed.
     * 设置动画播放时应该使用这个时间为准。
     * @return 
     */
    protected float getAnimFullTime() {
        return useTime / getSpeed();
    }
    
    public float getTrueUseTime() {
        // 最终的实际运行时间是cutTime后的时间。
        float tempTime = useTime / getSpeed();
        
        // remove20170409
//        // 注：因为暂不开放cutTimeStart，所以cutTimeStart目前为0
////        return tempTime - tempTime * (cutTimeStart + getCutTimeEndRate(actor, skillData));
//        return tempTime - tempTime * (0 + getCutTimeEndRate());

        return tempTime;
    }
    
        /**
     * 重新计算受cutTime影响的时间插值点。
     * @param interpolation 原始的时间插值点(未受cutTime影响的时间插值)
     * @return 经过cutTime计算后的实际的时间插值点
     */
    protected float fixTimePointByCutTime(float interpolation) {
//        float cutTimeStart = data.getCutTimeStart();
//        float cutTimeEnd = data.getCutTimeEnd();
        float cutTimeStart = 0;
        float cutTimeEnd = 0;
        float result = FastMath.clamp((interpolation - cutTimeStart) / (1.0f - cutTimeStart - cutTimeEnd)
                , 0f, 1.0f);
        return result;
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
    
    // 声音控制
    public class SoundWrap {
        String soundId;
        float timePoint; // 未受cutTime影响的时间点,在xml中配置的
        float trueTimePoint; // 实际的时间点（动态的），受cutTime影响
        boolean started;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= trueTimePoint) {
                SoundManager.getInstance().playSound(soundId, actor.getSpatial().getWorldTranslation());
                started = true;
            }
        }
        
        void cleanup() {
            started = false;
        }
    }
    
    // 效果更新控制
    public class EffectWrap {
        // 效果ID
        String effectId;
        // 效果要绑定的骨骼名称，如果不绑定到骨骼则这个参数可以为null.
        String boneName;
        // 效果的开始播放时间点,这个时间是技能时间(trueUseTime)的插值点
        float timePoint;
        
        // 实际的效果执行时间插值点，受cutTime影响
        float trueTimePoint;
        // 标记效果是否已经开始
        boolean started;
        // 保持对特效实例的引用
        EffectEntity effectEntity;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= trueTimePoint) {
                playEffect();
                started = true;
            }
        }
        
        void playEffect() {
            EntityData effectEntityData = Loader.loadData(IdConstants.SYS_ENTITY_EFFECT);
            EffectData effectData = Loader.loadData(effectId);
            effectEntityData.setAttribute("effect", effectData);
            effectEntity = Loader.load(effectEntityData);
            effectEntity.setTraceEntity(actor);
            effectEntity.setTraceBone(boneName);
            effectEntity.getEffect().setSpeed(getSpeed()); // 效果需要同步与技能相同的执行速度
            actor.getScene().addEntity(effectEntity);
        }
        
        void cleanup() {
            if (effectEntity != null) {
                effectEntity.requestEnd();
                effectEntity = null;
            }
            started = false;
        }
    }
    
    // Magic更新控制
    public class MagicWrap {
        // 魔法ID
        String magicId;
        // 魔法开始播放时间点,这个时间是技能时间(trueUseTime)的插值点
        float timePoint;
        // 实际的魔法执行时间插值点，受cutTime影响
        float trueTimePoint;
        // 标记效果是否已经开始
        boolean started;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= trueTimePoint) {
                MagicData md = Loader.loadData(magicId);
                md.setSource(actor.getEntityId());
                md.setLocation(actor.getSpatial().getWorldTranslation());
                Magic magic = Loader.load(md);
                actor.getScene().addEntity(magic);
                
                // 标记效果已经开始
                started = true;
            }
        }
        
        void cleanup() {
            started = false;
        }
    }
    
    public class ActorAnimWrap {
        // 原始的时间开始点和结束点
        float timePointStart;
        float timePointEnd = 1;
        // 实际时间点
        float trueTimePointStart; 
        float trueTimePointEnd = 1;
        // 运动处理器
        ActorAnim actorAnim;
        boolean started;
        
        void update(float tpf, float interpolation) {
            if (started) {
                actorAnim.update(tpf);
                return;
            }
            if (interpolation >= trueTimePointStart) {
                actorAnim.setTarget(actor);
                // 计算出实际的动画时间
                actorAnim.setUseTime((trueTimePointEnd - trueTimePointStart) * getTrueUseTime());
                actorAnim.start();
                started = true;
            }
        }
      
        void cleanup() {
            if (!actorAnim.isEnd()) {
                actorAnim.cleanup();
            }
            started = false;
        }
    }
}
