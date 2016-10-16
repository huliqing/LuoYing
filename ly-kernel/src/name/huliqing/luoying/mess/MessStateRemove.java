/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.StateService;
import name.huliqing.luoying.object.entity.Entity;

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
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            stateService.removeState(actor, stateId);
        }
    }
    
}
