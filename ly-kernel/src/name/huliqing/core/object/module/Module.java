/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.ModuleData;
import name.huliqing.core.xml.DataProcessor;

/**
 * 角色的扩展模块
 * @author huliqing
 * @param <T>
 */
public interface Module<T extends ModuleData> extends DataProcessor<T> {
    
    /**
     * 初始化模块
     */
    void initialize();
    
    /**
     * 判断模块是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 清理模块
     */
    void cleanup();
}
