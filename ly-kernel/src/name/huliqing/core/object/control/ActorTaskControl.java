/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TaskListener;
import name.huliqing.core.object.task.Task;

/**
 * 任务管理
 * @author huliqing
 */
public class ActorTaskControl extends ActorControl {

    private Actor actor;
    // 角色任务列表
    private List<Task> tasks;
    
    // 监听任务
    private List<TaskListener> taskListeners;

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.actor = actor;
    }
    
    @Override
    public void actorUpdate(float tpf) {
    }

    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {
    }
    
    /**
     * 添加任务
     * @param task 
     */
    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<Task>();
        }
        if (!tasks.contains(task)) {
            task.setActor(actor);
            task.initialize();
            tasks.add(task);
        }
    }
    
    /**
     * 移除任务
     * @param task
     * @return 
     */
    public boolean removeTask(Task task) {
        task.cleanup();
        return tasks != null && tasks.remove(task);
    }
    
    /**
     * 获取指定ID的任务，如果不存在则返回null.
     * @param taskId
     * @return 
     */
    public Task getTask(String taskId) {
        if (tasks == null) 
            return null;
        
        for (Task t : tasks) {
            if (t.getId().equals(taskId)) {
                return t;
            }
        }
        return null;
    }
    
    /**
     * 获取角色的任务列表，如果不存在任务列表则返回null
     * @return 
     */
    public List<Task> getTasks() {
        return tasks;
    }
    
    public void addTaskListener(TaskListener taskListener) {
        if (taskListeners == null) {
            taskListeners = new ArrayList<TaskListener>();
        }
        if (!taskListeners.contains(taskListener)) {
            taskListeners.add(taskListener);
        }
    }

    public boolean removeTaskListener(TaskListener taskListener) {
        return taskListeners != null && taskListeners.remove(taskListener);
    }

    public List<TaskListener> getTaskListeners() {
        return taskListeners;
    }
}
