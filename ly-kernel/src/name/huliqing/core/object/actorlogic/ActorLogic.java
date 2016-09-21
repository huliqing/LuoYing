/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actorlogic;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.xml.DataProcessor;

/**
 * 角色逻辑
 * @author huliqing
 * @param <T>
 */
public abstract class ActorLogic<T extends ActorLogicData> implements DataProcessor<T>{
    
    protected T data;
    protected boolean initialized;
    protected float interval = 1;
    private float timeUsed;
    
    /**
     * 运行当前逻辑的角色.
     */
    protected Actor actor;

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getInterval();
    }

    @Override
    public T getData() {
        return data;
    }

    public void initialize() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
    
    public void update(float tpf) {
        timeUsed += tpf;
        if (timeUsed >= interval) {
            doLogic(tpf);
            timeUsed = 0;
        }
    }

    public void cleanup() {
        initialized = false;
    }
    
    /**
     * 获取执行逻辑的角色.
     * @return 
     */
    public Actor getActor() {
        return actor;
    }
    
    /**
     * 设置执行逻辑的角色。
     * @param self 
     */
    public void setActor(Actor self) {
        this.actor = self;
    }

    public void setInterval(float interval) {
        data.setInterval(interval);
        this.interval = interval;
    }

    public float getInterval() {
        return interval;
    }
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    protected abstract void doLogic(float tpf);
}
