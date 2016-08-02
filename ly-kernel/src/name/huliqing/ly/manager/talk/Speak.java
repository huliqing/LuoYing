/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.manager.talk;

import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public abstract class Speak {
    
    // 该逻辑所需要执行的时间,单位秒
    protected float useTime;
    // 当前已经使用的时间，单位秒
    private float time;
    // 是否开始
    private boolean started = false;
    
    public Speak() {}

    public float getUseTime() {
        return useTime;
    }

    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }
    
    public void start() {
        started = true;
        time = 0;
        doInit();
    }

    public void update(float tpf) {
        if (!started) 
            return;
        
        time += tpf;
        if (time >= useTime) {
            cleanup();
        } else {
            doLogic(tpf);                
        }
    }

    /**
     * 结束当前speak并清理数据
     */
    public void cleanup() {
        started = false;
    }
    
    public boolean isEnd() {
        return !started;
    }
    
    /**
     * 获得说话者
     * @return 
     */
    public abstract Actor getActor();
    
    /**
     * 初始化
     */
    protected abstract void doInit();
    
    /**
     * 处理逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);


}
