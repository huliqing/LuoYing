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

import com.jme3.math.FastMath;
import com.jme3.util.SafeArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.AnimData;

/**
 * 动画控制，V2
 * @author huliqing
 * @param <E> 
 */
public abstract class AbstractAnim<E> implements Anim<E> {
    private final static Logger LOG = Logger.getLogger(AbstractAnim.class.getName());
     
    // 默认的bezier参数: {p0,p1,p2,p3}
    private final static float[] DEFAULT_BEZIER_FACTOR = new float[] {0, 0, 0, 1};
    // 默认的catmullRom参数: {T, p0,p1,p2,p3}
    private final static float[] DEFAULT_CATMULLROM_FACTOR = new float[] {0.5f, 0, 0, 1, 0};
    
    public enum MotionType {
        Linear,
        Bezier,
        CatmullRom;
        
        public static MotionType identify(String name) {
            if (name == null)
                return null;
            
            for (MotionType mt : values()) {
                if (mt.name().equals(name)) {
                    return mt;
                }
            }
            return null;
        }
    }
    
    protected AnimData data;
    
    // 提示显示调试信息，但是具体由各个子类去实现。
    protected boolean debug;
    
    // 动画时间，这个指定的时间是在标准速度为1的情况下的时间。
    // 跟据速度的不同，实际的动画时间会不同。实际动画时间: realTime = useTime / speed;
    protected float useTime = 1f;
    // 动画速度
    protected float speed = 1;
    protected Loop loop = Loop.dontLoop;
    
    protected MotionType motionType;
    
    protected float[] bezierFactor;
    protected float[] catmullRomFactor;
    
    // ---- 内部参数 ----
    protected SafeArrayList<Listener> listeners;
    protected E target;
    
    // 当前的动画"时间"插值。[0.0~1.0]
    protected float timeInterpolation;
    // 当前的实际动画插值，这个插值受运动方式的影响。
    protected float trueInterpolation;
    
    protected boolean paused;
    protected boolean initialized;
    
    // 正向1, 反向 -1
    private int dir = 1;
    
    @Override
    public void setData(AnimData data) {
        this.data = data;
        debug = data.getAsBoolean("debug", debug);
        useTime = data.getAsFloat("useTime", useTime);
        // 不能让useTime小于0,至少取一个非常小的值。
        if (useTime <= 0) {
            useTime = 0.0001f;
        }
        speed = data.getAsFloat("speed", speed);
        
        String tempLoop = data.getAsString("loop");
        if (tempLoop != null) {
            loop = Loop.identify(tempLoop);
        }
        
        motionType = MotionType.identify(data.getAsString("motionType"));
        if (motionType == null) {
            motionType = MotionType.Linear;
        }

        if (motionType == MotionType.Bezier) {
            this.bezierFactor = data.getAsFloatArray("bezierFactor");
            if (bezierFactor.length < 4) {
                LOG.log(Level.WARNING
                        , "bezierFactor length could not less than 4. animId={0}, bezierFactor={1}"
                        , new Object[] {data.getId(), Arrays.toString(bezierFactor)});
                bezierFactor = null;
            }
        }
        
        if (motionType == MotionType.CatmullRom) {
            this.catmullRomFactor = data.getAsFloatArray("catmullRomFactor");
            if (catmullRomFactor.length < 5) {
                LOG.log(Level.WARNING
                        , "catmullRomFactor length could not less than 5. animId={0}, catmullRomFactor={1}"
                        , new Object[] {data.getId(), Arrays.toString(catmullRomFactor)});
                catmullRomFactor = null;
            }
        }
        
        paused = data.getAsBoolean("paused", paused);
        timeInterpolation = data.getAsFloat("timeInterpolation", timeInterpolation);
        dir = data.getAsInteger("dir", dir);
    }

    @Override
    public AnimData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        data.setAttribute("useTime", useTime);
        data.setAttribute("speed", speed);
        data.setAttribute("loop", loop.name());
        data.setAttribute("paused", paused);
        data.setAttribute("timeInterpolation", timeInterpolation);
        data.setAttribute("dir", dir);
    }
    
    @Override
    public E getTarget() {
        return target;
    }

    @Override
    public void setTarget(E target) {
        this.target = target;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getUseTime() {
        return useTime;
    }

    @Override
    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }
    
    @Override
    public float getInterpolation() {
        return trueInterpolation;
    }
    
    @Override
    public Loop getLoop() {
        return loop;
    }

    @Override
    public void setLoop(Loop loop) {
        this.loop = loop;
    }
    
    @Override
    public boolean isInverse() {
        return dir < 0;
    }

    @Override
    public void start() {
        if (initialized) {
            return;
        }
        
        // Animation可以通过直接调用display(float)方法来让动画直接显示在某一个位置
        // 当调用display(float方法时也会进行doInit,因此这里需要进行init判断，确定是否
        // 已经初始化
        if (!initialized) {
            doAnimInit();
            initialized = true;
        }
    }
    
    @Override
    public void update(float tpf) {
        if (!initialized || paused) {
            return;
        } 
        
        timeInterpolation += tpf * speed * dir / useTime;
        
//        logger.log(Level.INFO, "AbstractAnim update, class={0}, interpolation={1}"
//                , new Object[] {getClass(), interpolation});
        
        boolean stepEnd = false;
        if (timeInterpolation > 1) {
            timeInterpolation = 1;
            stepEnd = true;
        } else if (timeInterpolation < 0) {
            timeInterpolation = 0;
            stepEnd = true;
        }
        
        display(timeInterpolation);
        
        if (stepEnd) {
            if (loop == Loop.dontLoop) {
                
                doListenerDone();
                cleanup();
                
            } else if (loop == Loop.loop) {
                
                doListenerDone();
                timeInterpolation = 0;
                
            } else if (loop == Loop.cycle) {
                
                if (isInverse()) {
                    doListenerDone();
                }
                dir = -dir;
                
            }
        }
        
    }

    @Override
    public void pause(boolean paused) {
        this.paused = paused;
    }
    
    private void doListenerDone() {
        if (listeners == null) {
            return;
        }
        for (Listener l : listeners.getArray()) {
            l.onDone(this);
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean isEnd() {
        return !initialized;
    }
    
    @Override
    public void cleanup() {
        paused = false;
        initialized = false;
        timeInterpolation = 0;
    }
    
    /**
     * 添加动画侦听器
     * @param listener 
     */
    @Override
    public void addListener(Listener listener) {
        if (listener == null) {
            throw new NullPointerException("listener could not be null!");
        }
        if (listeners == null) {
            listeners = new SafeArrayList<Listener>(Listener.class);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public boolean removeListener(Listener listener) {
        if (listeners != null) {
            return listeners.remove(listener);
        }
        return false;
    }
    
    @Override
    public void display(float timeInterpolation) {
        if (!initialized) {
            doAnimInit();
            initialized = true;
        }
        
        if (motionType == MotionType.Bezier) {
            if (bezierFactor != null) {
                trueInterpolation = FastMath.interpolateBezier(timeInterpolation
                        , bezierFactor[0]
                        , bezierFactor[1]
                        , bezierFactor[2]
                        , bezierFactor[3]);
            } else {
                trueInterpolation = FastMath.interpolateBezier(timeInterpolation
                        , DEFAULT_BEZIER_FACTOR[0]
                        , DEFAULT_BEZIER_FACTOR[1]
                        , DEFAULT_BEZIER_FACTOR[2]
                        , DEFAULT_BEZIER_FACTOR[3]);
            }
        } else if (motionType == MotionType.CatmullRom) {
            if (catmullRomFactor != null) {
                trueInterpolation = FastMath.interpolateCatmullRom(timeInterpolation
                        , catmullRomFactor[0]
                        , catmullRomFactor[1]
                        , catmullRomFactor[2]
                        , catmullRomFactor[3]
                        , catmullRomFactor[4]);
            } else {
                trueInterpolation = FastMath.interpolateCatmullRom(timeInterpolation
                        , DEFAULT_CATMULLROM_FACTOR[0]
                        , DEFAULT_CATMULLROM_FACTOR[1]
                        , DEFAULT_CATMULLROM_FACTOR[2]
                        , DEFAULT_CATMULLROM_FACTOR[3]
                        , DEFAULT_CATMULLROM_FACTOR[4]);
            }
        } else {
            // 默认MotionType=Linear
            trueInterpolation = timeInterpolation;
        }
        
        doAnimUpdate(trueInterpolation);
    }

    public void setMotionType(MotionType motionType) {
        this.motionType = motionType;
    }

    public void setBezierFactor(float[] bezierFactor) {
        this.bezierFactor = bezierFactor;
    }

    public void setCatmullRomFactor(float[] catmullRomFactor) {
        this.catmullRomFactor = catmullRomFactor;
    }
    
    /**
     * 初始化动画。
     */
    protected abstract void doAnimInit();
    
    /**
     * 实现动画，根据指定的插值位置渲染动画。
     * @param interpolation 
     */
    protected abstract void doAnimUpdate(float interpolation);

}
