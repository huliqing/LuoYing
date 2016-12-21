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

import name.huliqing.luoying.layer.network.TaskNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TaskListener;

/**
 *
 * @author huliqing
 */
public interface TaskService extends TaskNetwork {
    
    /**
     * 检查指定的任务是否完成。
     * @param actor
     * @param taskId 任务
     * @return 
     */
    boolean checkCompletion(Entity actor, String taskId);
    
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
