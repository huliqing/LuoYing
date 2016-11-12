///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.layer.service.ObjectService;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * 给角色添加物体
// * @author huliqing
// */
//@Serializable
//public class MessProtoAdd extends MessBase {
//    private long actorId;
//    // 获得的物品ID
//    private String objectId;
//    // 获得的物品数量,如果count为0，则只同步物品数量
//    private int addCount;
//    
//    // remove20160830
////    // 同步物品总数
////    private int syncTotal;
//
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//    
//    public String getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(String itemId) {
//        this.objectId = itemId;
//    }
//
//    public int getAddCount() {
//        return addCount;
//    }
//
//    public void setAddCount(int count) {
//        this.addCount = count;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ObjectService protoService = Factory.get(ObjectService.class);
//        Entity actor = playService.getEntity(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        if (addCount > 0) {
//            protoService.addData(actor, objectId, addCount);
//        }
//
//    }
//}
