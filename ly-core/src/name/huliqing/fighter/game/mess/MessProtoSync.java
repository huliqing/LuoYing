/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ProtoService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 同步角色物品数量
 * @author huliqing
 */
@Serializable
public class MessProtoSync extends MessBase {
    private long actorId;
    // 获得的物品ID
    private String objectId;
    // 同步物品总数
    private int total;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ProtoService protoService = Factory.get(ProtoService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return;
        }
        protoService.syncDataTotal(actor, objectId, total);
    }
}
