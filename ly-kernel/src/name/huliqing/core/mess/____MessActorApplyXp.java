///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.core.Factory;
//import name.huliqing.core.mvc.service.ActorService;
//import name.huliqing.core.mvc.service.PlayService;
//import name.huliqing.core.object.actor.Actor;
//
///**
// * 给指定的角色添加XP
// * @author huliqing
// */
//@Serializable
//public class MessActorApplyXp extends MessBase {
//    
//    // 角色ID
//    private long actorId;
//    
//    // 要增加的XP量
//    private int xp;
//
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//
//    public int getXp() {
//        return xp;
//    }
//
//    public void setXp(int xp) {
//        this.xp = xp;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        Actor actor = playService.findActor(actorId);
//        if (actor != null) {
//            actorService.applyXp(actor, xp);
//        }
//    }
//    
//    
//}
