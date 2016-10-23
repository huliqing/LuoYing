/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.luoying.network.Network;
import name.huliqing.ly.mess.MessChatSell;
import name.huliqing.ly.mess.MessChatSend;
import name.huliqing.ly.mess.MessChatShop;
import name.huliqing.luoying.object.entity.Entity;

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
        MessChatShop mess = new MessChatShop();
        mess.setSeller(seller.getData().getUniqueId());
        mess.setBuyer(buyer.getData().getUniqueId());
        mess.setItemId(itemId);
        mess.setCount(count);
        mess.setDiscount(discount);
        
        // on client
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            chatService.chatShop(seller, buyer, itemId, count, discount);
        }
        
    }

    @Override
    public void chatSell(Entity seller, Entity buyer, String[] items, int[] counts, float discount) {
        MessChatSell mess = new MessChatSell();
        mess.setBuyer(buyer.getData().getUniqueId());
        mess.setCounts(counts);
        mess.setDiscount(discount);
        mess.setItems(items);
        mess.setSeller(seller.getData().getUniqueId());
        
        // On client
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            // On Server
            NETWORK.broadcast(mess);
            chatService.chatSell(seller, buyer, items, counts, discount);
        }
    }

    @Override
    public void chatSend(Entity sender, Entity receiver, String[] items, int[] counts) {
        MessChatSend mess = new MessChatSend();
        mess.setCounts(counts);
        mess.setItems(items);
        mess.setReceiver(receiver.getData().getUniqueId());
        mess.setSender(sender.getData().getUniqueId());
        
        // on client
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            chatService.chatSend(sender, receiver, items, counts);
        }

    }

}
