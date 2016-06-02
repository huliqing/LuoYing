/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.ChatNetwork;
import name.huliqing.fighter.game.service.ChatService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

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
        Actor sActor = playService.findActor(sender);
        Actor rActor = playService.findActor(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatNetwork.chatSend(sActor, rActor, items, counts);
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Actor sActor = playService.findActor(sender);
        Actor rActor = playService.findActor(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatService.chatSend(sActor, rActor, items, counts);
    }
    
}
