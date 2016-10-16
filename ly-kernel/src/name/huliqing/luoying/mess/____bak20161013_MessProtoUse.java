package name.huliqing.luoying.mess;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.mess;
//
//import com.jme3.network.HostedConnection;
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.ly.Factory;
//import name.huliqing.ly.data.ObjectData;
//import name.huliqing.ly.layer.service.PlayService;
//import name.huliqing.ly.data.ConnData;
//import name.huliqing.ly.layer.service.ActorService;
//import name.huliqing.ly.network.GameServer;
//import name.huliqing.ly.layer.service.ObjectService;
//import name.huliqing.ly.layer.network.ObjectNetwork;
//import name.huliqing.ly.object.entity.Entity;
//
///**
// * 使用物品的指令
// * @author huliqing
// */
//@Serializable
//public class MessProtoUse extends MessBase {
//    
//    // 角色id
//    private long actorId;
//    // 物品的ID
//    private String objectId;
//
//    /**
//     * 获取使用物品的角色
//     * @return 
//     */
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//
//    /**
//     * 获取被使用的物品
//     * @return 
//     */
//    public String getObjectId() {
//        return objectId;
//    }
//
//    /**
//     * 设置被使用的物品 
//     * @param objectId
//     */
//    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }
//
//    @Override
//    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        PlayService playService = Factory.get(PlayService.class);
//        ObjectService protoService = Factory.get(ObjectService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        ObjectNetwork protoNetwork = Factory.get(ObjectNetwork.class);
//        
//        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
//        long clientActorId = cd.getEntityId();
//        
//        Entity actor = playService.getEntity(actorId);
//         // 找不到指定的角色或者角色不是客户端所控制的。
//        if (actor == null) {
//            return;
//        }
//        // 使用物品的必须是客户端角色自身或者客户端角色的宠物
//        if (actor.getData().getUniqueId() == clientActorId
//                || actorService.getOwner(actor) == clientActorId) {
//            ObjectData data = protoService.getData(actor, objectId);
//            protoNetwork.useData(actor, data);
//        }
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ObjectService protoService = Factory.get(ObjectService.class);
//        Entity actor = playService.getEntity(actorId);
//        if (actor != null) {
//            ObjectData data = protoService.getData(actor, objectId);
//            protoService.useData(actor, data); 
//        }
//    }
//    
//}
