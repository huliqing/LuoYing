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
//package name.huliqing.luoying.object.effect;
//
//import com.jme3.bounding.BoundingVolume;
//import com.jme3.math.Quaternion;
//import com.jme3.math.Vector3f;
//import com.jme3.scene.Node;
//import com.jme3.scene.Spatial;
//import com.jme3.util.TempVars;
//import java.util.ArrayList;
//import java.util.List;
//import name.huliqing.luoying.data.EffectData;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.anim.Anim;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.object.entity.EntityModule;
//import name.huliqing.luoying.object.scene.Scene;
//import name.huliqing.luoying.object.sound.Sound;
//import name.huliqing.luoying.object.sound.SoundManager;
//import name.huliqing.luoying.utils.GeometryUtils;
//
///**
// * 特效, 特效可以添加到EffectManager上，也可以直接添加到一个Node下面,所有效果都有一个执行时间.
// * @author huliqing
// * @version v1.4 20161011
// * @version v1.3 20160806
// * @param <T>
// * @since v1.2 20150421
// */
//public class Effect<T extends EffectData> extends Node implements Entity<T> {
//    
//    protected final EntityModule entityModule = new EntityModule(this);
//    
//    protected T data;
//    
//    /** 动画控制,所有动画控制器都作用在animRoot上。*/
//    protected List<AnimationWrap> animations;
//    
//    /** 特效声音 */
//    protected List<SoundWrap> sounds;
//    
//    /** 特效的初始偏移位置（跟随时用） */
//    protected Vector3f initLocation;
//    /** 特效的初始偏移旋转（跟随时用） */
//    protected Quaternion initRotation;
//    /** 特效的初始偏移缩放（跟随时用） */
//    protected Vector3f initScale;
//    
//    /** 要跟随的物体的ID,这个物体必须存在于场景中,值小于等于0表示不跟随 */
//    protected long traceEntityId;
//    /** 特效跟随位置的类型 */
//    private TraceType tracePosition;
//    /** 特效跟随旋转的类型  */
//    private TraceType traceRotation;
//    /** 特效跟随位置时的偏移类型 */
//    private TracePositionType tracePositionType;
// 
//    /** 特效的总的使用时间，注：特效的使用时间受特效执行速度的影响。
//     * 如果特效速度为2.0则实际执行时间只有useTime的一半，反之也然 */
//    protected float useTime = 1.0f;
//    
//    /** 判断特效是否可以立即结束，默认false, 大部分情况下特效都应该”自然结束“，
//     * 但是存在一些特殊特效，这些特效需要在被依赖的目标（如技能）结束的时候立即也一起结束。 */
//    protected boolean endImmediate;
//    
////    /** 是否让特效在结束的时候自动清理和移除 */
////    private boolean autoDetach = true;
//    
//    // ---------- inner
//    
//    /** 特效所在的场景，如果特效是直接放在某个节点下面，则scene可能为null. */
//    protected Scene scene;
//    
//    /**
//     * 动画(Anim)根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个节点用于接受所有动画变换的节点。
//     * animations所执行的动画变换都作用在这个节点上。
//     */
//    protected final Node animRoot = new Node("EffectAnimRoot");
//    
//    /** 判断特效是否已经初始化 */
//    protected boolean initialized;
//    
//    /** 跟踪的目标对象,必须必须有一个目标对象才有可能跟随 */
//    protected Spatial traceObject;
//    
//    /** 实际效果所用的时间,这个实际时间受速度的影响,效果速度越快则效果的实际执行时间越少。*/
//    protected float trueTimeTotal;
//    
//    /** 当前效果所使用的实际时间。*/
//    protected float timeUsed;
//    
//    /** 默认的技能速度 */
//    protected float speed = 1.0f;
//    
//    public Effect() {
//        attachChild(animRoot);
//    }
//    
//    @Override
//    public void setData(T data) {
//        this.data = data;
//        // 初始位置
//        Vector3f location = data.getAsVector3f("location");
//        Quaternion rotation = data.getAsQuaternion("rotation");
//        Vector3f scale = data.getAsVector3f("scale");
//        if (location != null) {
//            setLocalTranslation(location);
//        }
//        if (rotation != null) {
//            setLocalRotation(rotation);
//        }
//        if (scale != null) {
//            setLocalScale(scale);
//        }
//        
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
//        // 跟随偏移
//        initLocation = data.getAsVector3f("initLocation");
//        initRotation = data.getAsQuaternion("initRotation");
//        initScale = data.getAsVector3f("initScale");
//        traceEntityId = data.getAsLong("traceEntityId", traceEntityId);
//        tracePosition = TraceType.identity(data.getAsString("tracePosition", TraceType.no.name()));
//        traceRotation = TraceType.identity(data.getAsString("traceRotation", TraceType.no.name()));
//        tracePositionType = TracePositionType.identify(data.getAsString("tracePositionType", TracePositionType.origin.name()));
//        useTime = data.getAsFloat("useTime", useTime);
//        timeUsed = data.getAsFloat("timeUsed", timeUsed);
//        speed = data.getAsFloat("speed", speed);
//        endImmediate = data.getAsBoolean("endImmediate", endImmediate);
//    }
//    
//    @Override
//    public T getData() {
//        return data;
//    }
//    
//    @Override
//    public void updateDatas() {
//        entityModule.updateDatas();
//        // 保存位置状态
//        data.setAttribute("location", getLocalTranslation());
//        data.setAttribute("rotation", getLocalRotation());
//        data.setAttribute("scale", getLocalScale());
//        data.setAttribute("timeUsed", timeUsed);
//        data.setAttribute("speed", speed);
//        data.setAttribute("traceEntityId", traceEntityId);
//        // xxx to save Anim\Sounds data
//    }
//
//    @Override
//    public Spatial getSpatial() {
//        // 特效也是一个Node节点，直接返回这个节点就可以
//        return this;
//    }
//    
//    /**
//     * 初始化特效
//     */
//    @Override
//    public void initialize() {
//        if (initialized) {
//            throw new IllegalStateException("Effect already is initialized! EffectId=" + data.getId());
//        }
//        
//        trueTimeTotal = useTime / speed;
//        
//        // 1.查找被跟随的对象，并初始化位置
//        initTrace();
//
//        // 2.初始化动画
//        initAnimations();
//        
//        // 初始化模块控制器
//        entityModule.initialize();
//        
//        initialized = true;
//    }
//    
//    /**
//     * 查找被跟随的对象，并初始化位置
//     */
//    private void initTrace() {
//        if (traceObject == null) {
//            traceObject = findTraceObject(traceEntityId);
//        }
//        if (traceObject != null) {
//            if (tracePosition == TraceType.once || tracePosition == TraceType.always) {
//                doUpdateTracePosition();
//            }
//            if (traceRotation == TraceType.once || traceRotation == TraceType.always) {
//                doUpdateTraceRotation();
//            }
//        }
//    }
//    
//    private void initAnimations() {
//        // 计算Animation的实际开始时间和实际使用时间
//        if (animations != null) {
//            for (int i = 0; i < animations.size(); i++) {
//                AnimationWrap aw = animations.get(i);
//                aw.trueAnimSpeed = speed;
//                aw.trueStartTime = aw.startTime / speed;
//            }
//        }
//        // 初始化时需要更新一次动画
//        updateAnimations(0, timeUsed);
//    }
//    
//    /**
//     * 判断特效是否已经初始化
//     * @return 
//     */
//    @Override
//    public boolean isInitialized() {
//        return initialized;
//    }
//    
//    @Override
//    public final void updateLogicalState(float tpf) {
//        // 当Effect作为Entity添加到Scene时，Scene会自动调用Effect的initialize进行初始化，
//        // 当Effect作为普通节点直接放在某个Node下面时，可能用户会忘记调用 initialize方法，所以这里特别判断调用一下，
//        // 减少用户代码麻烦, (注：scene可以为null)
//        if (!initialized) {
//            initialize();
//        }
//        effectUpdate(tpf);
//    }
//    
//    /**
//     * 更新效果逻辑
//     * @param tpf 
//     */
//    protected void effectUpdate(float tpf) {
//        timeUsed += tpf;
//        
//        // 更新位置
//        updateTrace();
//        
//        // 更新声音
//        updateSounds(tpf, timeUsed);
//        
//        // 更新动画
//        updateAnimations(tpf, timeUsed);
//        
//        // 检查是否需要结束特效
//        if (timeUsed >= trueTimeTotal) {
//            doEndEffect();
//        }
//    }
//    
//    /**
//     * 强制结束效果，当效果运行时间结束时这个方法会被调用，来结束效果，这个方法也可以由子类进行调用，
//     * 当子类需要特殊结束效果时可以直接调用这个方法。
//     */
//    protected void doEndEffect() {
//        // 如果特效设置了自动脱离场景则从场景中移除
//        if (scene != null) {
//            scene.removeEntity(this);
//            scene = null;
//        } else {
//            cleanup();
//        }
//    }
//    
//    /**
//     * 清理效果数据.
//     */
//    @Override
//    public void cleanup() {
//         entityModule.cleanup();
//         
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
////        // 如果特效设置了自动脱离场景则从场景中移除
////        if (autoDetach) {
////            if (scene != null) {
////                scene.removeEntity(this);
////            }
////            // 有些特效是不一定直接放在场景中的。
////            removeFromParent();
////        }
//        
//        // 重置效果时间
//        scene = null;
//        timeUsed = 0;
//        initialized = false;
//        
//        // 节点自动移除场景
//        removeFromParent();
//    }
//    
//    @Override
//    public final boolean removeFromScene() {
//        doEndEffect();
//        return true;
//    }
//    
//    /**
//     * 请求结束特效，一般情况下不要直接结束特效（如：cleanup)，因为一些特效如果直接结束会非常不自然和难看，
//     * 所以在调用特效，并希望结束一个特效时应该使用这个方法来请求结束一个特效，
//     * 而具体是否结束或者如何结束一个特效由具体的子类去实现. 
//     */
//    public void requestEnd() {
//        if (endImmediate) {
//            doEndEffect();
//        }
//    }
//    
//    /**
//     * 判断特效是否已经结束，如果该方法返回true,则特效逻辑将不再执行。
//     * @return 
//     */
//    public boolean isEnd() {
//        return !initialized;
//    }
//    
//    /**
//     * 更新跟随位置
//     */
//    private void updateTrace() {
//        if (traceObject != null) {
//            if (tracePosition == TraceType.always) {
//                doUpdateTracePosition();
//            }
//            if (traceRotation == TraceType.always) {
//                doUpdateTraceRotation();
//            }
//        }
//    }
//    
//    /**
//     * update Animations
//     * @param tpf 
//     */
//    private void updateAnimations(float tpf, float trueTimeUsed) {
//        if (animations != null) {
//            for (int i = 0; i < animations.size(); i++) {
//                animations.get(i).update(tpf, trueTimeUsed);
//            }
//        }
//    }
//    
//    /**
//     * update Sound
//     * @param tpf 
//     */
//    private void updateSounds(float tpf, float trueTimeUsed) {
//        if (sounds != null) {
//            for (int i = 0; i < sounds.size(); i++) {
//                sounds.get(i).update(tpf, trueTimeUsed);
//            }
//        }
//    }
//    
//    // 查找被跟随的节点
//    private Spatial findTraceObject(long entityId) {
//        if (entityId > 0 && scene != null) {
//            Entity traceEntity = scene.getEntity(entityId);
//            if (traceEntity != null) {
//                return traceEntity.getSpatial();
//            }
//        }
//        return null;
//    }
//    
//    private void doUpdateTracePosition() {
//        // add type offset
//        Vector3f pos = getLocalTranslation();
//        
//        if (tracePositionType == TracePositionType.origin) {
//            pos.set(traceObject.getWorldTranslation());
//            
//        } else if (tracePositionType == TracePositionType.origin_bound_center) {
//            pos.set(traceObject.getWorldTranslation());
//            BoundingVolume bv = traceObject.getWorldBound();
//            if (bv != null) {
//                pos.setY(bv.getCenter().getY());
//            }
//            
//        } else if (tracePositionType == TracePositionType.origin_bound_top) {
//            GeometryUtils.getBoundTopPosition(traceObject, pos);
//            pos.setX(traceObject.getWorldTranslation().x);
//            pos.setZ(traceObject.getWorldTranslation().z);
//            
//        } else if (tracePositionType == TracePositionType.bound_center) {
//            pos.set(traceObject.getWorldBound().getCenter());
//            
//        } else if (tracePositionType == TracePositionType.bound_top) {
//            GeometryUtils.getBoundTopPosition(traceObject, pos);
//            
//        }
//        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
//        // 所以必须mult上目标对象的旋转
//        if (initLocation != null) {
//            TempVars tv = TempVars.get();
//            traceObject.getWorldRotation().mult(initLocation, tv.vect2);
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
//        if (initRotation != null) {
//            rot.multLocal(initRotation);
//        }
//        setLocalRotation(rot);
//    }
//    
//    /**
//     * 设置要跟随的场景中的物体的id
//     * @param entityId 
//     */
//    public void setTraceEntity(long entityId) {
//        this.traceEntityId = entityId;
//        setTraceObject(findTraceObject(traceEntityId));
//    }
//    
//    /**
//     * 直接设置要跟随的对象
//     * @param traceObject 
//     */
//    public void setTraceObject(Spatial traceObject) {
//        this.traceObject = traceObject;
//        // 重新查找被跟随的节点并立即初始化位置
//        if (initialized) {
//            initTrace();
//        }
//    }
//    
//    /**
//     * 默认效果的执行速度，默认为1.0
//     * @return 
//     */
//    public float getSpeed() {
//        return speed;
//    }
//    
//    /**
//     * 设置效果的运行速度，1.0为原始速度，2.0为二倍速度,依此类推.
//     * @param speed 
//     */
//    public void setSpeed(float speed) {
//        this.speed = speed;
//        if (this.speed <= 0) {
//            this.speed = 0.0001f;
//        }
//        // 更新时间
//        trueTimeTotal = useTime / this.speed;
//        // 更新动画时间
//        if (initialized) {
//            initAnimations();
//        }
//    }
//
//    @Override
//    public long getEntityId() {
//        return data.getUniqueId();
//    }
//
//    /**
//     * 返回特效所在的场景，注：如果特效并不是直接放在场景中(作为Entity)的，则这个方法可能返回null.
//     * @return 
//     */
//    @Override
//    public Scene getScene() {
//        return scene;
//    }
//
//    @Override
//    public void onInitScene(Scene scene) {
//        this.scene = scene;
//        scene.getRoot().attachChild(this);
//    }
//
//    @Override
//    public EntityModule getEntityModule() {
//        return entityModule;
//    }
//    
//    // ----------------------------------- Inner class
//    
//    protected class AnimationWrap {
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
//                    anim = Loader.load(animId);
//                }
//                anim.setSpeed(trueAnimSpeed);
//                anim.setTarget(animRoot);
//                anim.start();
//                started = true;
//            }
//        }
//        
//        void updateSpeed(float speed) {
//            trueAnimSpeed = speed;
//            if (anim != null) {
//                anim.setSpeed(speed);
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
//    protected class SoundWrap {
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
//                    attachChild(sound);
//                }
//                SoundManager.getInstance().addAndPlay(sound);
//                started = true;
//            }
//        }
//        
//        void cleanup() {
//            if (sound != null) {
//                SoundManager.getInstance().removeAndStopLoop(sound);
//            }
//            started = false;
//        }
//    }
//    
//}
