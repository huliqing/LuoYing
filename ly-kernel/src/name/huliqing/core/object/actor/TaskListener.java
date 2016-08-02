/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import name.huliqing.core.object.task.Task;

/**
 * 监听角色接受任务、完成任务、等操作
 * @author huliqing
 */
public interface TaskListener {
    
    /**
     * 当角色添加了任务的时候触发该方法，即在接受了任务之后该方法会立即被调用。
     * @param source
     * @param task 
     */
    void onTaskAdded(Actor source, Task task);
    
    /**
     * 当角色完成了某个任务的时候触发该方法。
     * @param actor
     * @param task 
     */
    void onTaskCompleted(Actor actor, Task task);
}
