/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Factory;
import name.huliqing.ly.mess.MessActorSetTarget;
import name.huliqing.ly.mess.MessItemAdd;
import name.huliqing.ly.mess.MessItemRemove;
import name.huliqing.ly.mess.MessItemUse;
import name.huliqing.ly.layer.service.ItemService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.network.Network;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.item.Item;

/**
 * @author huliqing
 */
public class ItemNetworkImpl implements ItemNetwork {    
    private final static Network NETWORK = Network.getInstance();
    private ItemService itemService;
    private PlayService playService;
    private ActorNetwork actorNetwork;
    
    @Override
    public void inject() {
        itemService = Factory.get(ItemService.class);
        playService = Factory.get(PlayService.class);
        actorNetwork = Factory.get(ActorNetwork.class);
    }
    
    @Override
    public void addItem(Actor actor, String itemId, int count) {
        if (NETWORK.isClient()) {
            // 客户端不允许直接给角色添加物品
            // ignore
        } else {
            MessItemAdd mess = new MessItemAdd();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setItemId(itemId);
            mess.setCount(count);
            
            NETWORK.broadcast(mess);
            itemService.addItem(actor, itemId, count);
        }
    }

    @Override
    public void removeItem(Actor actor, String itemId, int count) {
        MessItemRemove mess = new MessItemRemove();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setItemId(itemId);
        mess.setCount(count);
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            NETWORK.broadcast(mess);
            itemService.removeItem(actor, itemId, count);
        }
    }
    
    @Override
    public void useItem(Actor actor, String itemId) {
        
        // remove20161004,注：不能在这里将playService.getTarget的目标对象设置给actor,因为actor可能是游戏中的npc,
        // 不能使用当前游戏的主目标当前游戏的主目标只适用于当前“玩家”
//        actorNetwork.setTarget(actor, playService.getTarget());
        
        MessItemUse mess = new MessItemUse();
        mess.setActorId(actor.getData().getUniqueId());
        mess.setItemId(itemId);
        
        if (NETWORK.isClient()) {
            NETWORK.sendToServer(mess);
        } else {
            Item item = itemService.getItem(actor, itemId);
            if (item != null && item.canUse(actor)) {
                NETWORK.broadcast(mess);
                itemService.useItem(actor, itemId);
            }
        }
    }


    
}
