/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.ProtoNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.ProtoService;
import name.huliqing.fighter.game.state.ConnData;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 删除物品数量
 * @author huliqing
 */
@Serializable
public class MessProtoRemove extends MessBase {
    
    // 删除物品的角色
    private long actorId;
    // 删除的物品ID
    private String objectId;
    // 删除的物品数量
    private int amount;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        PlayService playService = Factory.get(PlayService.class);
        ProtoService protoService = Factory.get(ProtoService.class);
        ProtoNetwork protoNetwork = Factory.get(ProtoNetwork.class);
        
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        long clientActorId = cd.getActorId();

        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            if (actor.getData().getUniqueId() != clientActorId) {
                // 角色不是客户端所控制的,则不应该删除，这时需要同步物品数量回客户端。
                // 因为物品的删除是客户端优先原则的。
                ProtoData data = protoService.getData(actor, objectId);
                MessProtoSync messSyn = new MessProtoSync();
                messSyn.setActorId(actor.getData().getUniqueId());
                messSyn.setObjectId(objectId);
                messSyn.setTotal(data != null ? data.getTotal() : 0);
                gameServer.broadcast(messSyn);
            } else {
                ProtoData data = protoService.getData(actor, objectId);
                protoNetwork.removeData(actor, data, amount);
            }
        }
    }
    
}
