/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.luoying.Factory;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.luoying.network.Network;
import name.huliqing.ly.mess.ChatSendMess;
import name.huliqing.ly.mess.ChatShopMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public class ChatNetworkImpl implements ChatNetwork {
    private final Network network = Network.getInstance();
    private ChatService chatService;
    
    @Override
    public void inject() {
        chatService = Factory.get(ChatService.class);
    }

    @Override
    public void chatShop(Entity seller, Entity buyer, long objectId, int count, float discount) {
        ChatShopMess mess = new ChatShopMess();
        mess.setSeller(seller.getData().getUniqueId());
        mess.setBuyer(buyer.getData().getUniqueId());
        mess.setObjectId(objectId);
        mess.setCount(count);
        mess.setDiscount(discount);
        
        // on client
        if (network.isClient()) {
            network.sendToServer(mess);
        } else {
            network.broadcast(mess);
            chatService.chatShop(seller, buyer, objectId, count, discount);
        }
    }

    // remove20161122
//    @Override
//    public void chatSell(Entity seller, Entity buyer, String[] items, int[] counts, float discount) {
//        MessChatSell mess = new MessChatSell();
//        mess.setBuyer(buyer.getData().getUniqueId());
//        mess.setCounts(counts);
//        mess.setDiscount(discount);
//        mess.setItems(items);
//        mess.setSeller(seller.getData().getUniqueId());
//        
//        // On client
//        if (network.isClient()) {
//            network.sendToServer(mess);
//        } else {
//            // On Server
//            network.broadcast(mess);
//            chatService.chatSell(seller, buyer, items, counts, discount);
//        }
//    }

    @Override
    public void chatSend(Entity sender, Entity receiver, long objectId, int amount) {
        ChatSendMess mess = new ChatSendMess();
        mess.setAmount(amount);
        mess.setObjectId(objectId);
        mess.setReceiver(receiver.getEntityId());
        mess.setSender(sender.getEntityId());
        
        // on client
        if (network.isClient()) {
            network.sendToServer(mess);
        } else {
            network.broadcast(mess);
            chatService.chatSend(sender, receiver, objectId, amount);
        }

    }

}
