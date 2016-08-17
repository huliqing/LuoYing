/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.ModuleData;

/**
 * 简单的模块，这种模块不需要实现任何更新逻辑,只需要实现初始化和清理数据就可以。
 * @author huliqing
 * @param <T>
 */
public class SimpleModule<T extends ModuleData> implements Module<T> {

    protected T data;
    protected boolean initialized;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void initialize() {
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
    
}
