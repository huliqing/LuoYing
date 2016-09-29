/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.TaskService;
import name.huliqing.core.network.Network;
import name.huliqing.core.mess.MessTaskAdd;
import name.huliqing.core.mess.MessTaskApplyItem;
import name.huliqing.core.mess.MessTaskComplete;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.task.Task;

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
    public void addTask(Actor actor, Task task) {
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
    public void completeTask(Actor actor, Task task) {
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
    public void applyItem(Actor actor, Task task, String itemId, int amount) {
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
