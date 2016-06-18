///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.network;
//
//import java.util.List;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.ProtoData;
//import name.huliqing.fighter.game.dao.ItemDao;
//import name.huliqing.fighter.game.service.ItemService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.state.lan.Network;
//import name.huliqing.fighter.game.mess.MessProtoAdd;
//import name.huliqing.fighter.game.mess.MessProtoSync;
//import name.huliqing.fighter.game.mess.MessProtoUse;
//import name.huliqing.fighter.game.service.HandlerService;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// *
// * @author huliqing
// */
//public class ItemNetworkImpl implements ItemNetwork {
//
//    private final static Network network = Network.getInstance();
//    private ItemDao itemDao;
//    private ItemService itemService;
//    private PlayService playService;
//    private HandlerService handlerService;
//    
//    @Override
//    public void inject() {
//        itemDao = Factory.get(ItemDao.class);
//        playService = Factory.get(PlayService.class);
//        itemService = Factory.get(ItemService.class);
//        handlerService = Factory.get(HandlerService.class);
//    }
//    
//    @Override
//    public void addItem(Actor actor, String itemId, int count) {
//        if (network.isClient())
//            return;
//        
//        // 本地服务端更新数量后同步到客户端
//        itemService.addItem(actor, itemId, count); 
//
//        // 同步物品数量
//        ProtoData data = itemService.getItem(actor, itemId);
//        MessProtoAdd mess = new MessProtoAdd();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setObjectId(itemId);
//        mess.setAddCount(count);
//        mess.setSyncTotal(data != null ? data.getTotal() : 0);
//        network.broadcast(mess);
//    }
//
//    @Override
//    public int removeItem(Actor actor, String itemId, int count) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public ProtoData getItem(Actor actor, String itemId) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public List<ProtoData> getItems(Actor actor, List<ProtoData> store) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isSellable(ProtoData data) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void syncItemTotal(Actor actor, String itemId, int total) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void useItem(Actor actor, String itemId) {
//        
//        if (network.isClient())
//            return;
//        
//        // 广播到客户端
//        if (network.hasConnections()) {
//            MessProtoUse mess = new MessProtoUse();
//            mess.setActorId(actor.getData().getUniqueId());
//            mess.setObjectId(itemId);
//            network.broadcast(mess);
//        }
//
//        // 自身执行
//        itemService.useItem(actor, itemId);
//
//        // 同步物品数量
//        if (network.hasConnections()) {
//            ProtoData data = itemService.getItem(actor, itemId);
//            MessProtoSync mess = new MessProtoSync();
//            mess.setActorId(actor.getData().getUniqueId());
//            mess.setObjectId(itemId);
//            mess.setTotal(data != null ? data.getTotal() : 0);
//            network.broadcast(mess);
//        }
//    }
//    
//    
//}
