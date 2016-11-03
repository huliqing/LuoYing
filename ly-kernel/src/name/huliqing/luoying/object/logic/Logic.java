/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.logic;

import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public interface Logic<T extends LogicData> extends DataProcessor<T> {

    @Override
    public void setData(T data);

    @Override
    public T getData();
    
    /**
     * 初始化逻辑
     */
    void initialize();

    /**
     * 判断逻辑是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新逻辑
     * @param tpf 
     */
    void update(float tpf);

    /**
     * 清理并释放资源
     */
    void cleanup();
    
    /**
     * 设置执行逻辑的角色。
     * @param self 
     */
    void setActor(Entity self);

}
