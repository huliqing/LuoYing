/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.layer.network.TaskNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TaskListener;

/**
 *
 * @author huliqing
 */
public interface TaskService extends TaskNetwork {
    
    /**
     * 检查指定的任务是否完成。
     * @param actor
     * @param taskId 任务
     * @return 
     */
    boolean checkCompletion(Entity actor, String taskId);
    
    /**
     * 给角色添加一个任务侦听器
     * @param actor
     * @param taskListener 
     */
    void addTaskListener(Entity actor, TaskListener taskListener);

    /**
     * 从角色身上移除任务侦听器
     * @param actor
     * @param taskListener
     * @return 
     */
    boolean removeTaskListener(Entity actor, TaskListener taskListener);
}
