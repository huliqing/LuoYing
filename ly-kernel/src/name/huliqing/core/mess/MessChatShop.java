/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.ChatNetwork;
import name.huliqing.core.mvc.service.ChatService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;

/**
 * 角色向另一个角色购买商品
 * @author huliqing
 */
@Serializable
public class MessChatShop extends MessBase {
    private final transient PlayService playService = Factory.get(PlayService.class);
    private final transient ChatNetwork chatNetwork = Factory.get(ChatNetwork.class);
    private final transient ChatService chatService = Factory.get(ChatService.class);
        
    private long seller;
    private long buyer;
    private String itemId;
    private int count;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
        chatNetwork.chatShop(sellerActor, buyerActor, itemId, count, discount);
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Actor sellerActor = playService.findActor(seller);
        Actor buyerActor = playService.findActor(buyer);
        if (sellerActor == null || buyerActor == null) {
            return;
        }
        chatService.chatShop(sellerActor, buyerActor, itemId, count, discount);
    }
    
}
