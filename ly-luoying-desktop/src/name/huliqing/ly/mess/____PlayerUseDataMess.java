///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.mess;
//
//import com.jme3.network.HostedConnection;
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.data.ConnData;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.mess.GameMess;
//import name.huliqing.luoying.network.GameClient;
//import name.huliqing.luoying.network.GameServer;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.ly.layer.network.GameNetwork;
//import name.huliqing.ly.layer.service.GameService;
//
///**
// * 玩家使用物品命令,这个命令只适应于玩家类型。
// * @author huliqing
// */
//@Serializable
//public class PlayerUseDataMess extends GameMess {
//    
//    private long entityId;
//    private long objectId;
//
//    public long getEntityId() {
//        return entityId;
//    }
//
//    public void setEntityId(long entityId) {
//        this.entityId = entityId;
//    }
//
//    public long getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(long objectId) {
//        this.objectId = objectId;
//    }
//
//    @Override
//    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        super.applyOnServer(gameServer, source);
//        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
//        if (entity == null)
//            return;
//        
//        // 目标entity不是玩家，并且也不是玩家的所有物，则不能执行命令
//        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
//        long playerId = cd.getEntityId();
//        if (entity.getEntityId() != playerId && Factory.get(GameService.class).getOwner(entity) != playerId) {
//            return;
//        }
//        
//        Factory.get(GameNetwork.class).playerUseData(entity, objectId);
//    }
//
//    @Override
//    public void applyOnClient(GameClient gameClient) {
//        super.applyOnClient(gameClient);
//        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
//        if (entity == null)
//            return;
//        Factory.get(GameService.class).playerUseData(entity, objectId);
//    }
//
//    
//}
