/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.TaskListener;
import name.huliqing.ly.object.module.TaskModule;
import name.huliqing.ly.object.task.Task;

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
        return Loader.load(taskId);
    }

    @Override
    public Task loadTask(TaskData taskData) {
        return Loader.load(taskData);
    }
    
    @Override
    public void addTask(Entity actor, Task task) {
        TaskModule control = actor.getEntityModule().getModule(TaskModule.class);
        if (control != null) {
            control.addTask(task);
        }
    }

    @Override
    public Task getTask(Entity actor, String taskId) {
        TaskModule control = actor.getEntityModule().getModule(TaskModule.class);
        if (control != null) {
            return control.getTask(taskId);
        }
        return null;
    }

    @Override
    public List<Task> getTasks(Entity actor) {
        TaskModule module = actor.getEntityModule().getModule(TaskModule.class);
        if (module != null) {
            return module.getTasks();
        }
        return null;
    }
   
    // remove
//    @Override
//    public List<TaskData> getTaskDatas(Entity actor) {
////        TaskModule module = actor.getEntityModule().getModule(TaskModule.class);
////        if (module != null) {
////            return module.getTaskDatas();
////        }
////        return null;
//        
//        return actor.getData().getObjectDatas(TaskData.class, null);
//    }
    
    @Override
    public boolean checkCompletion(Entity actor, Task task) {
        checkValidState(actor, task);
        return task.checkCompletion();
    }
    
    @Override
    public void completeTask(Entity actor, Task task) {
        TaskModule control = actor.getEntityModule().getModule(TaskModule.class);
        if (control != null) {
            control.completeTask(task);
        }
    }
    
    @Override
    public void applyItem(Entity actor, Task task, String itemId, int amount) {
        checkValidState(actor, task);
        TaskData data = task.getData();
        data.applyTaskItem(itemId, amount);
    }

    @Override
    public int getItemTotal(Entity actor, Task task, String itemId) {
        checkValidState(actor, task);
        return task.getData().getTaskItemTotal(itemId);
    }

    @Override
    public void addTaskListener(Entity actor, TaskListener taskListener) {
        TaskModule control = actor.getEntityModule().getModule(TaskModule.class);
        if (control != null) {
            control.addTaskListener(taskListener);
        }
    }

    @Override
    public boolean removeTaskListener(Entity actor, TaskListener taskListener) {
        TaskModule control = actor.getEntityModule().getModule(TaskModule.class);
        return control != null && control.removeTaskListener(taskListener);
    }
    
    // 检查任务状态是否正常
    private void checkValidState(Entity actor, Task task) {
        if (task.getActor() != actor) 
            throw new IllegalStateException("Task state error by execute actor, task=" + task.getId()
                    + ", actor=" + actor.getData().getId());
    }
}
