///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.magic;
//
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.fighter.Common;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.MagicData;
//import name.huliqing.fighter.enums.TraceType;
//import name.huliqing.fighter.game.service.ActorAnimService;
//import name.huliqing.fighter.game.service.EffectService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.StateService;
//import name.huliqing.fighter.manager.SoundManager;
//import name.huliqing.fighter.object.actor.Actor;
//import name.huliqing.fighter.object.actoranim.ActorAnim;
//import name.huliqing.fighter.object.effect.Effect;
//import name.huliqing.fighter.utils.ConvertUtils;
//import name.huliqing.fighter.utils.MathUtils;
//
///**
// *
// * @author huliqing
// */
//public class AbstractMagic<T extends MagicData> extends Node implements Magic<T>{
//    private final StateService stateService = Factory.get(StateService.class);
//    private final ActorAnimService actorAnimService = Factory.get(ActorAnimService.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final EffectService effectService = Factory.get(EffectService.class);
//    
//    protected T data;
//    
//    // ---- 由于一些Object要涉及到缓存，以下一些参数存放到MagicData中,这些参数包
//    // 括：sounds,effects,states, actorAnims
//    // 格式: soundId|timePoint,soundId|timePoint...
//    protected List<SoundWrap> sounds;
//    
//    // 格式:effectId|timePoint,effectId|timePoint,effectId|timePoint...
//    protected List<EffectWrap> effects;
//    
//    // 格式:stateId|timePoint,stateId|timePoint,stateId|timePoint...
//    protected List<StateWrap> states;
//    
//    // 角色动画,格式:actorAnimId|timePoint,actorAnimId|timePoint,actorAnimId|timePoint...
//    protected List<ActorAnimWrap> actorAnims;
//    
//    // ----------------inner
//    
//    // 魔法的施放者，有可能为null.
//    protected Actor source;
//    // 魔法针对的主目标,如果魔法没有特定的目标，则有可能为null
//    protected Actor target;
//    protected Spatial targetObject;
//    
//    // 标记是否已经开始运行
//    protected boolean started;
//    
//    @Override
//    public T getData() {
//        return data;
//    }
//
//    @Override
//    public void setData(T data) {
//        this.data = data;
//        // Sounds
//        String[] tempSounds = data.getAsArray("sounds");
//        if (tempSounds != null) {
//            sounds = new ArrayList<SoundWrap>(tempSounds.length);
//            for (int i = 0; i < tempSounds.length; i++) {
//                String[] soundArr = tempSounds[i].split("\\|");
//                SoundWrap sw = new SoundWrap();
//                sw.soundId = soundArr[0];
//                if (soundArr.length >= 2) {
//                    sw.timePoint = MathUtils.clamp(ConvertUtils.toFloat(soundArr[1], 0f), 0, 1);
//                }
//                sounds.add(sw);
//            }
//        }
//        
//        // Effects, 格式:effectId|timePoint,effect|timePoint,effectId|timePoint...
//        String[] tempEffects = data.getAsArray("effects");
//        if (tempEffects != null) {
//            effects = new ArrayList<EffectWrap>(tempEffects.length);
//            for (int i = 0; i < tempEffects.length; i++) {
//                String[] effectArr = tempEffects[i].split("\\|");
//                EffectWrap ew = new EffectWrap();
//                ew.effectId = effectArr[0];
//                if (effectArr.length >= 2) {
//                    ew.timePoint = MathUtils.clamp(ConvertUtils.toFloat(effectArr[1], 0f), 0, 1);
//                }
//                effects.add(ew);
//            }
//        }
//        
//        // 状态格式:stateId|timePoint,stateId|timePoint,stateId|timePoint...
//        String[] tempStates = data.getAsArray("states");
//        if (tempStates != null) {
//            states = new ArrayList<StateWrap>(tempStates.length);
//            for (int i = 0; i < tempStates.length; i++) {
//                String[] stateArr = tempStates[i].split("\\|");
//                StateWrap sw = new StateWrap();
//                sw.stateId = stateArr[0];
//                if (stateArr.length >= 2) {
//                    sw.timePoint = MathUtils.clamp(ConvertUtils.toFloat(stateArr[1], 0f), 0, 1);
//                }
//                states.add(sw);
//            }
//        }
//        
//        // 角色动画,格式:actorAnimId|timePoint,actorAnimId|timePoint,actorAnimId|timePoint...
//        String[] tempActorAnims = data.getAsArray("actorAnims");
//        if (tempActorAnims != null) {
//            actorAnims = new ArrayList<ActorAnimWrap>(tempActorAnims.length);
//            for (int i = 0; i < tempActorAnims.length; i++) {
//                String[] actorAnimArr = tempActorAnims[i].split("\\|");
//                ActorAnimWrap aaw = new ActorAnimWrap();
//                aaw.actorAnimId = actorAnimArr[0];
//                if (actorAnimArr.length >= 2) {
//                    aaw.timePoint = MathUtils.clamp(ConvertUtils.toFloat(actorAnimArr[1], 0f), 0, 1);
//                }
//                actorAnims.add(aaw);
//            }
//        }
//    }
//
//    @Override
//    public void updateLogicalState(float tpf) {
//        update(tpf);
//        super.updateLogicalState(tpf);
//    }
//
//    @Override
//    public Spatial getDisplay() {
//        return this;
//    }
//    
//    @Override
//    public void start() {
//        if (started) {
//            return;
//        }
//        started = true;
//        initialize();
//    }
//    
//    protected void initialize() {
//        source = playService.findActor(data.getSourceActor());
//        target = playService.findActor(data.getTargetActor());
//        if (target != null) {
//            targetObject = target.getModel();
//        }
//        
//        // 首次时进行一次位置更新
//        if (targetObject != null && Common.getPlayState().isInScene(targetObject)) {
//            if (data.getTracePosition() == TraceType.once 
//                    || data.getTracePosition() == TraceType.always) {
//                doUpdateTracePosition();
//            }
//            if (data.getTraceRotation() == TraceType.once 
//                    || data.getTraceRotation() == TraceType.always) {
//                doUpdateTraceRotation();
//            }
//        }
//    }
//    
//    @Override
//    public void update(float tpf) {
//        if (!started) {
//            return;
//        }
//        
//        // time
//        data.setTimeUsed(data.getTimeUsed() + tpf);
//        
//        float inter = getInterpolation();
//        
//        // update position & rotation
//        if (targetObject != null && Common.getPlayState().isInScene(targetObject)) {
//            if (data.getTracePosition() == TraceType.always) {
//                doUpdateTracePosition();
//            }
//            if (data.getTraceRotation() == TraceType.always) {
//                doUpdateTraceRotation();
//            }
//        }
//        
//        // update sounds
//        doUpdateSounds(inter);
//        
//        // update effects
//        doUpdateEffects(inter);
//        
//        // update states
//        doUpdateStates(inter);
//        
//        // update actorAnims
//        doUpdateActorAnims(inter);
//
//        // 判断是否结束魔法
//        if (data.getTimeUsed() >= data.getUseTime()) {
//            cleanup();
//        }
//
//    }
//    
//    private void doUpdateTracePosition() {
//        Vector3f pos = getLocalTranslation();
//        pos.set(targetObject.getWorldTranslation());
//        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
//        // 所以必须mult上目标对象的旋转
//        if (data.getTracePositionOffset() != null) {
//            TempVars tv = TempVars.get();
//            targetObject.getWorldRotation().mult(data.getTracePositionOffset(), tv.vect2);
//            pos.addLocal(tv.vect2);
//            tv.release();
//        }
//        setLocalTranslation(pos);
//    }
//    
//    private void doUpdateTraceRotation() {
//        Quaternion rot = getLocalRotation();
//        rot.set(targetObject.getWorldRotation());
//        if (data.getTraceRotationOffset() != null) {
//            rot.multLocal(data.getTraceRotationOffset());
//        }
//        setLocalRotation(rot);
//    } 
//
//    /**
//     * 播放声音事件
//     * @param tpf 
//     */
//    protected void doUpdateSounds(float inter) {
//        if (sounds != null) {
//            for (SoundWrap sw : sounds) {
//                sw.update(inter);
//            }
//        }
//    }
//    
//    protected void doUpdateEffects(float inter) {
//        if (effects != null) {
//            for (EffectWrap ew : effects) {
//                ew.update(inter);
//            }
//        }
//    }
//    
//    protected void doUpdateStates(float inter) {
//        if (states != null) {
//            for (StateWrap sw : states) {
//                sw.update(inter);
//            }
//        }
//    }
//    
//    protected void doUpdateActorAnims(float inter) {
//        if (actorAnims != null) {
//            for (ActorAnimWrap aaw : actorAnims) {
//                aaw.update(inter);
//            }
//        }
//    }
//
//    @Override
//    public void cleanup() {
//        started = false;
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
//        if (states != null) {
//            for (StateWrap sw : states) {
//                sw.cleanup();
//            }
//        }
//        if (actorAnims != null) {
//            for (ActorAnimWrap aaw : actorAnims) {
//                aaw.cleanup();
//            }
//        }
//        // 自动脱离
//        removeFromParent();
//    }
//    
//    /**
//     * 获取当前时间的插值
//     * @return 
//     */
//    protected float getInterpolation() {
//        float useTime = data.getUseTime();
//        if (useTime <= 0) {
//            return 0;
//        }
//        float inter = data.getTimeUsed() / useTime;
//        if (inter < 1) {
//            return inter;
//        }
//        return 1;
//    }
//    
//    protected void playSound(String soundId) {
//        SoundManager.getInstance().playSound(soundId, getWorldTranslation());
//    }
//    
//    protected void playEffect(String effectId) {
//        Effect effect = effectService.loadEffect(effectId);
//        effect.setTraceObject(this);
//        playService.addEffect(effect);
//    }
//    
//    protected void playState(String stateId) {
//        if (target == null)
//            return;
//        stateService.addState(target, stateId);
//    }
//    
//    protected void playActorAnim(String actorAnimId) {
//        if (target == null) 
//            return;
//        ActorAnim anim = actorAnimService.loadAnim(actorAnimId);
//        anim.setActor(target);
//        target.getModel().addControl(anim);
//        anim.start();
//    }
//    
//    // -------------------------------------------------------------------------
//    
//    // 状态更新控制
//    protected class StateWrap {
//        String stateId;
//        // 效果的开始播放时间点
//        float timePoint;
//        // 标记效果是否已经开始
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= timePoint) {
//                playState(stateId);
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
//    protected class EffectWrap {
//        // 效果ID
//        String effectId;
//        // 效果的开始播放时间点
//        float timePoint;
//        // 标记效果是否已经开始
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= timePoint) {
//                playEffect(effectId);
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
//    // 声音控制
//    public class SoundWrap {
//        String soundId;
//        float timePoint;     // 未受cutTime影响的时间点,在xml中配置的
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= timePoint) {
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
//    public class ActorAnimWrap {
//        // 原始的时间开始点和结束点
//        String actorAnimId;
//        float timePoint;
//        boolean started;
//        
//        void update(float interpolation) {
//            if (started) return;
//            if (interpolation >= timePoint) {
//                playActorAnim(actorAnimId);
//                started = true;
//            }
//        }
//      
//        void cleanup() {
//            started = false;
//        }
//    }
//}
