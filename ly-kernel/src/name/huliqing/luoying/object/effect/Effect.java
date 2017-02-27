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
package name.huliqing.luoying.object.effect;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.util.List;
import name.huliqing.luoying.data.DelayAnimData;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.ControlAdapter;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.anim.DelayAnim;
import name.huliqing.luoying.utils.GeometryUtils;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 特效
 * @author huliqing
 * @version v1.5 20170227
 * @version v1.4 20161011
 * @param <T>
 */
public class Effect<T extends EffectData> extends Node implements DataProcessor<T> {
    
    protected T data;
    
    /** 特效的总的使用时间，注：特效的使用时间受特效执行速度的影响。
     * 如果特效速度为2.0则实际执行时间只有useTime的一半，反之也然 */
    protected float useTime = 1.0f;
    
    /** 当前效果已经运行的时间。*/
    protected float timeUsed;
    
    /** 特效速度 */
    protected float speed = 1.0f;
    
    /** 是否循环 */
    protected boolean loop;
    
    /** 判断特效是否可以立即结束，默认false, 大部分情况下特效都应该”自然结束“，
     * 但是存在一些特殊特效，这些特效需要在被依赖的目标（如技能）结束的时候立即也一起结束。 */
    protected boolean endImmediate;
    
    /** 动画控制,所有动画控制器都作用在animRoot上。*/
    protected DelayAnim[] animations;
    
    /** 特效声音 */
    protected SoundWrap[] sounds;
    
    /** 特效跟随位置的类型 */
    private TraceType traceLocation;
    /** 特效跟随旋转的类型  */
    private TraceType traceRotation;
    /** 特效的初始偏移位置（跟随时用） */
    protected Vector3f traceLocationOffset;
    /** 特效的初始偏移旋转（跟随时用） */
    protected Quaternion traceRotationOffset;
    /** 特效跟随位置时的偏移类型 */
    protected TraceOffsetType traceLocationType;
    
    // ---------- inner
    
    /**
     * 动画(Anim)根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个节点用于接受所有动画变换的节点。
     * animations所执行的动画变换都作用在这个节点上。
     */
    protected final Node animNode = new Node("AnimEffectRoot");
    
    /** 跟踪的目标对象,必须必须有一个目标对象才有可能跟随 */
    protected Spatial traceObject;
    
    /** 实际效果所用的时间,这个实际时间受速度的影响,效果速度越快则效果的实际执行时间越少。*/
    protected float trueTimeTotal;
    
    protected boolean initialized;
    
    protected final ControlAdapter control = new ControlAdapter() {
        @Override
        public void update(float tpf) {
            effectUpdate(tpf);
        }
    };
    
    public Effect() {
        attachChild(animNode);
        addControl(control);
    }
    
    @Override
    public void setData(T data) {
        this.data = data;
        
        useTime = data.getAsFloat("useTime", useTime);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        speed = data.getAsFloat("speed", speed);
        loop = data.getAsBoolean("loop", loop);
        endImmediate = data.getAsBoolean("endImmediate", endImmediate);
        
        // 声音: "sound1 | startTime, sound2 | startTime, ..."
        String[] cArr = data.getAsArray("sounds");
        if (cArr != null && cArr.length > 0) {
            sounds = new SoundWrap[cArr.length];
            for (int i = 0; i < cArr.length; i++) {
                String[] dArr = cArr[i].split("\\|");
                SoundWrap sw = new SoundWrap(this);
                sw.soundId = dArr[0];
                sw.startTime = dArr.length > 1 ? Float.parseFloat(dArr[1]) : 0;
                sounds[i] = sw;
            }
        }
        
        // 状态的动画功能
        List<DelayAnimData> dads = data.getDelayAnimDatas();
        if (dads != null && dads.size() > 0) {
            animations = new DelayAnim[dads.size()];
            for (int i = 0; i < dads.size(); i++) {
                DelayAnim da = Loader.load(dads.get(i));
                da.setSpeed(speed);
                da.setTarget(animNode);
                animations[i] = da;
            }
        }
        
        // 跟随
        traceLocation = TraceType.identity(data.getAsString("traceLocation", TraceType.no.name()));
        traceRotation = TraceType.identity(data.getAsString("traceRotation", TraceType.no.name()));
        traceLocationOffset = data.getAsVector3f("traceLocationOffset");
        traceRotationOffset = data.getAsQuaternion("traceRotationOffset");
        traceLocationType = TraceOffsetType.identify(data.getAsString("traceLocationType", TraceOffsetType.origin.name()));
        
        String tempQueueBucket = data.getAsString("queueBucket");
        if (tempQueueBucket != null) {
            setQueueBucket(Bucket.valueOf(tempQueueBucket));
        }
        String tempCullHint = data.getAsString("cullHint");
        if (tempCullHint != null) {
            setCullHint(CullHint.valueOf(tempCullHint));
        }
        String tempShadowMode = data.getAsString("shadowMode");
        if (tempShadowMode != null) {
            setShadowMode(ShadowMode.valueOf(tempShadowMode));
        }
    }
    
    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
//     data.setAttribute("useTime", useTime); // 不改变的数据不需要更新
        data.setAttribute("timeUsed", timeUsed);
        data.setAttribute("speed", speed);
        if (animations != null) {
            for (DelayAnim da : animations) {
                da.updateDatas();
            }
        }
        // xxx to save Sounds data
    }
    
    /**
     * 初始化特效
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Effect already initialized! effect=" + data.getId());
        }
        initialized = true;
        
        // 计算实际时间
        trueTimeTotal = useTime / speed;
        
        if (traceObject != null) {
            if (traceLocation == TraceType.once || traceLocation == TraceType.always) {
                doUpdateTracePosition();
            }
            if (traceRotation == TraceType.once || traceRotation == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 更新效果逻辑
     * @param tpf 
     */
    protected void effectUpdate(float tpf) {
        if (!initialized) {
            return;
        }
        
        timeUsed += tpf;
        
        // 更新位置
        updateTrace();
        
        // 更新声音
         if (sounds != null) {
            for (SoundWrap sw : sounds) {
                sw.update(tpf, timeUsed);
            }
        }
        
        // 更新动画
        if (animations != null) {
            for (DelayAnim da : animations) {
                da.update(tpf);
            }
        }
        
        if (timeUsed >= trueTimeTotal) {
            if (loop) {
                timeUsed = 0;
            } else {
                doEndEffect();
            }
        }
    }
    
    /**
     * 清理效果数据.
     */
    public void cleanup() {
        initialized = false;
        timeUsed = 0;
         if (sounds != null) {
            for (SoundWrap aw : sounds) {
                aw.cleanup();
            }
        }
        
        if (animations != null) {
            for (DelayAnim anim : animations) {
                anim.cleanup();
            }
        }
        
        removeFromParent();
    }
    
    /**
     * 请求结束特效，一般情况下不要直接结束特效（如：cleanup)，因为一些特效如果直接结束会非常不自然和难看，
     * 所以在调用特效，并希望结束一个特效时应该使用这个方法来请求结束一个特效，
     * 而具体是否结束或者如何结束一个特效由具体的子类去实现. 
     */
    public void requestEnd() {
        if (endImmediate) {
            doEndEffect();
        }
    }
    
    /**
     * 强制结束效果，当效果运行时间结束时这个方法会被调用，来结束效果，这个方法也可以由子类进行调用，
     * 当子类需要特殊结束效果时可以直接调用这个方法。
     */
    protected void doEndEffect() {
        cleanup();
    }
    
    /**
     * 判断特效是否已经结束，如果该方法返回true,则特效逻辑将不再执行。
     * @return 
     */
    public boolean isEnd() {
        if (!initialized) {
            return true;
        }
        
        if (!loop && timeUsed >= trueTimeTotal) {
            return true;
        }
        return false;
    }
    
    /**
     * 直接设置要跟随的对象
     * @param traceObject 
     */
    public void setTraceObject(Spatial traceObject) {
        this.traceObject = traceObject;
    }
    
    /**
     * 默认效果的执行速度，默认为1.0
     * @return 
     */
    public float getSpeed() {
        return speed;
    }
    
    /**
     * 设置效果的运行速度，1.0为原始速度，2.0为二倍速度,依此类推.
     * @param speed 
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        if (this.speed <= 0) {
            this.speed = 0.0001f;
        }
        // 更新时间
        trueTimeTotal = useTime / this.speed;
        
        // 速度更新时要一起更新动画速度
        if (animations != null) {
            for (DelayAnim da : animations) {
                da.setSpeed(speed);
            }
        }
    }
    
    /**
     * 更新跟随位置
     */
    private void updateTrace() {
        if (traceObject != null) {
            if (traceLocation == TraceType.always) {
                doUpdateTracePosition();
            }
            if (traceRotation == TraceType.always) {
                doUpdateTraceRotation();
            }
        }
    }
    
    private void doUpdateTracePosition() {
        // add type offset
        Vector3f pos = getLocalTranslation();
        
        if (traceLocationType == TraceOffsetType.origin) {
            pos.set(traceObject.getWorldTranslation());
            
        } else if (traceLocationType == TraceOffsetType.origin_bound_center) {
            pos.set(traceObject.getWorldTranslation());
            BoundingVolume bv = traceObject.getWorldBound();
            if (bv != null) {
                pos.setY(bv.getCenter().getY());
            }
            
        } else if (traceLocationType == TraceOffsetType.origin_bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
            pos.setX(traceObject.getWorldTranslation().x);
            pos.setZ(traceObject.getWorldTranslation().z);
            
        } else if (traceLocationType == TraceOffsetType.bound_center) {
            pos.set(traceObject.getWorldBound().getCenter());
            
        } else if (traceLocationType == TraceOffsetType.bound_top) {
            GeometryUtils.getBoundTopPosition(traceObject, pos);
            
        }
        // 注：tracePositionOffset是以被跟随的目标对象的本地坐标为基准的,
        // 所以必须mult上目标对象的旋转
        if (traceLocationOffset != null) {
            TempVars tv = TempVars.get();
            traceObject.getWorldRotation().mult(traceLocationOffset, tv.vect2);
            pos.addLocal(tv.vect2);
            tv.release();
        }
        setLocalTranslation(pos);
//        LOG.log(Level.INFO, "AbstractEffect doUpdateTracePosition, pos={0}", new Object[] {pos});
    }
    
    private void doUpdateTraceRotation() {
        Quaternion rot = getLocalRotation();
        rot.set(traceObject.getWorldRotation());
        if (traceRotationOffset != null) {
            rot.multLocal(traceRotationOffset);
        }
        setLocalRotation(rot);
    }
    
}
