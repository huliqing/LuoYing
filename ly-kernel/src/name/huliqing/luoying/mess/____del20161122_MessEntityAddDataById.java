///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.mess;
//
//import com.jme3.network.HostedConnection;
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.network.EntityNetwork;
//import name.huliqing.luoying.layer.service.EntityService;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.network.GameServer;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.entity.Entity;
//import name.huliqing.luoying.xml.ObjectData;
//
///**
// * 通过id给Entity添加物品
// * @author huliqing
// */
//@Serializable
//public class MessEntityAddDataById extends MessBase {
//    
//    private long entityId;
//    private String objectId;
//    private int amount;
//
//    public long getEntityId() {
//        return entityId;
//    }
//
//    /**
//     * 设置EntityId
//     * @param entityId 
//     */
//    public void setEntityId(long entityId) {
//        this.entityId = entityId;
//    }
//
//    public String getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    /**
//     * 设置要添加的物品数量
//     * @param amount 
//     */
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//    
//    @Override
//    public void applyOnClient() {
//        super.applyOnClient();
//        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
//        ObjectData data = Loader.loadData(objectId);
//        if (entity != null && data != null) {
//            Factory.get(EntityService.class).addObjectData(entity, data, amount);
//        }
//    }
//
//    @Override
//    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        super.applyOnServer(gameServer, source); 
//        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
//        ObjectData data = Loader.loadData(objectId);
//        if (entity != null && data != null) {
//            Factory.get(EntityNetwork.class).addObjectData(entity, data, amount);
//        }
//    }
//    
//    
//}
