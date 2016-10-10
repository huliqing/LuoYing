/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.action;

import name.huliqing.ly.data.ActionData;
import name.huliqing.ly.xml.DataProcessor;
import name.huliqing.ly.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public interface Action<T extends ActionData> extends DataProcessor<T>{
    
    /**
     * 开始行为
     */
    void initialize();
    
    /**
     * 判断行为是否已经初始化
     * @return 
     */
    boolean isInitialized();
    
    /**
     * 更新行为逻辑
     * @param tpf 
     */
    void update(float tpf);
    
    /**
     * 清理行为数据，当行为正常退出或被打断时都会调用该方法来清理数
     * 据。
     */
    void cleanup();
    
    /**
     * 判断行为是否已经正常结束
     * @return 
     */
    boolean isEnd();
    
    /**
     * 锁定当前行为一定时间，让行为不更新，单位秒。
     * @param time 
     */
    void lock(float time);
    
    /**
     * 设置执行行为的角色。
     * @param actor 
     */
    void setActor(Entity actor);
}
