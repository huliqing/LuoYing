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

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;
import java.util.List;
import name.huliqing.luoying.data.DelayAnimData;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.ControlAdapter;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.anim.DelayAnim;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * 特效
 * @author huliqing
 * @version v1.6 20170416 分离trace功能到TraceEffect.java
 * @version v1.5 20170227
 * @version v1.4 20161011
 */
public class Effect extends Node implements DataProcessor<EffectData> {
    
    protected EffectData data;
    
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
    
    // ---------- inner
    
    /**
     * 动画(Anim)根作点, 为了隔离Effect自身的变换和Anim所执行的动画变换，必须提供一个节点用于接受所有动画变换的节点。
     * animations所执行的动画变换都作用在这个节点上。
     */
    protected final Node animNode = new Node("AnimEffectRoot");
    
    /** 实际效果所用的时间,这个实际时间受速度的影响,效果速度越快则效果的实际执行时间越少。*/
    protected float trueTimeTotal;
    
    protected boolean initialized;
    
    protected final ControlAdapter control = new ControlAdapter() {
        @Override
        public void update(float tpf) {
            effectUpdate(tpf);
        }
    };
    
    public interface EffectListener {
        /**
         * 当特效结束时该方法被调用。
         * @param eff 
         */
        void onEffectEnd(Effect eff);
    }
    
    private SafeArrayList<EffectListener> listeners;
    
    @Override
    public EffectData getData() {
        return data;
    }
    
    @Override
    public void setData(EffectData data) {
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
        
        Vector3f location = data.getAsVector3f("location");
        if (location != null) {
            setLocalTranslation(location);
        }
        Quaternion rotation = data.getAsQuaternion("rotation");
        if (rotation != null) {
            setLocalRotation(rotation);
        }
        Vector3f scale = data.getAsVector3f("scale");
        if (scale != null) {
            setLocalScale(scale);
        }
        
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
    public void updateDatas() {
//     data.setAttribute("useTime", useTime); // 不改变的数据不需要更新
        data.setAttribute("timeUsed", timeUsed);
        data.setAttribute("speed", speed);
        data.setAttribute("location", getLocalTranslation());
        data.setAttribute("rotation", getLocalRotation());
        data.setAttribute("scale", getLocalScale());
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
        
        // 动画根节点和“更新”控制器
        attachChild(animNode);
        addControl(control);
        
        // 计算实际时间
        trueTimeTotal = useTime / speed;
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
                if (listeners != null) {
                    for (EffectListener el : listeners.getArray()) {
                        el.onEffectEnd(this);
                    }
                }
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
     * 判断特效是否已经结束，如果该方法返回true,则特效逻辑将不再执行。
     * @return 
     */
    public boolean isEnd() {
        if (!initialized) {
            return true;
        }
        
        return !loop && timeUsed >= trueTimeTotal;
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
     * 添加特效监听器
     * @param listener 
     */
    public void addListener(EffectListener listener) {
        if (listeners == null) {
            listeners = new SafeArrayList<EffectListener>(EffectListener.class);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public boolean removeListener(EffectListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    // remove20170416
//    /**
//     * 设置效果要跟随或添加到的目标对象, 如果目标对象为Node，则直接将当前效果添加到该节点下面。
//     * 否则如果目标为Geometry则偿试将当前效果添加到其父对象中。
//     * @param traceObject 
//     */
//    public void setTraceObject(Spatial traceObject) {
//        if (traceObject instanceof Node) {
//            ((Node) traceObject).attachChild(this);
//            return;
//        }
//        if (traceObject instanceof Geometry) {
//            Node toParent = traceObject.getParent();
//            if (toParent != null) {
//                toParent.attachChild(this);
//                return;
//            }
//        }
//        throw new UnsupportedOperationException("TraceObject need to be a Node or a geometry with a parent!");
//    }
    
    /**
     * 强制结束效果，当效果运行时间结束时这个方法会被调用，来结束效果，这个方法也可以由子类进行调用，
     * 当子类需要特殊结束效果时可以直接调用这个方法。
     */
    protected void doEndEffect() {
        cleanup();
    }

    
}
