/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
