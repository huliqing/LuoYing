/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.xml.ProtoData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.chat.Chat;

/**
 *
 * @author huliqing
 */
public class ChatServiceImpl implements ChatService {
    private PlayService playService;
    private ItemService itemService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        itemService = Factory.get(ItemService.class);
    }

    @Override
    public Chat loadChat(String chatId) {
        return Loader.loadChat(chatId);
    }

    @Override
    public Chat getChat(Actor actor) {
        Chat chat = actor.getChat();
        if (chat == null) {
            String chatId = actor.getData().getChat();
            if (chatId != null) {
                chat = loadChat(chatId);
                chat.setActor(actor);
                actor.setChat(chat);
            }
        }
        return chat;
    }
    
    @Override
    public void chatShop(Actor seller, Actor buyer, String itemId, int count, float discount) {
        ProtoData data = itemService.getItem(seller, itemId);
        if (data == null || data.getTotal() <= 0 || data.getTotal() < count) {
            // 库存不足，如果是当前场景“主角”则显示提示
            if (buyer == playService.getPlayer()) {
                playService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_PRODUCT_NOT_ENOUGH)
                        , MessageType.notice);
            }
            return;
        }
        
        // 非卖品
        if (!itemService.isSellable(data)) {
            return;
        }
        
        int needGold = (int) (data.getCost() * count * discount);
        ProtoData gold = itemService.getItem(buyer, IdConstants.ITEM_GOLD);
        if (gold == null || gold.getTotal() < needGold) {
            // 金币不足
            if (buyer == playService.getPlayer()) {
                playService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_GOLD_NOT_ENOUGH)
                        , MessageType.notice);
            }
            return;
        }
        
        itemService.addItem(buyer, itemId, count);
        itemService.removeItem(seller, itemId, count);
        
        itemService.addItem(seller, IdConstants.ITEM_GOLD, needGold);
        itemService.removeItem(buyer, IdConstants.ITEM_GOLD, needGold);
        
        SoundManager.getInstance().playSound(IdConstants.SOUND_COIN1, buyer.getLocation());
        
    }

    @Override
    public void chatSell(Actor seller, Actor buyer, String[] items, int[] counts, float discount) {
        if (sellInner(seller, buyer, items, counts, discount)) {
            // 有卖出过东西则播放声音
            SoundManager.getInstance().playSound(IdConstants.SOUND_COIN2, seller.getLocation());
        }
    }

    @Override
    public void chatSend(Actor sender, Actor receiver, String[] items, int[] counts) {
        sellInner(sender, receiver, items, counts, 0);
    }
    
    /**
     * 发送物品
     * @param seller
     * @param buyer
     * @param items
     * @param counts
     * @param discount 
     */
    private boolean sellInner(Actor seller, Actor buyer, String[] items, int[] counts, float discount) {
        String id;
        ProtoData data;
        int trueCount;
        float amount;
        boolean result = false; // 标记是否有卖出过任何一件物品
        for (int i = 0; i < items.length; i++) {
            id = items[i];
            data = itemService.getItem(seller, id);
            
            // 这里可能为null,因为“出售”或“发送”物品并不是与物品的使用动作同步的，有可能在点击“出售”或“发送”的时候
            // 包裹中的物品已经被使用掉。
            if (data == null) {
                continue;
            }
            
            // 非卖品
            if (!itemService.isSellable(data)) {
                continue;
            }
            
            // 注意：一件商品一件商品的卖,避免items中ID重复而出现重复卖的问题。
            if (data.getTotal() > 0) {
                // 如果角色身上指定ID物品的数量不够卖，则卖出尽可能多。否则按指定数量卖出。
                trueCount = data.getTotal() > counts[i] ? counts[i] : data.getTotal();
                amount = data.getCost() * trueCount * discount;
                
                itemService.addItem(buyer, id, trueCount);
                itemService.removeItem(seller, id, trueCount);
                
                if (amount > 0) {
                    itemService.removeItem(buyer, IdConstants.ITEM_GOLD, (int) amount);
                    itemService.addItem(seller, IdConstants.ITEM_GOLD, (int) amount);
                }
                result = true;
            }
        }
        return result;
    }
}
