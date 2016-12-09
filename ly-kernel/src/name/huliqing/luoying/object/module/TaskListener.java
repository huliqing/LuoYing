/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.task.Task;

/**
 * 监听角色完成任务操作
 * @author huliqing
 */
public interface TaskListener {
    
    /**
     * 当角色完成了某个任务的时候触发该方法。
     * @param actor
     * @param task 
     */
    void onTaskCompleted(Entity actor, Task task);
}
