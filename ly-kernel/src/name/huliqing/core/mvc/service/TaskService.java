/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.Inject;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.task.Task;

/**
 *
 * @author huliqing
 */
public interface TaskService extends Inject {

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
     * 添加一个任务给角色
     * @param actor
     * @param task
     */
    void addTask(Actor actor, Task task);
    
    /**
     * 从角色身上获取任务。如果角色没有接受过指定ID的任务，则该方法返回null.
     * @param actor
     * @param taskId
     * @return 
     */
    Task getTask(Actor actor, String taskId);
    
    /**
     * 获取角色的所有任务，包含完成的和正在进行的。
     * 注：返回的列表不要随便修改,只允许只读使用。
     * @param actor
     * @return 
     */
    List<Task> getTasks(Actor actor);
    
    /**
     * 获得角色的所有任务data;返回的列表不允许修改。
     * @param actor
     * @return 
     */
    List<TaskData> getTaskDatas(Actor actor);
    
    /**
     * 检查指定的任务是否完成。
     * @param actor
     * @param task 任务
     * @return 
     */
    boolean checkCompletion(Actor actor, Task task);
    
    /**
     * 完成指定的任务
     * @param actor
     * @param task
     */
    void completeTask(Actor actor, Task task);
    
    /**
     * 增加或减少任务物品的数量,任务物品并不作为普通物品一样存放在角色包裹上
     * ,因为任务物品不能使用、删除
     * @param actor 角色
     * @param task 任务
     * @param itemId 任务物品ID
     * @param amount 要增加或减少的任务物品数量，可正可负 
     */
    void applyItem(Actor actor, Task task, String itemId, int amount);
    
    /**
     * 获取当前已经收集到的任务物品的数量
     * @param actor
     * @param task
     * @param itemId
     * @return 
     */
    int getItemTotal(Actor actor, Task task, String itemId);
}
