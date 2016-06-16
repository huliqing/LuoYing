/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.TalentNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.TalentService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 给目标角色所指定的天赋增加点数
 * @author huliqing
 */
@Serializable
public class MessTalentAddPoint extends MessBase {
    
    // 目标角色
    private long actorId;
    // 天赋ID
    private String talentId;
    // 要添加多少天赋点数？
    private int points;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getTalentId() {
        return talentId;
    }

    public void setTalentId(String talentId) {
        this.talentId = talentId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        TalentNetwork talentNetwork = Factory.get(TalentNetwork.class);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            talentNetwork.addTalentPoints(actor, talentId, points);
        }
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        TalentService talentService = Factory.get(TalentService.class);
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            talentService.addTalentPoints(actor, talentId, points);
        }
    }
    
}
