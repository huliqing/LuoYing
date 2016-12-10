/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.skill;

import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.AttributeUse;
import name.huliqing.luoying.data.MagicData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.actoranim.ActorAnim;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.effect.Effect;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.magic.Magic;
import name.huliqing.luoying.object.module.ChannelModule;
import name.huliqing.luoying.object.module.SkinModule;
import name.huliqing.luoying.object.sound.SoundManager;
import name.huliqing.luoying.utils.ConvertUtils;
import name.huliqing.luoying.utils.MathUtils;
 
/**
 * 技能的基类。
 * 
 * 注意：影响技能执行时间和速度的三个重要因素：
 * 1.useTime
 * 2.speed;
 * 3.cutTimeStart,cutTimeEnd
 * 默认情况下：useTime开放给外部进行固定配置，而speed和cutTimeStart,cutTimeEnd
 * 作为动态参数进行设置，即在执行技能过程中可动态设置speed和cutTime.
 * 这要求在设置speed和cutTime的时候不应该影响其它时间插值点的协调运行，如：
 * 技能检测点、特效时间点等。
 * 所以，配置技能的时候应该在标准设置(speed=1,cutTimeStart=0,cutTimeEnd=0)下进行.
 * 否则最终可能会产生不协调的现象.
 * 
 * 关于useTime：会影响技能时间、技能速度、动画时间、动画速度
 * 关于speed: 会影响技能时间、技能速度、动画时间、动画速度
 * 关于cutTime: 影响技能时间、动画时间,但不影响技能速度和动画速度
 * @author huliqing
 */
public abstract class AbstractSkill implements Skill {
    private static final Logger LOG = Logger.getLogger(AbstractSkill.class.getName());
    private final ElService elService = Factory.get(ElService.class);
    private ChannelModule channelModule;
    private SkinModule skinModule;
    
    protected SkillData data;
    
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
    
    /** 让技能循环执行 */
    protected boolean loop;
    
    /**
     * 影响技能执行速度的角色属性，指向一个attribute id,默认技能的执行速度为1，当设置了这个值之后，
     * 目标角色的指定属性的值将会影响到技能的执行速度。
    */
    protected String bindSpeedAttribute;
    
    /**
     * 绑定一个防止技能被中断的“概率”属性。
     */
    protected String bindInterruptRateAttribute;

    /**
     * 用于剪裁cutTimeEndMax的角色属性。
     */
    protected String bindCutTimeEndAttribute;
    
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
    
    /** 技能的等级公式，该公式与技能等级（level）可以计算出当前技能的一个等级值。*/
    protected LNumberEl levelEl;
    /** 技能升级等级公式，该公式中的每一个等级值表示每次技能升级时需要的sp数(skillPoints)*/
    protected LNumberEl levelUpEl;
    
    /** checkEl用于检查角色是否可以使用这个技能 */
    protected SBooleanEl checkEl;
    
    // ---- 内部参数 ----
    
    // 当前执行技能的角色
    protected Entity actor;
    
    // 当前技能已经运行的时间。每一次执行该技能或循环时都重置为0
    protected float time;
    
    // 技能是否已经开始运行。
    private boolean initialized;
    
    // 优化性能,这样就不需要在update中不停的去计算trueUseTime
    // 只在start的时候计算一次，在update中不再去计算
    protected float trueUseTime;
    // 优化性能，因为特效的速度需要和技能的速度同步，所以在执行特效的时候也需要
    // 同步设置速度,这个trueSpeed用于缓存技能的实际速度，在每次init的时候计算一次
    // 在执行过程中就不再需要计算。
    protected float trueSpeed;
    
    @Override
    public void setData(SkillData data) {
        this.data = data;
        
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
        animation = data.getAsString("animation");
        channels = data.getAsArray("channels");
        channelLockAll = data.getAsBoolean("channelLockAll", false);
        channelLocks = data.getAsArray("channelLocks");
        loop = data.getAsBoolean("loop", false);
        bindSpeedAttribute = data.getAsString("bindSpeedAttribute");
        bindInterruptRateAttribute = data.getAsString("bindInterruptRateAttribute");
        // CutTimeEnd的剪裁
        bindCutTimeEndAttribute = data.getAsString("bindCutTimeEndAttribute");
        // 时间\动画剪裁参数
        cutTimeStartMax = data.getAsFloat("cutTimeStartMax", 0);
        cutTimeEndMax = data.getAsFloat("cutTimeEndMax", 0);
        // 等级公式
        String levelElStr = data.getAsString("levelEl");
        if (levelElStr != null) {
            levelEl = elService.createLNumberEl(levelElStr);
        }
        String levelUpElStr = data.getAsString("levelUpEl");
        if (levelUpElStr != null) {
            levelUpEl = elService.createLNumberEl(levelUpElStr);
        }
        // el检查器
        String tempCheckEl = data.getAsString("checkEl");
        if (tempCheckEl != null) {
            checkEl = elService.createSBooleanEl(tempCheckEl);
        }
    }

    @Override
    public SkillData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void setActor(Entity actor) {
        this.actor = actor;
        channelModule = actor.getModuleManager().getModule(ChannelModule.class);
        skinModule = actor.getModuleManager().getModule(SkinModule.class);
        if (checkEl != null) {
            checkEl.setSource(actor.getAttributeManager());
        }
    }
    
    @Override
    public Entity getActor() {
        return actor;
    }
    
    @Override
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        trueUseTime = getTrueUseTime();
        trueSpeed = getSpeed();
        
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
        
        // --技能消耗
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                addNumberAttributeValue(actor, ua.getAttribute(), -ua.getAmount());
            }
        }

        // --记录技能执行时间及增加技能点数
        data.setLastPlayTime(LuoYing.getGameTime());
        data.setPlayCount(data.getPlayCount() + 1);
        
        // 检查技能等级提升
        levelUpCheck();
    }
    
    // 检查并升级技能
    private void levelUpCheck() {
        if (data.getLevel() >= data.getMaxLevel()) 
            return;
        if (levelUpEl == null)
            return;
        int levelPoints = levelUpEl.setLevel(data.getLevel() + 1).getValue().intValue();
        if (data.getPlayCount() >= levelPoints) {
            data.setLevel(data.getLevel() + 1);
            data.setPlayCount(data.getPlayCount() - levelPoints);
            levelUpCheck();
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void update(float tpf) {
        if (!initialized) {
            return;
        }
        
        // 检查是否结束
        time += tpf;
        
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
        
        // 6.update logic
        doSkillUpdate(tpf);
        
        if (time >= trueUseTime) {
            if (loop) {
                time = 0;
            } else {
                doSkillEnd();
            }
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
    public void cleanup() {
        // 清理声效播放标记,让声效可以重新播放
        if (sounds != null) {
            for (SoundWrap sw : sounds) {
                sw.cleanup();
            }
        }
        // 清理效果播放标记,让效果可以重新播放
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
        
        // 重置
        time = 0;
        initialized = false;
    }
    
    @Override
    public boolean isEnd() {
        return !initialized;
    }

    @Override
    public boolean isCooldown() {
        return LuoYing.getGameTime() - data.getLastPlayTime() < data.getCooldown() * 1000;
    }

    /**
     * 获取"动画"的完整执行时间,注：动画的完整时间并不等于动画的实际执行时间,
     * 实际动画的执行时间受cutTime的影响。该值返回: useTime / speed.
     * 设置动画播放时应该使用这个时间为准。
     * @return 
     */
    protected float getAnimFullTime() {
        return data.getUseTime() / getSpeed();
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

    @Override
    public void restoreAnimation() {
        if (animation != null) {
            channelModule.restoreAnimation(animation, channels
                    , loop ? LoopMode.Loop : LoopMode.DontLoop
                    , getAnimFullTime(), 0);
        }
    }
    
    /**
     * 获取技能的等级值,由等级公式计算出来，这个方法返回的是当前等级下技能的等级值，
     * 如果技能没有配置等级公式 ，则该方法将返回null.
     * @return 
     */
    protected Number getLevelValue() {
        if (levelEl == null) {
            return null;
        }
        return levelEl.setLevel(data.getLevel()).getValue();
    }
    
    @Override
    public int checkState() {
        // 武器必须取出
        if (data.getWeaponStateLimit() != null) {
            if (!skinModule.isWeaponTakeOn()) {
                return StateCode.SKILL_USE_FAILURE_WEAPON_NEED_TAKE_ON;
            }            
        }
        
        // 冷却中
        if (isCooldown()) {
            return StateCode.SKILL_USE_FAILURE_COOLDOWN;
        }
        
        // 武器状态检查,有一些技能需要拿特定的武器才能执行。
        if (!isPlayableByWeapon()) {
            return StateCode.SKILL_USE_FAILURE_WEAPON_NOT_ALLOW;
        }
        
        // 技能需要消耗一定的属性值，而角色属性值不足
        if (!isPlayableByAttributeLimit()) {
            return StateCode.SKILL_USE_FAILURE_ATTRIBUTE_NOT_ENOUGH;
        }
        
        // EL检查器，例如：属性值不足等
        if (!isPlayableByElCheck()) {
            return StateCode.SKILL_USE_FAILURE_ELCHECK;
        }
        
        return StateCode.SKILL_USE_OK;
    }
    
    @Override
    public boolean canInterruptBySkill(Skill newSkill) {
        if (bindInterruptRateAttribute == null) {
            return true;
        }
        
        NumberAttribute nAttr = actor.getAttributeManager().getAttribute(bindInterruptRateAttribute, NumberAttribute.class);
        float resistRate = nAttr != null ? nAttr.floatValue() : 0;
        if (resistRate <=0) { // 抵抗率为0
            return true;
        }
        if (resistRate >= 1.0f) { // 抵抗率为100%
            return false;
        }
        
        float randomValue = RandomManager.getNextValue();
        return randomValue > resistRate;
    }
    
    @Override
    public boolean isPlayableByWeapon() {
        if (data.getWeaponStateLimit() == null)
            return true;
        
        long weaponState = skinModule.getWeaponState();
        for (long ws : data.getWeaponStateLimit()) {
            if (ws == weaponState) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isPlayableByAttributeLimit() {
        List<AttributeUse> uas = data.getUseAttributes();
        if (uas != null) {
            for (AttributeUse ua : uas) {
                if (getNumberAttributeValue(actor, ua.getAttribute(), 0) < ua.getAmount()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean isPlayableByElCheck() {
        boolean result = checkEl == null || checkEl.getValue();
//        if (Config.debug) {
//            String el = checkEl != null ? checkEl.getData().getId() : null;
//            String expression = checkEl != null ? checkEl.getExpression() : null;
//            LOG.log(Level.INFO, "Playable check by el, result={0}, el={1}, expression={2}, skillId={3}, entity={4}"
//                    , new Object[] {result, el, expression, data.getId(), actor.getData().getId()});
//        }
        return result;
    }
    
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
        Effect effect;
        
        void update(float interpolation) {
            if (started) return;
            if (interpolation >= trueTimePoint) {
                playEffect();
                started = true;
            }
        }

        void playEffect() {
            Spatial traceObject = actor.getSpatial();
            if (boneName != null) {
                SkeletonControl sc = traceObject.getControl(SkeletonControl.class);
                if (sc != null) {
                    try {
                        traceObject = sc.getAttachmentsNode(boneName);
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Bone not found, bone name={0}, skill={1}, actor={2}", new Object[]{boneName, data.getId(), actor.getData().getId()});
                        return;
                    };
                }
            }
            // 效果需要同步与技能相同的执行速度
            effect = Loader.load(effectId);
            effect.setSpeed(trueSpeed);
            effect.setTraceObject(traceObject);
            actor.getScene().addEntity(effect);
        }
        
        void cleanup() {
            if (effect != null) {
                effect.requestEnd();
                effect = null;
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
    
    /**
     * 获取技能的CutTimeEndRate,这个值是对技能执行时间的剪裁，即对技能的结束阶段
     * 的时间进行剪裁，这个值受角色属性影响，并且不会大于CutTimeEndMax.
     * 如果技能没有指定影响该值的角色属性，或者角色没有指定的属性值，则这个值应
     * 返回0.<br >
     * 注：这个值返回的是一个比率，取值为[0.0,1.0]之间，即表示要剪裁掉的技能总时间
     * 的比率。例如：当返回值为0.5时，即表示技能的总执行时间要剪裁掉一半（时间的后半部分）
     * @return 
     */
    private float getCutTimeEndRate() {
        float cutTime = 0;
        if (bindCutTimeEndAttribute != null) {
            cutTime = (cutTimeEndMax * MathUtils.clamp(getNumberAttributeValue(actor, bindCutTimeEndAttribute, 0), 0, 1.0f));
        }
        return cutTime;
    }
    
    /**
     * 获取技能的执行速度,技能的执行速度受角色属性的影响，当技能指定了speedAttribute
     * 后，角色的这个属性值将影响技能的执行速度。如果技能没有指定这个属性或
     * 者角色没有这个属性，则这个方法应该返回1.0,即原始速度。
     * @return 返回的最小值为0.0001f，为避免除0错误，速度不能小于或等于0
     */
    public float getSpeed() {
        float speed = 1.0f;
        if (bindSpeedAttribute != null) {
            speed = getNumberAttributeValue(actor, bindSpeedAttribute, 1.0f);
            if (speed <= 0) {
                speed = 0.0001f;
            }
        }
        return speed;
    }
    
    @Override
    public float getTrueUseTime() {
        // 最终的实际运行时间是cutTime后的时间。
        float tempTime = data.getUseTime() / getSpeed();
        
        // 注：因为暂不开放cutTimeStart，所以cutTimeStart目前为0
//        return tempTime - tempTime * (cutTimeStart + getCutTimeEndRate(actor, skillData));
        return tempTime - tempTime * (0 + getCutTimeEndRate());
    }
    
    /**
     * 获取指定Entity的Number属性的值，如果不存在指定的属性值，则返回defValue
     * @param entity
     * @param attributeName
     * @param defValue
     * @return 
     */
    protected final float getNumberAttributeValue(Entity entity, String attributeName, float defValue) {
         NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
         return nattr != null ? nattr.floatValue() : defValue;
    }
    
    /**
     * 给指定Entity的Number属性添加值，如果不存在指定的属性值，则什么也不做。
     * @param entity
     * @param attributeName
     * @param value 
     */
    protected final void addNumberAttributeValue(Entity entity, String attributeName, float value) {
        NumberAttribute nattr = entity.getAttributeManager().getAttribute(attributeName, NumberAttribute.class);
        if (nattr != null) {
            nattr.add(value);
        }
    }
    
    /**
     * 该方法会在技能结束时被自动调用,子类可以直接调用这个方法来提前结束技能。
     */
    protected void doSkillEnd() {
        initialized = false;
    }
    
    /**
     * 实现技能逻辑
     * @param tpf 
     */
    protected abstract void doSkillUpdate(float tpf);
    
}
