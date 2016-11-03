///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.layer.service.ActorService;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * 服务端向客户端发送命令，杀死一个角色
// * @author huliqing
// */
//@Serializable
//public class MessActorKill extends MessBase {
//    
//    private long killActorId = -1;
//
//    public long getKillActorId() {
//        return killActorId;
//    }
//
//    public void setKillActorId(long killActorId) {
//        this.killActorId = killActorId;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        Entity actor = playService.getEntity(killActorId);
//        if (actor != null) {
//            actorService.kill(actor);
//        }
//    }
//    
//}
