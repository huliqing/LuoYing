/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.TaskService;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.mess.MessTaskAdd;
import name.huliqing.luoying.mess.MessTaskApplyItem;
import name.huliqing.luoying.mess.MessTaskComplete;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.task.Task;

/**
 * 任务
 * @author huliqing
 */
public class TaskNetworkImpl implements TaskNetwork {
    private final Network NETWORK = Network.getInstance();
    private TaskService taskService;
    
    @Override
    public void inject() {
        taskService = Factory.get(TaskService.class);
    }

    @Override
    public void addTask(Entity actor, Task task) {
        MessTaskAdd mess = new MessTaskAdd();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTaskData(task.getData());
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            taskService.addTask(actor, task);
        }
    }

    @Override
    public void completeTask(Entity actor, Task task) {
        MessTaskComplete mess = new MessTaskComplete();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setTaskId(task.getId());
        
        // 客户端向服务端提交“完成任务”的请求
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            taskService.completeTask(actor, task);
        }
    }

    @Override
    public void applyItem(Entity actor, Task task, String itemId, int amount) {
        if (NETWORK.isClient()) {
            // ignore
        } else {
            MessTaskApplyItem mess = new MessTaskApplyItem();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setAmount(amount);
            mess.setItemId(itemId);
            mess.setTaskId(task.getId());
            NETWORK.broadcast(mess);
            taskService.applyItem(actor, task, itemId, amount);
        }
    }
    
}
