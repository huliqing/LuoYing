/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.network.GameServer;
import name.huliqing.ly.object.entity.Entity;

/**
 * 设置目标
 * @author huliqing
 */
@Serializable
public class MessActorSetTarget extends MessBase {
    
    private long actorId;
    // -1表示清除目标
    private long targetId;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        PlayService playService = Factory.get(PlayService.class);
        ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
        Entity actor = playService.getEntity(actorId);
        Entity target = playService.getEntity(targetId);
        if (actor != null && target != null) {
            actorNetwork.setTarget(actor, target);
        }
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        
        Entity actor = playService.getEntity(actorId);
        Entity actorTarget = playService.getEntity(targetId);
        if (actor != null) {
            actorService.setTarget(actor, actorTarget);
        } 
    }
    
}
