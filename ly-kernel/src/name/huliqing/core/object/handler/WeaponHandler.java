/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.GameException;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.service.ActionService;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.LogicService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.action.FightAction;

/**
 * 切换武器
 * @author huliqing
 */
public class WeaponHandler extends AbstractSkinHandler {
    private final static Logger logger = Logger.getLogger(WeaponHandler.class.getName());
    private final SkinService skinService = Factory.get(SkinService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);
    private final LogicService logicService = Factory.get(LogicService.class);

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
    }

    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        if (!(data instanceof SkinData)) {
            if (Config.debug) {
                logger.log(Level.WARNING, "Not a Skin object! data={0}", data.getId());
            }
            return false;
        }
        if (skillService.isAttacking(actor)) {
            return false;
        }
        SkinData skinData = (SkinData) data;
        if (!skinService.isWeapon(skinData)) {
            return false;
        }
                
        return true;
    }

    @Override
    protected void useObject(Actor actor, ObjectData data) {
        SkinData skinData = (SkinData) data;
        if (skinData.isUsing()) {
            if (skinService.isWeaponTakeOn(actor) 
                    && skinData.getSlots() != null && !skinData.getSlots().isEmpty()) {
                skinService.takeOffWeapon(actor, false);
            } else {
                skinService.detachSkin(actor, skinData);
            }
            
        } else {
            skinService.attachSkin(actor, skinData);
        }
        
        // 打开或关闭侦察敌人的逻辑,
        // 注：当关闭侦察敌人时，如果当前还在战斗，则需要停止战斗行为和清除角色当前战斗目标
        if (!skinService.isWeaponTakeOn(actor)) {
            logicService.setAutoDetect(actor, false);
            Action action = actionService.getPlayingAction(actor);
            if (action instanceof FightAction) {
                actionService.playAction(actor, null);
                actorService.setTarget(actor, null);
            }
        } else {
            logicService.setAutoDetect(actor, true);
        }
    }

    @Override
    public boolean remove(Actor actor, ObjectData data, int count) throws GameException {
//        ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
        if (!(data instanceof SkinData)) {
            return false;
        }
        SkinData weaponData = (SkinData) data;
        if (!skinService.isWeapon(weaponData)) {
            return false;
        }
        if (weaponData.isUsing()) {
            return false;
        }
        return super.remove(actor, data, count);
    }
    
}
