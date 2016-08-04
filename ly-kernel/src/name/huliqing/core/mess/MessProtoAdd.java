/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.object.actor.Actor;

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
            ObjectData data = protoService.createData(objectId);
            protoService.addData(actor, data, addCount);
        }
        protoService.syncDataTotal(actor, objectId, syncTotal);
    }
}
