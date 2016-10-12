/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.ly.Factory;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.object.Loader;
import name.huliqing.luoying.object.chat.Chat;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.luoying.object.module.ChatModule;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.ly.layer.service.ObjectService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class ChatServiceImpl implements ChatService {
    private ObjectService protoService;
    private PlayService playService;
    
    @Override
    public void inject() {
        protoService = Factory.get(ObjectService.class);
        playService = Factory.get(PlayService.class);
    }
    
    @Override
    public Chat loadChat(String chatId) {
        return Loader.loadChat(chatId);
    }
    
    @Override
    public Chat getChat(Entity actor) {
        ChatModule module = actor.getEntityModule().getModule(ChatModule.class);
        if (module != null) {
            return module.getChat();
        }
        return null;
    }
    
    @Override
    public void chatShop(Entity seller, Entity buyer, String itemId, int count, float discount) {
//        ObjectData data = protoService.getData(seller, itemId);
//        if (data == null || data.getTotal() <= 0 || data.getTotal() < count) {
//            // 库存不足，如果是当前场景“主角”则显示提示
//            if (buyer == playService.getPlayer()) {
//                playService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_PRODUCT_NOT_ENOUGH)
//                        , MessageType.notice);
//            }
//            return;
//        }
//        
//        // 非卖品
//        if (!protoService.isSellable(data)) {
//            return;
//        }
//        
//        int needGold = (int) (protoService.getCost(data) * count * discount);
//        ObjectData gold = protoService.getData(buyer, IdConstants.ITEM_GOLD);
//        if (gold == null || gold.getTotal() < needGold) {
//            // 金币不足
//            if (buyer == playService.getPlayer()) {
//                playService.addMessage(ResourceManager.get(ResConstants.CHAT_SHOP_WARN_GOLD_NOT_ENOUGH)
//                        , MessageType.notice);
//            }
//            return;
//        }
//        
//        protoService.addData(buyer, itemId, count);
//        protoService.removeData(seller, itemId, count);
//        
//        protoService.addData(seller, IdConstants.ITEM_GOLD, needGold);
//        protoService.removeData(buyer, IdConstants.ITEM_GOLD, needGold);
//        
//        SoundManager.getInstance().playSound(IdConstants.SOUND_COIN1, buyer.getSpatial().getWorldTranslation());
        
        throw new UnsupportedOperationException();
    }

    @Override
    public void chatSell(Entity seller, Entity buyer, String[] items, int[] counts, float discount) {
        if (sellInner(seller, buyer, items, counts, discount)) {
            // 有卖出过东西则播放声音
            SoundManager.getInstance().playSound(IdConstants.SOUND_COIN2, seller.getSpatial().getWorldTranslation());
        }
    }

    @Override
    public void chatSend(Entity sender, Entity receiver, String[] items, int[] counts) {
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
    private boolean sellInner(Entity seller, Entity buyer, String[] items, int[] counts, float discount) {
        String id;
        ObjectData data;
        int trueCount;
        float amount; 
        boolean result = false; // 标记是否有卖出过任何一件物品
        for (int i = 0; i < items.length; i++) {
            id = items[i];
            data = protoService.getData(seller, id);
            
            // 这里可能为null,因为“出售”或“发送”物品并不是与物品的使用动作同步的，有可能在点击“出售”或“发送”的时候
            // 包裹中的物品已经被使用掉。
            if (data == null) {
                continue;
            }
            
            // 非卖品
            if (!protoService.isSellable(data)) {
                continue;
            }
            
            // 注意：一件商品一件商品的卖,避免items中ID重复而出现重复卖的问题。
            if (data.getTotal() > 0) {
                // 如果角色身上指定ID物品的数量不够卖，则卖出尽可能多。否则按指定数量卖出。
                trueCount = data.getTotal() > counts[i] ? counts[i] : data.getTotal();
                amount = protoService.getCost(data) * trueCount * discount;
                
                protoService.addData(buyer, id, trueCount);
                protoService.removeData(seller, id, trueCount);
                
                if (amount > 0) {
                    protoService.removeData(buyer, IdConstants.ITEM_GOLD, (int) amount);
                    protoService.addData(seller, IdConstants.ITEM_GOLD, (int) amount);
                }
                result = true;
            }
        }
        return result;
    }
}
