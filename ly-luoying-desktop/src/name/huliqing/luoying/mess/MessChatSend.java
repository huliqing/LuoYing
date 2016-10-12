/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.luoying.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.mess.MessBase;
import name.huliqing.ly.network.GameServer;
import name.huliqing.ly.object.entity.Entity;

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
    
    // 出售的物品ID，与counts一一对应
    private String[] items;
    // 出售的物品数量,与items一一对应
    private int[] counts;

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

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public int[] getCounts() {
        return counts;
    }

    public void setCounts(int[] counts) {
        this.counts = counts;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        Entity sActor = playService.getEntity(sender);
        Entity rActor = playService.getEntity(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatNetwork.chatSend(sActor, rActor, items, counts);
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity sActor = playService.getEntity(sender);
        Entity rActor = playService.getEntity(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatService.chatSend(sActor, rActor, items, counts);
    }
    
}
