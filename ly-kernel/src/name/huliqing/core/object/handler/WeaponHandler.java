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
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.enums.DataType;
import name.huliqing.core.game.dao.ItemDao;
import name.huliqing.core.game.service.ActionService;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.SkinService;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.action.FightAction;

/**
 * 切换武器
 * @author huliqing
 */
public class WeaponHandler extends AbstractHandler {
    private final static Logger logger = Logger.getLogger(WeaponHandler.class.getName());
    private final ItemDao actorDao = Factory.get(ItemDao.class);
    private final SkinService skinService = Factory.get(SkinService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final ActionService actionService = Factory.get(ActionService.class);

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
    }

    @Override
    public boolean canUse(Actor actor, ProtoData data) {
        if (!super.canUse(actor, data)) {
            return false;
        }
        if (data.getProto().getDataType() != DataType.skin) {
            if (Config.debug) {
                logger.log(Level.WARNING, "Not a Skin object! data={0}", data.getId());
            }
            return false;
        }
        if (actor.isAttacking()) {
            return false;
        }
        SkinData skinData = (SkinData) data;
        if (!skinService.isWeapon(skinData)) {
            return false;
        }
                
        return true;
    }

    @Override
    protected void useObject(Actor actor, ProtoData data) {
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
            actorService.setAutoDetect(actor, false);
            Action action = actionService.getPlayingAction(actor);
            if (action instanceof FightAction) {
                actionService.playAction(actor, null);
                actorService.setTarget(actor, null);
            }
        } else {
            actorService.setAutoDetect(actor, true);
        }
    }

    @Override
    public boolean remove(Actor actor, ProtoData data, int count) throws GameException {
//        ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
        if (data.getDataType() != DataType.skin) {
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
