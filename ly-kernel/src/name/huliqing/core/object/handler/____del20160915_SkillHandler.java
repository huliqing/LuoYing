///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.core.object.handler;
//
//import name.huliqing.core.Factory;
//import name.huliqing.core.GameException;
//import name.huliqing.core.constants.ResConstants;
//import name.huliqing.core.constants.SkillConstants;
//import name.huliqing.core.object.actor.Actor;
//import name.huliqing.core.data.ObjectData;
//import name.huliqing.core.data.SkillData;
//import name.huliqing.core.enums.MessageType;
//import name.huliqing.core.mvc.network.PlayNetwork;
//import name.huliqing.core.mvc.network.SkillNetwork;
//import name.huliqing.core.mvc.service.SkillService;
//import name.huliqing.core.manager.ResourceManager;
//import name.huliqing.core.mvc.service.ActorService;
//import name.huliqing.core.object.skill.Skill;
//
///**
// * 专门用于技能的Handler
// * @author huliqing
// */
//public class SkillHandler extends AbstractHandler {
//    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
//    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final SkillService skillService = Factory.get(SkillService.class);
//
//    @Override
//    public void setData(HandlerData data) {
//        super.setData(data);
//    }
//    
//    @Override
//    public boolean canUse(Actor actor, ObjectData data) {
//        if (!super.canUse(actor, data)) {
//            return false;
//        }
//        if (!(data instanceof SkillData)) {
//            return false;
//        }
//        SkillData skillData = (SkillData) data;
//        Skill skill = skillService.getSkill(actor, skillData.getId());
//        
//        int skillStateCode = skillService.checkStateCode(actor, skill, false);
//        if (skillStateCode != SkillConstants.STATE_OK) {
//            if (actorService.isPlayer(actor)) {
//                switch (skillStateCode) {
//                    case SkillConstants.STATE_MANA_NOT_ENOUGH:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_MANA_NOT_ENOUGH), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_SKILL_COOLDOWN:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_COOLDOWN), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_SKILL_LOCKED:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_LOCKED), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_SKILL_NOT_FOUND:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_NOT_FOUND), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_TARGET_NOT_FOUND:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_FOUND), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_TARGET_NOT_IN_RANGE:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_IN_RANGE), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_TARGET_UNSUITABLE:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_TARGET_UNSUITABLE), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_UNDEFINE:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_UNDEFINE), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_WEAPON_NEED_TAKE_ON:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NEED_TAKE_ON), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_WEAPON_NOT_ALLOW:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NOT_ALLOW), MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_NEED_LEVEL:
//                        playNetwork.addMessage(actor
//                                , ResourceManager.get(ResConstants.SKILL_NEED_LEVEL, new Object[] {skillData.getNeedLevel()})
//                                , MessageType.notice);
//                        break;
//                    case SkillConstants.STATE_CAN_NOT_INTERRUPT:
//                        break;
//                    case SkillConstants.STATE_HOOK:
////                        Logger.getLogger(SkillHandler.class.getName()).log(Level.INFO, "Could not play skill by Hook listener!");
//                        break;
//                }
//            }
//            return false;
//        }
//        
//        return true;
//    }
//
//    @Override
//    protected void useObject(Actor actor, ObjectData data) {
//        SkillData skillData = (SkillData) data;
//        
//        skillNetwork.playSkill(actor, skillData.getId(), false);
//    }
//
//    @Override
//    public boolean remove(Actor actor, ObjectData data, int count) throws GameException {
//        // 技能不允许删除
////        showWarn("Skill could not delete, skill=" + od);
//        return false;
//    }
//    
//}
