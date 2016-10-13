/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.layer.network.TaskNetwork;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.TaskListener;
import name.huliqing.ly.object.task.Task;

/**
 *
 * @author huliqing
 */
public interface TaskService extends TaskNetwork {

    /**
     * 载入一个任务
     * @param taskId
     * @return 
     */
    Task loadTask(String taskId);
    
    /**
     * 载入任务
     * @param taskData
     * @return 
     */
    Task loadTask(TaskData taskData);
    
    /**
     * 从角色身上获取任务。如果角色没有接受过指定ID的任务，则该方法返回null.
     * @param actor
     * @param taskId
     * @return 
     */
    Task getTask(Entity actor, String taskId);
    
    /**
     * 获取角色的所有任务，包含完成的和正在进行的。
     * 注：返回的列表不要随便修改,只允许只读使用。
     * @param actor
     * @return 
     */
    List<Task> getTasks(Entity actor);
   
    // remove
//    /**
//     * 获得角色的所有任务data;返回的列表不允许修改。
//     * @param actor
//     * @return 
//     */
//    List<TaskData> getTaskDatas(Entity actor);
    
    /**
     * 检查指定的任务是否完成。
     * @param actor
     * @param task 任务
     * @return 
     */
    boolean checkCompletion(Entity actor, Task task);
    
    /**
     * 获取当前已经收集到的任务物品的数量
     * @param actor
     * @param task
     * @param itemId
     * @return 
     */
    int getItemTotal(Entity actor, Task task, String itemId);
    
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
