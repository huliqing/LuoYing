/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

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
    
}
