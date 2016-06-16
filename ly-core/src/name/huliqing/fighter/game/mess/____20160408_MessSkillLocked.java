package name.huliqing.fighter.game.mess;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.SkillService;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// *
// * @author huliqing
// */
//@Serializable
//public class MessSkillLocked extends MessBase {
//    
//    private long actorId;
//    private boolean locked;
//
//    public long getActorId() {
//        return actorId;
//    }
//
//    public void setActorId(long actorId) {
//        this.actorId = actorId;
//    }
//
//    /**
//     * 是否锁定角色技能
//     * @return 
//     */
//    public boolean isLocked() {
//        return locked;
//    }
//
//    public void setLocked(boolean locked) {
//        this.locked = locked;
//    }
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        SkillService skillService = Factory.get(SkillService.class);
//        Actor actor = playService.findActor(actorId);
//        if (actor == null) return;
//        skillService.setLocked(actor, locked);
//    }
//    
//    
//}
