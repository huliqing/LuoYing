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
import name.huliqing.ly.object.actor.Actor;

/**
 * 设置角色的队伍分组
 * @author huliqing
 */
@Serializable
public class MessActorFollow extends MessBase {
    
    private long actorId;
    // 被跟随者的ID
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
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            ActorService actorService = Factory.get(ActorService.class);
            actorService.setFollow(actor, targetId);
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
            actorNetwork.setFollow(actor, targetId);
        }
    }

    
}
