/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ProtoService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 给角色添加物体
 * @author huliqing
 */
@Serializable
public class MessProtoAdd extends MessBase {
    private long actorId;
    // 获得的物品ID
    private String objectId;
    // 获得的物品数量,如果count为0，则只同步物品数量
    private int addCount;
    // 同步物品总数
    private int syncTotal;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String itemId) {
        this.objectId = itemId;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int count) {
        this.addCount = count;
    }

    public int getSyncTotal() {
        return syncTotal;
    }

    public void setSyncTotal(int syncTotal) {
        this.syncTotal = syncTotal;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ProtoService protoService = Factory.get(ProtoService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return;
        }
        
        if (addCount > 0) {
            ProtoData data = protoService.createData(objectId);
            protoService.addData(actor, data, addCount);
        }
        protoService.syncDataTotal(actor, objectId, syncTotal);
    }
}
