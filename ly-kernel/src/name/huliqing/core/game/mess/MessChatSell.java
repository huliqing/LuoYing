/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.game.network.ChatNetwork;
import name.huliqing.core.game.service.ChatService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;

/**
 * 角色向另一个角色出售商品
 * @author huliqing
 */
@Serializable
public class MessChatSell extends MessBase {
    private final transient PlayService playService = Factory.get(PlayService.class);
    private final transient ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);
    private final transient ChatService chatService = Factory.get(ChatService.class);
    
    // 出售者
    private long seller;
    // 购买者
    private long buyer;
    
    // 出售的物品ID，与counts一一对应
    private String[] items;
    // 出售的物品数量,与items一一对应
    private int[] counts;
    // 出售的物品价钱折扣
    private float discount;

    public long getSeller() {
        return seller;
    }

    public void setSeller(long seller) {
        this.seller = seller;
    }

    public long getBuyer() {
        return buyer;
    }

    public void setBuyer(long buyer) {
        this.buyer = buyer;
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

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        Actor sellerActor = playService.findActor(seller);
        Actor buyerActor = playService.findActor(buyer);
        if (sellerActor == null || buyerActor == null) {
            return;
        }
        chatNetwork.chatSell(sellerActor, buyerActor, items, counts, discount);
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Actor sellerActor = playService.findActor(seller);
        Actor buyerActor = playService.findActor(buyer);
        if (sellerActor == null || buyerActor == null) {
            return;
        }
        chatService.chatSell(sellerActor, buyerActor, items, counts, discount);
    }
    
}
