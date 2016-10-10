/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.mess.MessChatSell;
import name.huliqing.ly.mess.MessChatSend;
import name.huliqing.ly.mess.MessChatShop;
import name.huliqing.ly.object.entity.Entity;

/**
 * @author huliqing
 */
public class ChatNetworkImpl implements ChatNetwork {
    private final static Network NETWORK = Network.getInstance();
    private ChatService chatService;
    
    @Override
    public void inject() {
        chatService = Factory.get(ChatService.class);
    }

    @Override
    public void chatShop(Entity seller, Entity buyer, String itemId, int count, float discount) {
        // 客户端不处理
        if (NETWORK.isClient()) {
            return;
        }
        
        // 服务端逻辑
        if (NETWORK.hasConnections()) {
            MessChatShop mess = new MessChatShop();
            mess.setSeller(seller.getData().getUniqueId());
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setItemId(itemId);
            mess.setCount(count);
            mess.setDiscount(discount);
            NETWORK.broadcast(mess);
        }
        
        chatService.chatShop(seller, buyer, itemId, count, discount);
    }

    @Override
    public void chatSell(Entity seller, Entity buyer, String[] items, int[] counts, float discount) {
        if (NETWORK.isClient())
            return;
        
        if (NETWORK.hasConnections()) {
            MessChatSell mess = new MessChatSell();
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setCounts(counts);
            mess.setDiscount(discount);
            mess.setItems(items);
            mess.setSeller(seller.getData().getUniqueId());
            NETWORK.broadcast(mess);
        }
        
        chatService.chatSell(seller, buyer, items, counts, discount);
    }

    @Override
    public void chatSend(Entity sender, Entity receiver, String[] items, int[] counts) {
        if (NETWORK.isClient())
            return;
        
        if (NETWORK.hasConnections()) {
            MessChatSend mess = new MessChatSend();
            mess.setCounts(counts);
            mess.setItems(items);
            mess.setReceiver(receiver.getData().getUniqueId());
            mess.setSender(sender.getData().getUniqueId());
            NETWORK.broadcast(mess);
        }
        
        chatService.chatSend(sender, receiver, items, counts);
    }
    
}
