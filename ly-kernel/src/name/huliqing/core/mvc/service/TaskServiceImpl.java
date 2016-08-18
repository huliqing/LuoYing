/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.List;
import name.huliqing.core.data.TaskData;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.TaskListener;
import name.huliqing.core.object.module.TaskModule;
import name.huliqing.core.object.task.Task;

/**
 *
 * @author huliqing
 */
public class TaskServiceImpl implements TaskService {

    @Override
    public void inject() {
    }

    @Override
    public Task loadTask(String taskId) {
        return Loader.loadTask(taskId);
    }

    @Override
    public Task loadTask(TaskData taskData) {
        return Loader.loadTask(taskData);
    }
    
    public <T extends Object> T  getModule(Class<T> moduleType) {
        return null;
    }
    
    @Override
    public void addTask(Actor actor, Task task) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            control.addTask(task);
        }
    }

    @Override
    public Task getTask(Actor actor, String taskId) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            return control.getTask(taskId);
        }
        return null;
    }

    @Override
    public List<Task> getTasks(Actor actor) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            return control.getTasks();
        }
        return null;
    }
    
    @Override
    public List<TaskData> getTaskDatas(Actor actor) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            return control.getTaskDatas();
        }
        return null;
    }
    
    @Override
    public boolean checkCompletion(Actor actor, Task task) {
        checkValidState(actor, task);
        return task.checkCompletion();
    }
    
    @Override
    public void completeTask(Actor actor, Task task) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            control.completeTask(task);
        }
    }
    
    @Override
    public void applyItem(Actor actor, Task task, String itemId, int amount) {
        checkValidState(actor, task);
        TaskData data = task.getData();
        data.applyTaskItem(itemId, amount);
    }

    @Override
    public int getItemTotal(Actor actor, Task task, String itemId) {
        checkValidState(actor, task);
        return task.getData().getTaskItemTotal(itemId);
    }

    @Override
    public void addTaskListener(Actor actor, TaskListener taskListener) {
        TaskModule control = actor.getModule(TaskModule.class);
        if (control != null) {
            control.addTaskListener(taskListener);
        }
    }

    @Override
    public boolean removeTaskListener(Actor actor, TaskListener taskListener) {
        TaskModule control = actor.getModule(TaskModule.class);
        return control != null && control.removeTaskListener(taskListener);
    }
    
    // 检查任务状态是否正常
    private void checkValidState(Actor actor, Task task) {
        if (task.getActor() != actor) 
            throw new IllegalStateException("Task state error by execute actor, task=" + task.getId()
                    + ", actor=" + actor.getData().getId());
    }
}
