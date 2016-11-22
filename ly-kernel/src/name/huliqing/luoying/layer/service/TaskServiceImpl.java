/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TaskListener;
import name.huliqing.luoying.object.module.TaskModule;
import name.huliqing.luoying.object.task.Task;

/**
 *
 * @author huliqing
 */
public class TaskServiceImpl implements TaskService {

    @Override
    public void inject() {
    }

//    @Override
//    public Task getTask(Entity actor, String taskId) {
//        TaskModule control = actor.getModuleManager().getModule(TaskModule.class);
//        if (control != null) {
//            return control.getTask(taskId);
//        }
//        return null;
//    }
//
//    @Override
//    public List<Task> getTasks(Entity actor) {
//        TaskModule module = actor.getModuleManager().getModule(TaskModule.class);
//        if (module != null) {
//            return module.getTasks();
//        }
//        return null;
//    }
    
    @Override
    public boolean checkCompletion(Entity actor, String taskId) {
        TaskModule module = actor.getModuleManager().getModule(TaskModule.class);
        if (module != null) {
            Task task = module.getTask(taskId);
            return task != null && task.checkCompletion();
        }
        return false;
    }
    
    @Override
    public void completeTask(Entity actor, String taskId) {
        TaskModule control = actor.getModuleManager().getModule(TaskModule.class);
        if (control != null) {
            control.completeTask(taskId);
        }
    }
    
//    @Override
//    public void applyItem(Entity actor, String taskId, String itemId, int amount) {
//        TaskData data = actor.getData().getObjectData(taskId);
//        if (data != null) {
//            data.applyTaskItem(itemId, amount);
//        }
//    }
//
//    @Override
//    public int getItemTotal(Entity actor, String taskId, String itemId) {
//        TaskData data = actor.getData().getObjectData(taskId);
//        if (data != null) {
//            return data.getTaskItemTotal(itemId);
//        }
//        return 0;
//    }

    @Override
    public void addTaskListener(Entity actor, TaskListener taskListener) {
        TaskModule control = actor.getModuleManager().getModule(TaskModule.class);
        if (control != null) {
            control.addTaskListener(taskListener);
        }
    }

    @Override
    public boolean removeTaskListener(Entity actor, TaskListener taskListener) {
        TaskModule control = actor.getModuleManager().getModule(TaskModule.class);
        return control != null && control.removeTaskListener(taskListener);
    }
    
//    // 检查任务状态是否正常
//    private void checkValidState(Entity actor, Task task) {
//        if (task.getActor() != actor) 
//            throw new IllegalStateException("Task state error by execute actor, task=" + task.getId()
//                    + ", actor=" + actor.getData().getId());
//    }
}
