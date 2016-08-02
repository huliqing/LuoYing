/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.Factory;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.game.dao.ItemDao;
import name.huliqing.core.game.dao.SkillDao;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.handler.Handler;

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
    public boolean canUse(Actor actor, ProtoData data) {
        if (data == null)
            return false;

        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler == null) {
            return false;
        }
        
        return handler.canUse(actor, data);
    }

    @Override
    public void useForce(Actor actor, ProtoData data) {
        if (data == null)
            return;
        
        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler == null) {
            return;
        }
        
        handler.useForce(actor, data);
    }

    @Override
    public boolean useObject(Actor actor, ProtoData data) {
        if (canUse(actor, data)) {
            useForce(actor, data);
            return true;
        }
        return false;
    }

    @Override
    public void removeObject(Actor actor, ProtoData data, int count) {
        if (data == null) 
            return;
        
        Handler handler = Loader.loadHandler(data.getHandler());
        if (handler == null)
            return;
        
        handler.remove(actor, data, count);
    }
    
}
