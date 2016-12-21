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
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.define.TradeInfo;
import name.huliqing.luoying.data.define.TradeObject;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.ly.object.chat.Chat;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.object.module.ChatModule;
import name.huliqing.luoying.object.sound.SoundManager;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.manager.ResourceManager;

/**
 *
 * @author huliqing
 */
public class ChatServiceImpl implements ChatService {
    private GameService gameService;
    private EntityNetwork entityNetwork;
    
    @Override
    public void inject() {
        gameService = Factory.get(GameService.class);
        entityNetwork = Factory.get(EntityNetwork.class);
    }
    
    @Override
    public Chat getChat(Entity actor) {
        ChatModule module = actor.getModuleManager().getModule(ChatModule.class);
        if (module != null) {
            return module.getChat();
        }
        return null;
    }
    
    @Override
    public void chatShop(Entity seller, Entity buyer, long objectId, int amount, float discount) {
        ObjectData objectData = seller.getData().getObjectDataByUniqueId(objectId);
        if (!(objectData instanceof TradeObject)) {
            return;
        }
        TradeObject data = (TradeObject) objectData;
        if (data.getTotal() <= 0 || data.getTotal() < amount) {
            // 库存不足，如果是当前场景“主角”则显示提示
            if (buyer == gameService.getPlayer()) {
                gameService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_PRODUCT_NOT_ENOUGH)
                        , MessageType.notice);
            }
            return;
        }
        
        List<TradeInfo> tradeInfos = data.getTradeInfos();
        
        // 注：
        // 1.如果拆扣为0则表示物品白卖。
        // 2.如果物品不存在交易信息，也表示白卖，直接交换,
        // 3.这里用Network是因为这样可以确保服务端和客户端物品的唯一id一致。
        if (discount <= 0 || tradeInfos == null || tradeInfos.isEmpty()) {
            entityNetwork.addObjectData(buyer, objectData.clone(), amount); // 注：这里用的是clone
            entityNetwork.removeObjectData(seller, objectId, amount);
            return;
        }
        
        // 检查交易信息，检查买家是否有足够的物品来交换指定的货物，即比如需要多少金币.
        // 只要有一件不够，则不允许进行交易。
        for (TradeInfo ti : tradeInfos) {

            // 判断买家的交易物（如金币）是否足够。
            ObjectData needObject = buyer.getData().getObjectData(ti.getObjectId());
            if (needObject instanceof TradeObject) {
                TradeObject needTradeObject = (TradeObject) needObject;
                int needAmount = (int) (ti.getCount() * amount * discount);
                if (needTradeObject.getTotal() >= needAmount) {
                    continue;
                }
            }
            
            // 买家的交易物品不存在或不足，给一些提示,然后退出，不再执行其它。
            if (buyer == gameService.getPlayer()) {
                gameService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_GOLD_NOT_ENOUGH)
                    , MessageType.notice);
            }
            if (seller == gameService.getPlayer()) {
                gameService.addMessage(ResourceManager.get(
                        ResConstants.CHAT_SHOP_WARN_BUYER_GOLD_NOT_ENOUGH
                        , new Object[] {ResourceManager.getObjectName(buyer.getData())})
                        , MessageType.notice);
            }
            return;
        }
        
        entityNetwork.addObjectData(buyer, objectData.clone(), amount); // 注：这里用的是clone
        entityNetwork.removeObjectData(seller, objectId, amount);
        for (TradeInfo ti : tradeInfos) {
            int removeAmount = (int)(ti.getCount() * amount * discount);
            ObjectData od = buyer.getData().getObjectData(ti.getObjectId());
            entityNetwork.removeObjectData(buyer, od.getUniqueId(), removeAmount);
            entityNetwork.addObjectData(seller, od.clone(), removeAmount);
        }
        SoundManager.getInstance().playSound(IdConstants.SOUND_COIN1, buyer.getSpatial().getWorldTranslation());
    }
    
    @Override
    public void chatSend(Entity sender, Entity receiver, long objectId, int amount) {
        chatShop(sender, receiver, objectId, amount, 0);
    }
    
}
