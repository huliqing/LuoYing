/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.ModuleData;

/**
 * 拥有逻辑功能的模块,这种模块拥有更新功能。
 * @author huliqing
 * @param <T>
 */
public interface LogicModule<T extends ModuleData> extends Module<T> {
    
    /**
     * 更新模块逻辑
     * @param tpf 
     */
    void update(float tpf);
    
}
