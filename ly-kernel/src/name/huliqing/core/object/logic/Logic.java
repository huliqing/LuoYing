/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.logic;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.LogicData;
import name.huliqing.core.xml.DataProcessor;

/**
 * 角色逻辑
 * @author huliqing
 * @param <T>
 */
public abstract class Logic<T extends LogicData> implements DataProcessor<T>{
    
    protected T data;
    protected boolean initialized;
    protected float interval = 1.0f;
    protected float timeUsed;
    
    /**
     * 运行当前逻辑的角色.
     */
    protected Actor actor;

    @Override
    public void setData(T data) {
        this.data = data;
        interval = data.getAsFloat("interval", interval);
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
     * 设置执行逻辑的角色。
     * @param self 
     */
    public void setActor(Actor self) {
        this.actor = self;
    }

    public void setInterval(float interval) {
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
