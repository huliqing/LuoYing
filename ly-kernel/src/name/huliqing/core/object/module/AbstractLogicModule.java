/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.ModuleData;

/**
 * “逻辑”模块
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractLogicModule<T extends ModuleData> extends SimpleModule<T> implements LogicModule<T> {

    private boolean enabled;
    
     public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void update(float tpf) {
        if (!enabled)
            return;
        
        logicUpdate(tpf);
    }

    /**
     * 更新逻辑
     * @param tpf 
     */
    protected abstract void logicUpdate(float tpf);
    
}
