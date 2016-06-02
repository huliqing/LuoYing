/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 服务端向客户端发送命令，杀死一个角色
 * @author huliqing
 */
@Serializable
public class MessActorKill extends MessBase {
    
    private long killActorId = -1;

    public long getKillActorId() {
        return killActorId;
    }

    public void setKillActorId(long killActorId) {
        this.killActorId = killActorId;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Actor actor = playService.findActor(killActorId);
        if (actor != null) {
            actorService.kill(actor);
        }
    }
    
}
