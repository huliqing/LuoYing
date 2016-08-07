///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.effect;
//
//import com.jme3.bounding.BoundingVolume;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.core.data.EffectData;
//import name.huliqing.core.enums.TracePositionType;
//import name.huliqing.core.enums.TraceType;
//import name.huliqing.core.loader.Loader;
//import name.huliqing.core.object.anim.Anim;
//import name.huliqing.core.object.sound.Sound;
//import name.huliqing.core.object.sound.SoundManager;
//import name.huliqing.core.utils.GeometryUtils;
//
///**
// *
// * @author huliqing
// */
//public abstract class AbstractEffect extends Effect {
//    
//    /**
//     * 动画控制,所有动画控制器都作用在animRoot上。
//     */
//    protected List<AnimationWrap> animations;
//    
//    /**
//     * 特效声音
//     */
//    protected List<SoundWrap> sounds;
//    
//    // ---- Inner
//    /**
//     * 本地根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个节点用于接受所有动画变换的节点。
//     * animations所执行的动画变换都作用在这个节点上。
//     */
//    protected final Node animRoot = new Node("EffectLocalRoot");
//    
//    // 跟踪的目标对象,必须必须有一个目标对象才有可能跟随
//    protected Spatial traceObject;
//    
//    // 实际效果所用的时间,这个实际时间受速度的影响,效果速度越快则效果的实际执行时间越少。
//    protected float trueTimeTotal;
//    protected float trueTimeUsed;
//    
//    protected List<EffectListener> listeners;
//    
//    // 标记效果是否已经结束
//    private boolean end;
//
//    @Override
//    public void setData(EffectData data) {
//        super.setData(data);
//        // 格式： "animId1 | startTime, animId2 | startTime, ..."
//        String[] aArr = data.getAsArray("animations");
//        if (aArr != null) {
//            animations = new ArrayList<AnimationWrap>(aArr.length);
//            for (String a : aArr) {
//                String[] bArr = a.split("\\|");
//                AnimationWrap anim = new AnimationWrap();
//                anim.animId = bArr[0];
//                anim.startTime = bArr.length > 1 ? Float.parseFloat(bArr[1]) : 0;
//                animations.add(anim);
//            }
//        }
//        
//        // 声音: "sound1 | startTime, sound2 | startTime, ..."
//        String[] cArr = data.getAsArray("sounds");
//        if (cArr != null) {
//            sounds = new ArrayList<SoundWrap>(cArr.length);
//            for (String c : cArr) {
//                String[] dArr = c.split("\\|");
//                SoundWrap sw = new SoundWrap();
//                sw.soundId = dArr[0];
//                sw.startTime = dArr.length > 1 ? Float.parseFloat(dArr[1]) : 0;
//                sounds.add(sw);
//            }
//        }
//    }
//        
//    @Override
//    public final void updateLogicalState(float tpf) {
//        // initialize的初始化可能由外部或者EffectManager进行了调用，所以这里要判断并避免重覆调用初始化。
//        if (!initialized) {
//            initialize();
//        }
//        effectUpdate(tpf);
//    }
//    
//    /**
//     * 初始化特效，该方法的调用分以下三种情况：<br>
//     * 1.由EffectManager自动调用该初始化方法。<br>
//     * 2.由特效的父节点在更新时自动调用该初始化方法。<br>
//     * 3.由外部调用，只有在特殊情况时才应该使用这种方式。<br>
//     */
//    @Override
//    public void initialize() {
//        super.initialize(); 
//        end = false;
//        trueTimeTotal = data.getUseTime() / data.getSpeed();
//        trueTimeUsed = 0;
//        
//        // 计算Animation的实际开始时间和实际使用时间
//        if (animations != null) {
//            for (int i = 0; i < animations.size(); i++) {
//                AnimationWrap aw = animations.get(i);
//                aw.trueAnimSpeed = data.getSpeed();
//                aw.trueStartTime = aw.startTime / data.getSpeed();
//            }
//        }
//        
//        if (animRoot.getParent() == null) {
//            attachChild(animRoot);
//        }
//        
//        // 1.初始化变换
//        if (data.getLocation() != null) {
//            setLocalTranslation(data.getLocation());
//        }
//        if (data.getRotation() != null) {
//            setLocalRotation(data.getRotation());
//        }
//        if (data.getScale() != null) {
//            setLocalScale(data.getScale());
//        }
//        
//        // 2.初始化跟随
//        doUpdateTracePosition();
//        doUpdateTraceRotation();
//    }
//    
//    private void doUpdateTracePosition() {
//        // add type offset
//        Vector3f pos = getLocalTranslation();
//        TracePositionType tpt = data.getTracePositionType();
//        
//        
//        if (tpt == TracePositionType.origin) {
//            pos.set(traceObject.getWorldTranslation());
//            
//            
//        } else if (tpt == TracePositionType.origin_bound_center) {
//            pos.set(traceObject.getWorldTranslation());
//            BoundingVolume bv = traceObject.getWorldBound();
//            if (bv != null) {
//                pos.setY(bv.getCenter().getY());
//            }
//            
//        } else if (tpt == TracePositionType.origin_bound_top) {
//            GeometryUtils.getBoundTopPosition(traceObject, pos);
//            pos.setX(traceObject.getWorldTranslation().x);
//            pos.setZ(traceObject.getWorldTranslation().z);
//            
//        } else if (tpt == TracePositionType.bound_center) {
//            pos.set(traceObject.getWorldBound().getCenter());
//            
//        } else if (tpt == TracePositionType.bound_top) {
//            GeometryUtils.getBoundTopPosition(traceObject, pos);
//        }
//        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
//        // 所以必须mult上目标对象的旋转
//        if (data.getTracePositionOffset() != null) {
//            TempVars tv = TempVars.get();
//            traceObject.getWorldRotation().mult(data.getTracePositionOffset(), tv.vect2);
//            pos.addLocal(tv.vect2);
//            tv.release();
//        }
//        setLocalTranslation(pos);
////        LOG.log(Level.INFO, "AbstractEffect doUpdateTracePosition, pos={0}", new Object[] {pos});
//    }
//    
//    private void doUpdateTraceRotation() {
//        Quaternion rot = getLocalRotation();
//        rot.set(traceObject.getWorldRotation());
//        if (data.getTraceRotationOffset() != null) {
//            rot.multLocal(data.getTraceRotationOffset());
//        }
//        setLocalRotation(rot);
//    }
//    
//    /**
//     * 更新效果逻辑
//     * @param tpf 
//     */
//    protected void effectUpdate(float tpf) {
//        
//        trueTimeUsed += tpf;
//        
//        // update Sound
//        if (sounds != null) {
//            for (int i = 0; i < sounds.size(); i++) {
//                sounds.get(i).update(tpf, trueTimeUsed);
//            }
//        }
//        
//        // update Animations
//        if (animations != null) {
//            for (int i = 0; i < animations.size(); i++) {
//                animations.get(i).update(tpf, trueTimeUsed);
//            }
//        }
//        
//        // 特效结束时清理并移除。
//        if (trueTimeUsed > trueTimeTotal) {
//            doEndEffect();
//        }
//    }
//    
//    @Override
//    public void cleanup() {
//         if (sounds != null) {
//            for (int i = 0; i < sounds.size(); i++) {
//                sounds.get(i).cleanup();
//            }
//        }
//        
//        if (animations != null) {
//            for (int i = 0; i < animations.size(); i++) {
//                animations.get(i).cleanup();
//            }
//        }
//        
//        // 重置效果时间
//        trueTimeUsed = 0;
//        traceObject = null;
//        super.cleanup(); 
//    }
//    
//    /**
//     * 当效果结束的时候这个方法会被调用，该方法会调用cleanup清理资源，并执行帧听器
//     */
//    protected void doEndEffect() {
//        // 帧听器
//        if (listeners != null) {
//            for (int i = 0; i < listeners.size(); i++) {
//                listeners.get(i).onEffectEnd(this);
//            }
//        }
//        // 清理资源
//        cleanup();
//        // 自动从父节点中脱离。
//        removeFromParent();
//        // 标记为end
//        end = true;
//    }
//    
//    @Override
//    public boolean isEnd() {
//        return end;
//    }
//        
//    @Override
//    public void requestEnd() {
//        // 由特定子类实现
//    }
//    
//    @Override
//    public void addListener(EffectListener listener) {
//        if (listeners == null) {
//            listeners = new ArrayList<EffectListener>(1);
//        }
//        if (!listeners.contains(listener)) {
//            listeners.add(listener);
//        }
//    }
//
//    @Override
//    public boolean removeListener(EffectListener listener) {
//        return listeners != null && listeners.remove(listener);
//    }
//
//    /**
//     * 设置特效要跟随的目标对象，当设置了这个目标之后，根据设置，特效在运行时会跟随这个目标对象的“位置","朝向”等。
//     * 当特效结束并被清理时这个目标对象会同时被清理，如果需要执行这个特效则需要重新设置这个对象。
//     * @param traceObject 
//     */
//    @Override
//    public void setTraceObject(Spatial traceObject) {
//        this.traceObject = traceObject;
//    }
//    
//    public class AnimationWrap {
//        
//        // 绑定的动画ID,这个id是在xml中定义的原始动画id.
//        String animId;
//        // 动画的开始时间,这个时间是xml中定义的原始时间，标记着特效开始后多少秒才启动这个动画.
//        float startTime;
//        // ---- 以下为需要根据情况动态计算的参数,,这些参数的值受效果运行时影响,如效果的速度
//        // 绑定的动画控制器实例
//        Anim anim;
//        // 动画的实际开始时间，动画的实际开始时间受特效速度(speed)参数的影响,默认特效速度为1.0, 在speed=1.0时，startTime
//        // 和trueStartTime的值应该是相等的。
//        float trueStartTime;
//        // 实际的动画运行速度
//        float trueAnimSpeed = 1.0f;
//        // 判断动画是否已经启动
//        boolean started;
//        
//        void update(float tpf, float effectTimeUsed) {
//            if (started) {
//                anim.update(tpf);
//                return;
//            }
//            if (effectTimeUsed >= trueStartTime) {
//                if (anim == null) {
//                    anim = Loader.loadAnimation(animId);
//                }
//                anim.setSpeed(trueAnimSpeed);
//                anim.setTarget(animRoot);
//                anim.start();
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            if (anim != null) {
//                anim.cleanup();
//            }
//            started = false;
//        }
//    }
//    
//    public class SoundWrap {
//        // xml上配置的声音id
//        String soundId;
//        // xml上配置的声音的开始时间
//        float startTime;
//        // 缓存的声音控制器
//        Sound sound;
//        // 声音的实际开始时间，受效果速度的影响。
//        float trueStartTime;
//        // 表示声音播放是否已经开始
//        boolean started;
//        
//        void update(float tpf, float effectTimeUsed) {
//            if (started) {
//                return;
//            }
//            if (effectTimeUsed >= trueStartTime) {
//                if (sound == null) {
//                    sound = Loader.load(soundId);
//                }
//                sound.setPosition(getWorldTranslation());
//                SoundManager.getInstance().addSound(sound);
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            if (sound != null) {
//                SoundManager.getInstance().removeSound(sound);
//            }
//            started = false;
//        }
//    }
//}
