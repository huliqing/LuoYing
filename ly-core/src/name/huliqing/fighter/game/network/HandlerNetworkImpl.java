/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.network;

import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.dao.ItemDao;
import name.huliqing.fighter.game.service.HandlerService;
import name.huliqing.fighter.game.state.lan.mess.MessItemSync;
import name.huliqing.fighter.game.state.lan.mess.MessItemUse;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 逻辑，先删除
 * @author huliqing
 */
public class HandlerNetworkImpl implements HandlerNetwork {
    private final static Network network = Network.getInstance();
    private HandlerService handlerService;
    private ItemDao actorDao;
    
    @Override
    public void inject() {
        handlerService = Factory.get(HandlerService.class);
        actorDao = Factory.get(ItemDao.class);
    }

    @Override
    public boolean canUse(Actor actor, String objectId) {
        return handlerService.canUse(actor, objectId);
    }

    @Override
    public void useForce(Actor actor, String objectId) {
        handlerService.useForce(actor, objectId);
    }

    @Override
    public boolean useObject(Actor actor, String itemId) {
        boolean result = handlerService.canUse(actor, itemId);
        if (!network.isClient()) {
            if (result) {
                // 广播到客户端
                if (network.hasConnections()) {
                    MessItemUse mess = new MessItemUse();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setItemId(itemId);
                    network.broadcast(mess);
                }
                
                // 自身执行
                handlerService.useForce(actor, itemId);
                
                // 同步物品数量
                if (network.hasConnections()) {
                    ProtoData data = actorDao.getItemExceptSkill(actor, itemId);
                    MessItemSync mess = new MessItemSync();
                    mess.setActorId(actor.getData().getUniqueId());
                    mess.setItemId(itemId);
                    mess.setTotal(data != null ? data.getTotal() : 0);
                    network.broadcast(mess);
                }
            }
        }
        return result;
    }

    @Override
    public boolean removeObject(Actor actor, String objectId, int count) {
        if (!network.isClient()) {
            
            // remove0908 不再向客户端广播删除物品的消息，现在客户端是先从本地删除然后通知服务端，
            // 再由服务端删除后同步物品数量到客户端的.
//            // 广播删除物品
//            if (network.hasConnections()) {
//                MessItemDelete messDelete = new MessItemDelete();
//                messDelete.setActorId(actor.getData().getUniqueId());
//                messDelete.setItemId(objectId);
//                messDelete.setAmount(count);
//                network.broadcast(messDelete);
//            }
            
            // 自身删除
            boolean result = handlerService.removeObject(actor, objectId, count);
            
            // 同步物品数量到客户端
            if (network.hasConnections()) {
                ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
                MessItemSync mess = new MessItemSync();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setItemId(objectId);
                mess.setTotal(data != null ? data.getTotal() : 0);
                network.broadcast(mess);
            }
            
            return result;
        }
        return false;
    }
    
}
