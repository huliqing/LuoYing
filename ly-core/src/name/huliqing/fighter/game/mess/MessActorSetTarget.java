/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

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
        Actor actor = playService.findActor(actorId);
        Actor target = playService.findActor(targetId);
        if (actor != null && target != null) {
            actorNetwork.setTarget(actor, target);
        }
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        
        Actor actor = playService.findActor(actorId);
        Actor actorTarget = playService.findActor(targetId);
        if (actor != null) {
            actorService.setTarget(actor, actorTarget);
        } 
    }
    
}
