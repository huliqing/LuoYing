/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ChatService;
import name.huliqing.core.network.Network;
import name.huliqing.core.mess.MessChatSell;
import name.huliqing.core.mess.MessChatSend;
import name.huliqing.core.mess.MessChatShop;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.chat.Chat;

/**
 *
 * @author huliqing
 */
public class ChatNetworkImpl implements ChatNetwork {
    private final static Network network = Network.getInstance();
    private ChatService chatService;
    
    @Override
    public void inject() {
        chatService = Factory.get(ChatService.class);
    }

    @Override
    public Chat loadChat(String chatId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Chat getChat(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void chatShop(Actor seller, Actor buyer, String itemId, int count, float discount) {
        // 客户端不处理
        if (network.isClient()) {
            return;
        }
        
        // 服务端逻辑
        if (network.hasConnections()) {
            MessChatShop mess = new MessChatShop();
            mess.setSeller(seller.getData().getUniqueId());
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setItemId(itemId);
            mess.setCount(count);
            mess.setDiscount(discount);
            network.broadcast(mess);
        }
        
        chatService.chatShop(seller, buyer, itemId, count, discount);
    }

    @Override
    public void chatSell(Actor seller, Actor buyer, String[] items, int[] counts, float discount) {
        if (network.isClient())
            return;
        
        if (network.hasConnections()) {
            MessChatSell mess = new MessChatSell();
            mess.setBuyer(buyer.getData().getUniqueId());
            mess.setCounts(counts);
            mess.setDiscount(discount);
            mess.setItems(items);
            mess.setSeller(seller.getData().getUniqueId());
            network.broadcast(mess);
        }
        
        chatService.chatSell(seller, buyer, items, counts, discount);
    }

    @Override
    public void chatSend(Actor sender, Actor receiver, String[] items, int[] counts) {
        if (network.isClient())
            return;
        
        if (network.hasConnections()) {
            MessChatSend mess = new MessChatSend();
            mess.setCounts(counts);
            mess.setItems(items);
            mess.setReceiver(receiver.getData().getUniqueId());
            mess.setSender(sender.getData().getUniqueId());
            network.broadcast(mess);
        }
        
        chatService.chatSend(sender, receiver, items, counts);
    }
    
}
