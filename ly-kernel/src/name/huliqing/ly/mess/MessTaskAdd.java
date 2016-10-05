/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.layer.network.TaskNetwork;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.TaskService;
import name.huliqing.ly.network.GameServer;
import name.huliqing.ly.object.actor.Actor;

/**
 * 添加任务给指定的角色
 * @author huliqing
 */
@Serializable
public class MessTaskAdd extends MessBase {

    private long actorId;
    private TaskData taskData;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        TaskService taskService = Factory.get(TaskService.class);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            taskService.addTask(actor, taskService.loadTask(taskData));
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
        if (actor == null)
            return;
        TaskService taskService = Factory.get(TaskService.class);
        TaskNetwork taskNetwork = Factory.get(TaskNetwork.class);
        taskNetwork.addTask(actor, taskService.loadTask(taskData));
    }
    
    
}
