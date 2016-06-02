/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.anim;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.AnimData;
import name.huliqing.fighter.game.service.PlayService;

/**
 * 动画控制，V2
 * @author huliqing
 */
public abstract class AbstractAnim<T> implements Anim<T> {
//    private final static Logger logger = Logger.getLogger(AbstractAnim.class.getName());
    private final static PlayService playService = Factory.get(PlayService.class);
    
    // 提示显示调试信息，但是具体由各个子类去实现。
    protected boolean debug;
    
    // 动画时间，这个指定的时间是在标准速度为1的情况下的时间。
    // 跟据速度的不同，实际的动画时间会不同。实际动画时间: realTime = useTime / speed;
    protected float useTime = 1f;
    // 动画速度
    protected float speed = 1;
    protected Loop loop = Loop.dontLoop;
    
    // ---- 内部参数 ----
    protected List<Listener> listeners;
    protected T target;
    // 当前的动画时间插值。[0.0~1.0]
    protected float interpolation;
    
    protected boolean started;
    protected boolean paused;
    protected boolean init;
    
    // 正向1, 反向 -1
    private int dir = 1;
    
    public AbstractAnim() {}
    
    public AbstractAnim(AnimData data) {
        this.debug = data.getAsBoolean("debug", debug);
        this.useTime = data.getAsFloat("useTime", useTime);
        // 不能让useTime小于0,至少取一个非常小的值。
        if (useTime <= 0) {
            useTime = 0.001f;
        }
        this.speed = data.getAsFloat("speed", speed);
        String tempLoop = data.getAttribute("loop");
        if (tempLoop != null) {
            loop = Loop.identify(tempLoop);
        }
    }
    
    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public void setTarget(T target) {
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
        return this.interpolation;
    }

    @Override
    public void setInterpolation(float interpolation) {
        checkValid(interpolation);
        this.interpolation = interpolation;
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
        if (!init) {
            doInit();
            init = true;
        }
        started = true;
    }
    
    @Override
    public void update(float tpf) {
        if (!started || paused) {
            return;
        } 
        
        interpolation += tpf * speed * dir / useTime;
        
//        logger.log(Level.INFO, "AbstractAnim update, class={0}, interpolation={1}"
//                , new Object[] {getClass(), interpolation});
        
        boolean stepEnd = false;
        if (interpolation > 1) {
            interpolation = 1;
            stepEnd = true;
        } else if (interpolation < 0) {
            interpolation = 0;
            stepEnd = true;
        }
        
        display(interpolation);
        
        // remove20160214,没有什么意义
//        // listener
//        doListenerDisplay();
        
        if (stepEnd) {
            if (loop == Loop.dontLoop) {
                doListenerDone();
//                cleanup();
                doEnd();
            } else if (loop == Loop.loop) {
                doListenerDone();
                interpolation = 0;
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
    
    // remove20160214,没有什么意义
//    private void doListenerDisplay() {
//        if (listeners == null) {
//            return;
//        }
//        for (int i = 0; i < listeners.size(); i++) {
//            listeners.get(i).onDisplay(this);
//        }
//    }
    
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
        playService.removeAnimation(this);
    }
    
    @Override
    public void cleanup() {
        started = false;
        paused = false;
        init = false;
        interpolation = 0;
        
        // remove20160217
//        // 从场景中移除动画控制器
//        playService.removeAnimation(this);
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
    
    /**
     * 获取动画侦听器,如果没有添加过侦听器，则返回null.
     * @return 
     */
    @Override
    public List<Listener> getListeners() {
        return listeners;
    }
    
    @Override
    public boolean removeListener(Listener listener) {
        if (listeners != null) {
            return listeners.remove(listener);
        }
        return false;
    }
    
    @Override
    public void display(float interpolation) {
        if (!init) {
            doInit();
            init = true;
        }
        doAnimation(interpolation);
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
