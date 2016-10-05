/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.animation;

import com.jme3.scene.Spatial;
import java.util.logging.Logger;

/**
 * 基本的UI动画控制v1
 * @deprecated use Anim instead
 * @author huliqing
 */
public abstract class AbstractAnimation implements Animation{
    private final static Logger logger = Logger.getLogger(AbstractAnimation.class.getName());

    // 动画使用的总时间
    protected float useTime = 0.3f;
    // 目标要执行动画的UI
    protected Spatial target;
    
    private boolean started;
    private boolean init;
    // 当前已经使用的时间，单位秒
    protected float time;
    
    @Override
    public void setAnimateTime(float useTime) {
        this.useTime = useTime;
    }

    @Override
    public float getAnimateTime() {
        return this.useTime;
    }

    @Override
    public Spatial getTarget() {
        return this.target;
    }
    
    @Override
    public void setTarget(Spatial target) {
        this.target = target;
    }
    
    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void update(float tpf) {
        if (!started) {
            return;
        }
        
        if (!init) {
            doInit();
            init = true;
        }
        
        time += tpf;
        doAnimation(tpf);
        
        if (checkEnd()) {
            cleanup();
        }
    }
    
    /**
     * 检查是否动画已经结束
     * @return 
     */
    protected boolean checkEnd() {
        return time >= useTime;
    }
    
    @Override
    public boolean isEnd() {
        return !started;
    }
    
    @Override
    public void cleanup() {
        this.started = false;
        this.init = false;
        this.target = null;
        this.time = 0;
    }
    
    @Override
    public float getTime() {
        return time;
    }
    
    /**
     * 初始化UIAnimation
     */
    protected abstract void doInit();
    
    /**
     * 执行UIAnimation逻辑
     * @param tpf 
     */
    protected abstract void doAnimation(float tpf);

}
