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
package name.huliqing.ly.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 角色向另一个角色出售商品
 * @author huliqing
 */
@Serializable
public class ChatSendMess extends GameMess {
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
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(null);
        Entity sActor = playService.getEntity(sender);
        Entity rActor = playService.getEntity(receiver);
        if (sActor == null || rActor == null) {
            return;
        }
        chatService.chatSend(sActor, rActor, objectId, amount);
    }
    
}
