/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.mess.MessBase;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色向另一个角色出售商品
 * @author huliqing
 */
@Serializable
public class MessChatSend extends MessBase {
    private final transient PlayService playService = Factory.get(PlayService.class);
    private final transient ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);
    private final transient ChatService chatService = Factory.get(ChatService.class);
    
    // 出售者
    private long sender;
    // 购买者
    private long receiver;
    
    // 发送的物品ID
    private long objectId;
    
    // 发送的物品数量
    private int amount;

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
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
        super.applyOnServer(gameServer, source);
        Entity sActor = playService.getEntity(sender);
        Entity rActor = playService.getEntity(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatNetwork.chatSend(sActor, rActor, objectId, amount);
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity sActor = playService.getEntity(sender);
        Entity rActor = playService.getEntity(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatService.chatSend(sActor, rActor, objectId, amount);
    }
    
}
