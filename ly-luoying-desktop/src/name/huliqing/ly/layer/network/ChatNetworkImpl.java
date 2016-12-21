/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
