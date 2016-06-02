///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.mess;
//
//import com.jme3.network.serializing.Serializable;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.game.service.PlayService;
//import name.huliqing.fighter.game.service.SkillService;
//import name.huliqing.fighter.object.actor.Actor;
//
///**
// *
// * @author huliqing
// */
//@Serializable
//public class MessSkillDefend extends MessSkillAbstract {
//
//    @Override
//    public void applyOnClient() {
//        PlayService playService = Factory.get(PlayService.class);
//        SkillService skillService = Factory.get(SkillService.class);
//        Actor actor = playService.findActor(actorId);
//        if (actor == null) return;
////        SkillData skillData = skillService.getSkill(actor, skillId);
//        skillService.playSkill(actor, skillId, force);
//    }
//    
//}
