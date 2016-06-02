/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.dao.ItemDao;
import name.huliqing.fighter.game.dao.SkillDao;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.handler.Handler;

/**
 *
 * @author huliqing
 */
public class HandlerServiceImpl implements HandlerService {

    private SkillService skillService;
    private PlayService playService;
    private ItemDao actorDao;
    private SkillDao skillDao;
    
    @Override
    public void inject() {
        // inject
        playService = Factory.get(PlayService.class);
        skillService = Factory.get(SkillService.class);
        actorDao = Factory.get(ItemDao.class);
        skillDao = Factory.get(SkillDao.class);
    }

    @Override
    public boolean canUse(Actor actor, String objectId) {
        ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
        if (data == null) {
            data = skillDao.getSkillById(actor.getData(), objectId);
            if (data == null) {
                return false;
            }
        }
        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler == null) {
            return false;
        }
        return handler.canUse(actor, data);
    }

    @Override
    public void useForce(Actor actor, String objectId) {
        ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
        if (data == null) {
            data = skillDao.getSkillById(actor.getData(), objectId);
            if (data == null) {
                return;
            }
        }
        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler == null) {
            return;
        }
        handler.useForce(actor, data);
    }

    @Override
    public boolean useObject(Actor actor, String objectId) {
        if (canUse(actor, objectId)) {
            useForce(actor, objectId);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeObject(Actor actor, String objectId, int count) {
        ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
        if (data == null) {
            // 如果不是一般物品ID，则可能是技能id,因为技能并不包含在getItem中
            data = skillService.getSkill(actor, objectId);
            if (data == null) {
                return false;
            }
        }
        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler != null) {
            if (handler.remove(actor, objectId, count)) {
                
                // remove 0221现在全部交由 itemService处理提示
//                if (actor == playService.getPlayer()) {
//                    Common.getPlayState().addMessage(
//                            ResourceManager.get("common.remove"
//                            , new Object[] {ResourceManager.getObjectName(objectId), count > 1 ? count : ""})
//                            , MessageType.notice);
//                }
                
                return true;
            }
        }
        return false;
    }
    
}
