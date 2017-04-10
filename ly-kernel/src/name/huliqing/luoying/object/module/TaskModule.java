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
package name.huliqing.luoying.object.module;

import com.jme3.util.SafeArrayList;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.message.StateCode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.DataHandler;
import name.huliqing.luoying.object.task.Task;

/**
 * 任务管理
 * @author huliqing
 */
public class TaskModule extends AbstractModule implements DataHandler<TaskData> {

    private final SafeArrayList<Task> tasks = new SafeArrayList<Task>(Task.class);
    private List<TaskListener> taskListeners;

    @Override
    public void initialize() {
        super.initialize();
        List<TaskData> taskDatas = entity.getData().getObjectDatas(TaskData.class, new ArrayList<TaskData>());
        if (taskDatas != null && !taskDatas.isEmpty()) {
            for (TaskData td : taskDatas) {
                addTaskInner((Task) Loader.load(td));
            }
        }
    }

    @Override
    public void cleanup() {
        for (Task task : tasks.getArray()) {
            task.cleanup();
        }
        tasks.clear();
        super.cleanup();
    }
        
    /**
     * 获取指定ID的任务，如果不存在则返回null.
     * @param taskId
     * @return 
     */
    public Task getTask(String taskId) {
        for (Task t : tasks.getArray()) {
            if (t.getId().equals(taskId)) {
                return t;
            }
        }
        return null;
    }
    
    /**
     * 完成一个指定的任务
     * @param taskId 
     */
    public void completeTask(String taskId) {
        Task task = getTask(taskId);
        if (task == null) 
            return;
        
        // 执行“任务完成”如奖励经验、金钱、物品等。。。
        task.doCompletion();
        // 清理任务处理器，释放资源，当任务完成之后就不需要这些东西了，如各种侦听器等
        task.cleanup();
        
        // 侦听器
        if (taskListeners != null && !taskListeners.isEmpty()) {
            for (TaskListener tl : taskListeners) {
                tl.onTaskCompleted(entity, task);
            }
        }
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

    @Override
    public Class<TaskData> getHandleType() {
        return TaskData.class;
    }

    /**
     * 添加任务
     * @param task 
     * @return  
     */
    private void addTaskInner(Task task) {
        if (tasks.contains(task)) {
            return;
        }
        tasks.add(task);
        entity.getData().addObjectData(task.getData());
        task.setActor(entity);
        task.initialize();
    }
    
    @Override
    public boolean handleDataAdd(TaskData data, int amount) {
        Task task = getTask(data.getId());
        if (task != null) {
            addEntityDataAddMessage(StateCode.DATA_ADD_FAILURE_DATA_EXISTS, data, amount);
            return false;
        }
        addTaskInner((Task) Loader.load(data));
        addEntityDataAddMessage(StateCode.DATA_ADD, data, amount);
        return true;
    }

    @Override
    public boolean handleDataRemove(TaskData data, int amount) {
        Task task = getTask(data.getId());
        if (task == null) {
            addEntityDataRemoveMessage(StateCode.DATA_REMOVE_FAILURE_NOT_FOUND, data, amount);
            return false;
        }
        tasks.remove(task);
        entity.getData().removeObjectData(task.getData());
        task.cleanup();
        addEntityDataRemoveMessage(StateCode.DATA_REMOVE, data, amount);
        return true;
    }

    @Override
    public boolean handleDataUse(TaskData data) {
        return false;// ignore
    }
}
