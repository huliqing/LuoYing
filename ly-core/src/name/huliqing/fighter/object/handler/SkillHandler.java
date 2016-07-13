/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.GameException;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.constants.SkillConstants;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.network.ActionNetwork;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.skill.Skill;

/**
 *
 * @author huliqing
 */
public class SkillHandler extends AbstractHandler {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final ActionNetwork actionNetwork = Factory.get(ActionNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StateService stateService = Factory.get(StateService.class);

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
    }
    
    @Override
    public boolean canUse(Actor actor, ProtoData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        if (!(data instanceof SkillData)) {
            return false;
        }
        SkillData skillData = (SkillData) data;
        Skill skill = skillService.getSkillInstance(actor, skillData.getId());
        
        int skillStateCode = skillService.checkStateCode(actor, skill, false);
        if (skillStateCode != SkillConstants.STATE_OK) {
            if (actor.isPlayer()) {
                switch (skillStateCode) {
                    case SkillConstants.STATE_MANA_NOT_ENOUGH:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_MANA_NOT_ENOUGH), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_COOLDOWN:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_COOLDOWN), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_LOCKED:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_LOCKED), MessageType.notice);
                        break;
                    case SkillConstants.STATE_SKILL_NOT_FOUND:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_NOT_FOUND), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_NOT_FOUND:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_FOUND), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_NOT_IN_RANGE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_NOT_IN_RANGE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_TARGET_UNSUITABLE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_TARGET_UNSUITABLE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_UNDEFINE:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_UNDEFINE), MessageType.notice);
                        break;
                    case SkillConstants.STATE_WEAPON_NEED_TAKE_ON:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NEED_TAKE_ON), MessageType.notice);
                        break;
                    case SkillConstants.STATE_WEAPON_NOT_ALLOW:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_WEAPON_NOT_ALLOW), MessageType.notice);
                        break;
                    case SkillConstants.STATE_NEED_LEVEL:
                        playNetwork.addMessage(actor
                                , ResourceManager.get(ResConstants.SKILL_NEED_LEVEL, new Object[] {skillData.getNeedLevel()})
                                , MessageType.notice);
                        break;
                    case SkillConstants.STATE_CAN_NOT_INTERRUPT:
                        break;
                    case SkillConstants.STATE_HOOK:
//                        Logger.getLogger(SkillHandler.class.getName()).log(Level.INFO, "Could not play skill by Hook listener!");
                        break;
                }
            }
            return false;
        }
        
        return true;
    }

    @Override
    protected void useObject(Actor actor, ProtoData data) {
        SkillData skillData = (SkillData) data;
        
        // remove20160129
//        // 注意所有物品的使用和技能的使用不一定都是玩家在执行。也就是useForce中的
//        // actor参数不一定是player
//        
//        // 对于攻击技能，需要转入"战斗行为", 其它
//        SkillType st = skillData.getSkillType();
//        if (st == SkillType.attack || st == SkillType.trick) {
//            
//            Actor target = actorService.getTarget(actor);
//            float viewDistance = actorService.getViewDistance(actor);
//            if (target == null 
//                    || target.getModel().getParent() == null 
//                    || target.isDead() 
//                    || !target.isEnemy(actor)
//                    || target.getDistance(actor) > viewDistance * 2
//                    ) {
//                target = actorService.findNearestEnemyExcept(actor, viewDistance * 2, null);
//                
//                actorNetwork.setTarget(actor, target);
//                
//                if (target == null) {
//                    // 没目标：攻击类技能需要目标，如果没有目标则不执行技能，否则会经常浪费魔法值
//                    playNetwork.addMessage(actor, ResourceManager.get(ResConstants.SKILL_TARGET_NOT_FOUND), MessageType.notice);
//                    return;
//                }
//            }
//            // 注意：客户端不能执行action
//            actionNetwork.playFight(actor, target, skillData.getId());
//        } else {
//            // 执行普通技能
//            skillNetwork.playSkill(actor, skillData, false);
//        }
        
        skillNetwork.playSkill(actor, skillData.getId(), false);
    }

    @Override
    public boolean remove(Actor actor, ProtoData data, int count) throws GameException {
        // 技能不允许删除
//        showWarn("Skill could not delete, skill=" + od);
        return false;
    }
    
}
