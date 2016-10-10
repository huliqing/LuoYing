/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.anim;

import com.jme3.math.FastMath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.AnimData;
import name.huliqing.ly.layer.service.PlayService;

/**
 * 动画控制，V2
 * @author huliqing
 * @param <E> 
 */
public abstract class AbstractAnim<E> implements Anim<AnimData, E> {
    private final static Logger LOG = Logger.getLogger(AbstractAnim.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    
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
    protected List<Listener> listeners;
    protected E target;
    
    // 当前的动画"时间"插值。[0.0~1.0]
    protected float timeInterpolation;
    // 当前的实际动画插值，这个插值受运动方式的影响。
    protected float trueInterpolation;
    
    protected boolean started;
    protected boolean paused;
    protected boolean initialized;
    
    // 正向1, 反向 -1
    private int dir = 1;
    
    @Override
    public void setData(AnimData data) {
        this.data = data;
        this.debug = data.getAsBoolean("debug", debug);
        this.useTime = data.getAsFloat("useTime", useTime);
        // 不能让useTime小于0,至少取一个非常小的值。
        if (useTime <= 0) {
            useTime = 0.0001f;
        }
        this.speed = data.getAsFloat("speed", speed);
        String tempLoop = data.getAsString("loop");
        if (tempLoop != null) {
            loop = Loop.identify(tempLoop);
        }
        this.motionType = MotionType.identify(data.getAsString("motionType"));
        if (this.motionType == null) {
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
    }

    @Override
    public AnimData getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
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
        return this.trueInterpolation;
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
        if (started) {
            return;
        }
        
        // Animation可以通过直接调用display(float)方法来让动画直接显示在某一个位置
        // 当调用display(float方法时也会进行doInit,因此这里需要进行init判断，确定是否
        // 已经初始化
        if (!initialized) {
            doInit();
            initialized = true;
        }
        started = true;
    }
    
    @Override
    public void update(float tpf) {
        if (!started || paused) {
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
                doEnd();
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
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDone(this);
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean isEnd() {
        return !started;
    }

    /**
     * 结束当前动画
     */
    protected final void doEnd() {
        
        // 从场景中移除动画控制器
//        playService.removeAnimation(this);
        throw new UnsupportedOperationException("....Debug test...这里要重构");

    }
    
    @Override
    public void cleanup() {
        started = false;
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
            throw new NullPointerException("Listener could not be null!");
        }
        if (listeners == null) {
            listeners = new ArrayList<Listener>(1);
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
            doInit();
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
        
        doAnimation(trueInterpolation);
    }
    
    /**
     * 检查插值点是否有效（在[0,1]范围内，如果无效，则抛出异常
     * @param interpolation 
     */
    protected void checkValid(float interpolation) {
        if (interpolation < 0 || interpolation > 1) {
            throw new IllegalArgumentException(
                    "interpolation could not less than 0 or more than 1! interpolation=" + interpolation);
        }
    }
    
    /**
     * 初始化动画。
     */
    protected abstract void doInit();
    
    /**
     * 实现动画，根据指定的插值位置渲染动画。
     * @param interpolation 
     */
    protected abstract void doAnimation(float interpolation);

}
