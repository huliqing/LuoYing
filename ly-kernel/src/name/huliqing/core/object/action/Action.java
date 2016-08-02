/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import name.huliqing.core.data.ActionData;
import name.huliqing.core.object.DataProcessor;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface Action extends DataProcessor<ActionData>{
    
    /**
     * 开始行为
     */
    void start();
    
    /**
     * 更新行为逻辑
     * @param tpf 
     */
    void update(float tpf);
    
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
     * 清理行为数据，当行为正常退出或被打断时都会调用该方法来清理数
     * 据。
     */
    void cleanup();
    
    /**
     * 设置执行行为的角色。
     * @param actor 
     */
    void setActor(Actor actor);
}
