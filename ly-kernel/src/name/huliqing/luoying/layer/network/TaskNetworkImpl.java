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
 *
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
        if (NETWORK.isClient())
            return;
        
        if (NETWORK.hasConnections()) {
            MessTaskAdd mess = new MessTaskAdd();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskData(task.getData());
            NETWORK.broadcast(mess);
        }
        taskService.addTask(actor, task);
    }

    @Override
    public void completeTask(Entity actor, Task task) {
        if (NETWORK.isClient())
            return;
        
        if (NETWORK.hasConnections()) {
            MessTaskComplete mess = new MessTaskComplete();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTaskId(task.getId());
            NETWORK.broadcast(mess);
        }
        taskService.completeTask(actor, task);
    }

    @Override
    public void applyItem(Entity actor, Task task, String itemId, int amount) {
        if (NETWORK.isClient())
            return;
        
        if (NETWORK.hasConnections()) {
            MessTaskApplyItem mess = new MessTaskApplyItem();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setAmount(amount);
            mess.setItemId(itemId);
            mess.setTaskId(task.getId());
            NETWORK.broadcast(mess);
        }
        taskService.applyItem(actor, task, itemId, amount);
    }
    
}
