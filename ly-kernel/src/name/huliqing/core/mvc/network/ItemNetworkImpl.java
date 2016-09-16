/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.mess.MessItemAdd;
import name.huliqing.core.mess.MessItemRemove;
import name.huliqing.core.mess.MessItemUse;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.network.Network;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.item.Item;
import name.huliqing.core.object.module.ItemListener;

/**
 *
 * @author huliqing
 */
public class ItemNetworkImpl implements ItemNetwork {    
    private final static Network NETWORK = Network.getInstance();
    private ItemService itemService;
    
    @Override
    public void inject() {
        itemService = Factory.get(ItemService.class);
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

    @Override
    public Item getItem(Actor actor, String itemId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Item> getItems(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addItemListener(Actor actor, ItemListener itemListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeItemListener(Actor actor, ItemListener itemListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncItemTotal(Actor actor, String itemId, int total) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
}
