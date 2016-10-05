/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.StateService;
import name.huliqing.ly.object.actor.Actor;

/**
 * SC,服务端通知客户端移除一个状态
 * @author huliqing
 */
@Serializable
public class MessStateRemove extends MessBase {
    
    // 状态添加者的ID
    private long actorId;
    // 状态ID
    private String stateId;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        StateService stateService = Factory.get(StateService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            stateService.removeState(actor, stateId);
        }
    }
    
}
