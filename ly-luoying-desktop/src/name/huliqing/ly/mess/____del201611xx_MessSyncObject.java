///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.ly.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.mess.GameMess;
//import name.huliqing.luoying.network.GameClient;
//import name.huliqing.luoying.object.SyncData;
//import name.huliqing.ly.object.NetworkObject;
//import name.huliqing.ly.layer.service.GameService;
//
///**
// *
// * @author huliqing
// */
//@Serializable
//public class MessSyncObject extends GameMess{
//
//    // 同步的物品ID
//    private long objectId;
//    
//    // 需要同步的数据
//    private SyncData syncData;
//
//    public void setObjectId(long objectId) {
//        this.objectId = objectId;
//    }
//
//    public void setSyncData(SyncData syncData) {
//        this.syncData = syncData;
//    }
//    
//    @Override
//    public void applyOnClient(GameClient gameClient) {
//        super.applyOnClient(null);
//        NetworkObject syncObject = Factory.get(GameService.class).findSyncObject(objectId);
//        if (syncObject != null) {
//            syncObject.applySyncData(syncData);
//        }
//    }
//    
//}
