/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.TaskService;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.task.Task;

/**
 * 增加或减少任务物品的数量
 * @author huliqing
 */
@Serializable
public class MessTaskApplyItem extends MessBase {
    private transient final TaskService taskService = Factory.get(TaskService.class);
    private transient final PlayService playService = Factory.get(PlayService.class);

    private long actorId;
    private String taskId;
    // 任务物品的ID
    private String itemId;
    // 增加或减少的数量，可正可负
    private int amount;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
 
    @Override
    public void applyOnClient() {
        super.applyOnClient(); 
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            Task task = taskService.getTask(actor, taskId);
            if (task != null) {
                taskService.applyItem(actor, task, itemId, amount);
            }
        }
    }

    
}
