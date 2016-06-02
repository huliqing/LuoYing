///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.skill;
//
//import com.jme3.animation.LoopMode;
//import com.jme3.math.FastMath;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.constants.SkillConstants;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.enums.SkillType;
//import name.huliqing.fighter.game.service.EffectService;
//import name.huliqing.fighter.game.service.ElService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.loader.Loader;
//import name.huliqing.fighter.manager.SoundManager;
//import name.huliqing.fighter.object.actoranim.ActorAnim;
//import name.huliqing.fighter.object.channel.ChannelProcessor;
//import name.huliqing.fighter.object.effect.Effect;
//import name.huliqing.fighter.utils.ConvertUtils;
//
///**
// *
// * 注意：影响技能执行时间和速度的三个重要因素：
// * 1.useTime
// * 2.speed;
// * 3.cutTimeStart,cutTimeEnd
// * 默认情况下：useTime开放给外部进行固定配置，而speed和cutTimeStart,cutTimeEnd
// * 作为动态参数进行设置，即在执行技能过程中可动态设置speed和cutTime.
// * 这要求在设置speed和cutTime的时候不应该影响其它时间插值点的协调运行，如：
// * 技能检测点、特效时间点等。
// * 所以，配置技能的时候应该在标准设置(speed=1,cutTimeStart=0,cutTimeEnd=0)下进行.
// * 否则最终可能会产生不协调的现象.
// * 
// * 关于useTime：会影响技能时间、技能速度、动画时间、动画速度
// * 关于speed: 会影响技能时间、技能速度、动画时间、动画速度
// * 关于cutTime: 影响技能时间、动画时间,但不影响技能速度和动画速度
// * 
// * @author huliqing
// */
//public abstract class AbstractSkill implements Skill {
////    private static final Logger logger = Logger.getLogger(AbstractSkill.class.getName());
////    private final AttributeService attributeService = Factory.get(AttributeService.class);
//    private final ElService elService = Factory.get(ElService.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final EffectService effectService = Factory.get(EffectService.class);
//    
//    // 格式: soundId|timePoint,soundId|timePoint...
//    protected SoundWrap[] sounds;
//    
//    // 格式:effectId|timePoint,effect|timePoint,effectId|timePoint...
//    protected EffectWrap[] effects;
//    
//    // 格式：motionId|timeStart|timeEnd,motionId|timeStart|timeEnd
//    protected ActorAnimWrap[] actorAnims;
//    
//    // ---- 内部参数 ----
//    protected SkillData data;
//    
//    // 当前执行技能的角色
//    protected Actor actor;
//    
//    protected ChannelProcessor channelProcessor;
//    
//    // 当前技能已经运行的时间。每一次执行该技能或循环时都重置为0
//    protected float time;
//    
//    // 技能是否已经开始运行。
//    private boolean started;
//    
//    public AbstractSkill() {}
//    
//    public AbstractSkill(SkillData data) {
//        this.data = data;
//        
//        // Sounds
//        String[] tempSounds = data.getAsArray("sounds");
//        if (tempSounds != null) {
//            sounds = new SoundWrap[tempSounds.length];
//            for (int i = 0; i < sounds.length; i++) {
//                String[] soundArr = tempSounds[i].split("\\|");
//                SoundWrap sw = new SoundWrap();
//                sw.soundId = soundArr[0];
//                if (soundArr.length >= 2) {
//                    sw.timePoint = ConvertUtils.toFloat(soundArr[1], 0f);
//                }
//                sounds[i] = sw;
//            }
//        }
//        
//        // Effects, 格式:effectId|timePoint,effect|timePoint,effectId|timePoint...
//        String[] tempEffects = data.getProto().getAsArray("effects");
//        if (tempEffects != null) {
//            effects = new EffectWrap[tempEffects.length];
//            for (int i = 0; i < effects.length; i++) {
//                String[] effectArr = tempEffects[i].split("\\|");
//                EffectWrap ew = new EffectWrap();
//                ew.effectId = effectArr[0];
//                if (effectArr.length >= 2) {
//                    ew.timePoint = ConvertUtils.toFloat(effectArr[1], 0f);
//                }
//                effects[i] = ew;
//            }
//        }
//        
//        // Motions, 格式: actorAnimId|timeStart|timeEnd,actorAnimId|timeStart|timeEnd
//        String[] tempActorAnims = data.getProto().getAsArray("actorAnims");
//        if (tempActorAnims != null) {
//            actorAnims = new ActorAnimWrap[tempActorAnims.length];
//            for (int i = 0; i < tempActorAnims.length; i++) {
//                String[] actorAnimArr = tempActorAnims[i].split("\\|");
//                ActorAnimWrap aaw = new ActorAnimWrap();
//                aaw.actorAnim = Loader.loadActorAnim(actorAnimArr[0]);
//                if (actorAnimArr.length >= 2) {
//                    aaw.timePointStart = ConvertUtils.toFloat(actorAnimArr[1], 0);
//                }
//                if (actorAnimArr.length >= 3) {
//                    aaw.timePointEnd = ConvertUtils.toFloat(actorAnimArr[2], 1);
//                }
//                actorAnims[i] = aaw;
//            }
//        }
//    }
//    
//    @Override
//    public void start() {
//        if (started) {
//            return;
//        }
//        
//        init();
//        loopStart();
//        started = true;
//    }
//    
//    /**
//     * 初始化技能,该方法只在技能start后执行一次,循环过程不会再执行。
//     */
//    protected  void init() {
//        
//        // 计算实际的声效执行时间点，受cutTime影响
//        if (sounds != null) {
//            for (SoundWrap sw : sounds) {
//                sw.trueTimePoint = fixTimePointByCutTime(sw.timePoint);
//            }
//        }
//        
//        // 计算实际的效果执行时间点
//        if (effects != null) {
//            for (EffectWrap ew : effects) {
//                ew.trueTimePoint = fixTimePointByCutTime(ew.timePoint);
//            }
//        }
//        
//        // 计算实际的运动时间点
//        if (actorAnims != null) {
//            for (ActorAnimWrap aaw : actorAnims) {
//                aaw.trueTimePointStart = fixTimePointByCutTime(aaw.timePointStart);
//                aaw.trueTimePointEnd = fixTimePointByCutTime(aaw.timePointEnd);
//            }
//        }
//        
//        // 1.start animation
//        // 注B：cutTime只影响动画的开始时间点，动画的结束时间点不需要设置。
//        // 结束时间点只受“是否有后续技能”的影响，
//        // 1.如果有后续技能，则当前技能时间结束后
//        // 会立即执行后续技能，所以当前动画会被立即停止（切换到新技能动画）。所以无需手动设置
//        // 2.如果没有后续技能，则让当前技能动画自行结束，这也是比较合理的。如武功中的“收式”，如果
//        // 没有后续连招，则应该让当前技能动画的“收式”正常播放。
//        String animation = data.getAnimation();
//        if (animation != null && channelProcessor != null) {
//            
//            doUpdateAnimation(animation
//                    , data.getLoopMode()
//                    , getAnimFullTime()
//                    , getAnimStartTime());
//        }
//    }
//    
//    protected void loopStart() {}
//
//    @Override
//    public final void update(float tpf) {
//        if (!started) {
//            return;
//        }
//        // 检查是否结束
//        time += tpf;
//        float interpolation = getInterpolation();
//        
//        // 2.update sounds
//        doUpdateSound(interpolation);
//        
//        // 3.update effects
//        doUpdateEffect(interpolation);
//        
//        // 4.update force;
//        doUpdateAnims(interpolation);
//        
//        // 5.update logic
//        // 为保证所有checkPoint都有执行到，
//        // doUpdateLogic(tpf)必须放在 time += tpf 和 cleanup中间
//        doUpdateLogic(tpf);
//        
//        if (time >= data.getTrueUseTime()) {
//            if (data.getLoopMode() == LoopMode.Loop || data.getLoopMode() == LoopMode.Cycle) {
//                loopStart();
//            } else {
//                end();
//                cleanup();
//            }
//        }
//    }
//    
//    /**
//     * 执行动画
//     * @param animation 动画名称
//     * @param loopMode　循环类型
//     * @param animFullTime 动画的完整执行时间
//     * @param animStartTime 指定动画的起始执行时间 
//     */
//    protected void doUpdateAnimation(String animation, LoopMode loopMode
//            , float animFullTime, float animStartTime) {
//        
//        channelProcessor.playAnim(animation, loopMode, animFullTime, animStartTime, data.getChannels());
//        if (data.isChannelLocked()) {
//            channelProcessor.setChannelLock(true, data.getChannels());
//        }
//    }
//    
//    /**
//     * 播放声音事件
//     * @param tpf 
//     */
//    protected void doUpdateSound(float interpolation) {
//        if (sounds != null) {
//            for (SoundWrap sw : sounds) {
//                if (sw.started) continue;
//                sw.update(interpolation);
//            }
//        }
//    }
//    
//    /**
//     * 更新效果逻辑
//     * @param tpf 
//     */
//    protected void doUpdateEffect(float interpolation) {
//        if (effects != null) {
//            for (EffectWrap ew : effects) {
//                if (ew.started) continue;
//                ew.update(interpolation);
//            }
//        }
//    }
//    
//    /**
//     * 更新所有角色动画
//     * @param interpolation 
//     */
//    protected void doUpdateAnims(float interpolation) {
//        if (actorAnims != null) {
//            for (ActorAnimWrap aaw : actorAnims) {
//                aaw.update(actor, this, interpolation);
//            }
//        }
//    }
//    
//    /**
//     * 覆盖该方法来handler特效的执行
//     * @param effect 
//     */
//    protected void playEffect(String effectId) {
//        // remove20160126
////        effect.setTraceObject(actor.getModel());
//        
//        Effect effect = effectService.loadEffect(effectId);
//        effect.setTraceObject(actor.getModel());
//        playService.addEffect(effect);
//    }
//    
//    /**
//     * 播放声音
//     * @param soundId 
//     */
//    protected void playSound(String soundId) {
//        SoundManager.getInstance().playSound(soundId, actor.getModel().getWorldTranslation());
//    }
//    
//    /**
//     * 标记技能结束。注意：end与cleanup的不同，cleanup不管在什么时候都
//     * 会执行，即使技能在被打断的时候也会执行。而end()则不一定。一般cleanup只作为
//     * 释放资源使用。只有在确定一定需要确保被释放的资源时才应该在cleanup中操作。
//     */
//    protected void end() {
//        started = false;
//    }
//    
//    @Override
//    public void cleanup() {
//        // 清理声效播放标记,让声效可以重新播放
//        if (sounds != null) {
//            for (SoundWrap sw : sounds) {
//                sw.cleanup();
//            }
//        }
//        // 清理效果播放标记,让效果可以重新播放
//        if (effects != null) {
//            for (EffectWrap ew : effects) {
//                ew.cleanup();
//            }
//        }
//        if (actorAnims != null) {
//            for (ActorAnimWrap aaw : actorAnims) {
//                aaw.cleanup();
//            }
//        }
//        
//        // 如果有锁定过动画通道则必须解锁，否则角色的动画通道将不能被其它技能使用。
//        if (data.isChannelLocked()) {
//            channelProcessor.setChannelLock(false, data.getChannels());
//        }
//        
//        // 重置
//        time = 0;
//        started = false;
//    }
//    
//    @Override
//    public boolean isEnd() {
//        return !started;
//    }
//
//    @Override
//    public SkillType getSkillType() {
//        return data.getSkillType();
//    }
//    
//    /**
//     * 获取"动画"的完整执行时间,注：动画的完整时间并不等于动画的实际执行时间,
//     * 实际动画的执行时间受cutTime的影响。该值返回: useTime / speed.
//     * 设置动画播放时应该使用这个时间为准。
//     * @return 
//     */
//    protected float getAnimFullTime() {
//        return data.getUseTime() / data.getSpeed();
//    }
//    
//    /**
//     * 获取动画的开始时间点，单位秒
//     * @return 
//     */
//    protected float getAnimStartTime() {
//        // 时间的起点在播放时间段开始的剪裁点上
//        return getAnimFullTime() * data.getCutTimeStart();
//    }
//    
//    /**
//     * 重新计算受cutTime影响的时间插值点。基本上所有在xml中配置的时间插值点在
//     * 使用前都应该经过这个方法进行一次处理。
//     * @param interpolation 原始的时间插值点(未受cutTime影响的时间插值)
//     * @return 经过cutTime计算后的实际的时间插值点
//     */
//    protected float fixTimePointByCutTime(float interpolation) {
//        float cutTimeStart = data.getCutTimeStart();
//        float cutTimeEnd = data.getCutTimeEnd();
//        float result = FastMath.clamp(
//                (interpolation - cutTimeStart) / (1.0f - cutTimeStart - cutTimeEnd)
//                , 0f, 1.0f);
//        return result;
//    }
//
//    @Override
//    public Actor getActor() {
//        return actor;
//    }
//    
//    @Override
//    public void setActor(Actor actor) {
//        this.actor = actor;
//    }
//    
//    @Override
//    public void setAnimChannelProcessor(ChannelProcessor animChannelProcessor) {
//        this.channelProcessor = animChannelProcessor;
//    }
//    
//    @Override
//    public SkillData getSkillData() {
//        return data;
//    }
//
//    @Override
//    public void restoreAnimation() {
//        String animation = data.getAnimation();
//        if (animation != null && channelProcessor != null) {
//            channelProcessor.restoreAnimation(animation
//                    , data.getLoopMode()
//                    , getAnimFullTime()
//                    , getAnimStartTime()
//                    , data.getChannels());
//        }
//    }
//    
//    /**
//     * 获取当前时间的插值
//     * @return 
//     */
//    protected float getInterpolation() {
//        float inter = time / data.getTrueUseTime();
//        if (inter < 1) {
//            return inter;
//        }
//        return 1;
//    }
//    
//    /**
//     * 获取当前等级下技能的等级值,由等级公式计算出来，如果不存在则返回-1.
//     * @return 
//     */
//    protected float getLevelValue() {
//        if (data.getLevelEl() == null) {
//            return -1;
//        }
//        return elService.getLevelEl(data.getLevelEl(), data.getLevel());
//    }
//    
//    /**
//     * 获取当前等级下技能的值，默认返回等级值。
//     * @return 
//     * @see #getLevelValue() 
//     */
//    protected float getSkillValue() {
//        // 由子类覆盖
//        return getLevelValue();
//    }
//
//    @Override
//    public int canPlay() {
//        return SkillConstants.STATE_OK;
//    }
//    
//    // -------------------------------------------------------------------------
//    // inner
//    
//    // 声音控制
//    public class SoundWrap {
//        String soundId;
//        float timePoint; // 未受cutTime影响的时间点,在xml中配置的
//        float trueTimePoint; // 实际的时间点（动态的），受cutTime影响
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= trueTimePoint) {
//                playSound(soundId);
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            started = false;
//        }
//    }
//    
//    // 效果更新控制
//    public class EffectWrap {
//        // 效果ID
//        String effectId;
//        // 效果的开始播放时间点,这个时间是技能时间(trueUseTime)的插值点
//        float timePoint;
//        // 实际的效果执行时间插值点，受cutTime影响
//        float trueTimePoint;
//        // 标记效果是否已经开始
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= trueTimePoint) {
//                playEffect(effectId);
//                // 标记效果已经开始
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            // 不要去手动调用效果的cleanup,效果只要开始后,就让它自动结束.
//            started = false;
//        }
//    }
//    
//    public class ActorAnimWrap {
//        // 原始的时间开始点和结束点
//        float timePointStart;
//        float timePointEnd = 1;
//        // 实际时间点
//        float trueTimePointStart; 
//        float trueTimePointEnd = 1;
//        // 运动处理器
//        ActorAnim actorAnim;
//        boolean started;
//        
//        void update(Actor actor, Skill skill, float interpolation) {
//            if (started) return;
//            if (interpolation >= trueTimePointStart) {
//                actorAnim.setActor(actor);
//                // 计算出实际的动画时间
//                actorAnim.setUseTime((trueTimePointEnd - trueTimePointStart) * data.getTrueUseTime());
//                actor.getModel().addControl(actorAnim);
//                actorAnim.start();
//                started = true;
//            }
//        }
//      
//        void cleanup() {
//            if (!actorAnim.isEnd()) {
//                actorAnim.cleanup();
//            }
//            started = false;
//        }
//    }
//    
//    /**
//     * 实现技能逻辑
//     * @param tpf 
//     */
//    protected abstract void doUpdateLogic(float tpf);
//    
//}
