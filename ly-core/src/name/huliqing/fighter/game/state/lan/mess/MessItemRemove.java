/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.HandlerNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.game.ConnData;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 删除物品数量
 * @author huliqing
 */
@Serializable
public class MessItemRemove extends MessBase {
    
    // 删除物品的角色
    private long actorId;
    // 删除的物品ID
    private String itemId;
    // 删除的物品数量
    private int amount;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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
        ActorService actorService = Factory.get(ActorService.class);
        HandlerNetwork handlerNetwork = Factory.get(HandlerNetwork.class);
        
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Long clientActorId = cd != null ? cd.getActorId() : null;

        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            if (actor.getData().getUniqueId() != clientActorId) {
                // 角色不是客户端所控制的,则不应该删除，这时需要同步物品数量回客户端。
                // 因为物品的删除是客户端优先原则的。
                ProtoData data = actorService.getItem(actor, itemId);
                MessItemAdd messSyn = new MessItemAdd();
                messSyn.setActorId(actor.getData().getUniqueId());
                messSyn.setItemId(itemId);
                messSyn.setSyncTotal(data != null ? data.getTotal() : 0);
                gameServer.broadcast(messSyn);
            } else {
                handlerNetwork.removeObject(actor, itemId, amount);
            }
        }
    }
    
}
