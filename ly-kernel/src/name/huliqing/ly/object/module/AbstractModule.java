/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.object.actor.Actor;

/**
 * Module的抽象类,所有角色模块可以直接继承自这个基类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractModule<T extends ModuleData> implements Module<T> {

    protected T data;
    protected boolean initialized;
    protected int moduleOrder;
    protected Actor actor;

    @Override
    public void setData(T data) {
        this.data = data;
        this.moduleOrder = data.getAsInteger("moduleOrder", moduleOrder);
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        // ignore
    }
    
    @Override
    public void initialize(Actor actor) {
        if (initialized) {
            throw new IllegalStateException("Module is already initialized! module=" + getClass());
        }
        this.actor = actor;
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }

    @Override
    public Actor getActor() {
        return actor;
    }
    
}
