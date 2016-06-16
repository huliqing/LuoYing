/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 设置角色的队伍分组
 * @author huliqing
 */
@Serializable
public class MessActorTeam extends MessBase {
    
    private long actorId;
    // 说话的内容
    private int teamId = -1;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTeamId() {
        return teamId;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            actorService.setTeam(actor, teamId);
        }
    }
    
}
