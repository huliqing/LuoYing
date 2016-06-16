package name.huliqing.fighter.game.mess;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.game.service.ActorService;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// * 获得物品奖励的消息
// * @author huliqing
// */
//@Serializable
//public class MessSCSyncItemGet extends MessSCSync {
//    // 获得的物品ID
//    private String itemId;
//    // 获得的物品数量,如果count为0，则只同步物品数量
//    private int addCount;
//    // 同步物品总数
//    private int syncTotal;
//
//    public String getItemId() {
//        return itemId;
//    }
//
//    public void setItemId(String itemId) {
//        this.itemId = itemId;
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
//    public int getSyncTotal() {
//        return syncTotal;
//    }
//
//    public void setSyncTotal(int syncTotal) {
//        this.syncTotal = syncTotal;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        Actor actor = playService.findActor(actorId);
//        if (actor == null) {
//            return;
//        }
//        
//        if (addCount > 0) {
//            actorService.rewardItem(actor, itemId, addCount);
//        }
//        actorService.itemSynTotal(actor, itemId, syncTotal);
//    }
//    
//}
