/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.TaskService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.mess.MessTaskAdd;
import name.huliqing.ly.mess.MessTaskApplyItem;
import name.huliqing.ly.mess.MessTaskComplete;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.task.Task;

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
