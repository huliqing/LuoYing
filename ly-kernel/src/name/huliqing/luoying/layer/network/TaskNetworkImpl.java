/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    // remove20161123
//    @Override
//    public void applyItem(Entity actor, String taskId, String itemId, int amount) {
//        if (NETWORK.isClient()) {
//            // ignore
//        } else {
//            MessTaskApplyItem mess = new MessTaskApplyItem();
//            mess.setActorId(actor.getData().getUniqueId());
//            mess.setAmount(amount);
//            mess.setItemId(itemId);
//            mess.setTaskId(taskId);
//            NETWORK.broadcast(mess);
//            taskService.applyItem(actor, taskId, itemId, amount);
//        }
//    }
    
}
