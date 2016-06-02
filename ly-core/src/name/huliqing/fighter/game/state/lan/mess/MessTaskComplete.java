/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.TaskNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.TaskService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.task.Task;

/**
 * 完成指定的任务
 * @author huliqing
 */
@Serializable
public class MessTaskComplete extends MessBase {
    
    private long actorId;
    private String taskId;

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

    @Override
    public void applyOnClient() {
        super.applyOnClient(); 
        TaskService taskService = Factory.get(TaskService.class);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null)
            return;
        
        Task task = taskService.getTask(actor, taskId);
        if (task == null)
            return;
        
        taskService.completeTask(actor, task);
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null)
            return;
        
        Task task = Factory.get(TaskService.class).getTask(actor, taskId);
        if (task == null)
            return;
        
        Factory.get(TaskNetwork.class).completeTask(actor, task);
    }
    
}
