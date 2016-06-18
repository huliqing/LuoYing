///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.network;
//
//import name.huliqing.fighter.game.state.lan.Network;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.ProtoData;
//import name.huliqing.fighter.game.dao.ItemDao;
//import name.huliqing.fighter.game.service.HandlerService;
//import name.huliqing.fighter.game.mess.MessProtoSync;
//import name.huliqing.fighter.game.mess.MessProtoUse;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// * 逻辑，先删除
// * @author huliqing
// */
//public class HandlerNetworkImpl implements HandlerNetwork {
//    private final static Network network = Network.getInstance();
//    private HandlerService handlerService;
//    private ItemDao actorDao;
//    
//    @Override
//    public void inject() {
//        handlerService = Factory.get(HandlerService.class);
//        actorDao = Factory.get(ItemDao.class);
//    }
//
//    @Override
//    public boolean canUse(Actor actor, ProtoData data) {
//        return handlerService.canUse(actor, data);
//    }
//
//    @Override
//    public void useForce(Actor actor, ProtoData data) {
//        handlerService.useForce(actor, data);
//    }
//
//    @Override
//    public boolean useObject(Actor actor, ProtoData data) {
//        boolean result = handlerService.canUse(actor, data);
//        if (!network.isClient()) {
//            if (result) {
//                // 广播到客户端
//                if (network.hasConnections()) {
//                    MessProtoUse mess = new MessProtoUse();
//                    mess.setActorId(actor.getData().getUniqueId());
//                    mess.setObjectId(data.getId());
//                    network.broadcast(mess);
//                }
//                
//                // 自身执行
//                handlerService.useForce(actor, data);
//                
//                // 同步物品数量
//                if (network.hasConnections()) {
//                    MessProtoSync mess = new MessProtoSync();
//                    mess.setActorId(actor.getData().getUniqueId());
//                    mess.setObjectId(data.getId());
//                    mess.setTotal(data.getTotal());
//                    network.broadcast(mess);
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public void removeObject(Actor actor, ProtoData data, int count) {
//        if (network.isClient()) 
//            return;
//
//        // 自身删除
//        handlerService.removeObject(actor, data, count);
//
//        // 同步物品数量到客户端
//        if (network.hasConnections()) {
//            MessProtoSync mess = new MessProtoSync();
//            mess.setActorId(actor.getData().getUniqueId());
//            mess.setObjectId(data.getId());
//            mess.setTotal(data.getTotal());
//            network.broadcast(mess);
//        }
//    }
//    
//}
