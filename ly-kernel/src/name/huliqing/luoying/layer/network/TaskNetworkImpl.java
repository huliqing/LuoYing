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
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.TaskService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.mess.TaskCompleteMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 任务
 * @author huliqing
 */
public class TaskNetworkImpl implements TaskNetwork {
    private final Network network = Network.getInstance();
    private TaskService taskService;
    
    @Override
    public void inject() {
        taskService = Factory.get(TaskService.class);
    }

    @Override
    public void completeTask(Entity actor, String taskId) {
        TaskCompleteMess mess = new TaskCompleteMess();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTaskId(taskId);
        
        // 客户端向服务端提交“完成任务”的请求
        if (network.isClient()) {
            network.sendToServer(mess);
        } else {
            network.broadcast(mess);
            taskService.completeTask(actor, taskId);
        }
    }

}
