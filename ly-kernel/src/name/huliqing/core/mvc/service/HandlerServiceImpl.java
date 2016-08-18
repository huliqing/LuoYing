/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Factory;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.define.HandlerObject;
import name.huliqing.core.mvc.dao.ItemDao;
import name.huliqing.core.mvc.dao.SkillDao;
import name.huliqing.core.object.Loader;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.handler.Handler;

/**
 *
 * @author huliqing
 */
public class HandlerServiceImpl implements HandlerService {

//    private SkillService skillService;
//    private PlayService playService;
//    private ItemDao actorDao;
//    private SkillDao skillDao;
    
    @Override
    public void inject() {
//        // inject
//        playService = Factory.get(PlayService.class);
//        skillService = Factory.get(SkillService.class);
//        actorDao = Factory.get(ItemDao.class);
//        skillDao = Factory.get(SkillDao.class);
    }
    
    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        if (data == null)
            return false;

        if (!isHandlerObject(data)) {
            return false;
        }
        
        Handler handler = Loader.loadHandler(((HandlerObject)data).getHandler());
        if (handler == null) {
            return false;
        }
        
        return handler.canUse(actor, data);
    }
    
    @Override
    public void useForce(Actor actor, ObjectData data) {
        if (data == null)
            return;
        
        if (!isHandlerObject(data)) {
            return;
        }
        
        Handler handler = Loader.loadHandler(((HandlerObject)data).getHandler());
        if (handler == null) {
            return;
        }
        
        handler.useForce(actor, data);
    }
    
    @Override
    public boolean useObject(Actor actor, ObjectData data) {
        if (canUse(actor, data)) {
            useForce(actor, data);
            return true;
        }
        return false;
    }
    
    @Override
    public void removeObject(Actor actor, ObjectData data, int count) {
        if (data == null) 
            return;
        
        if (!isHandlerObject(data)) {
            return;
        }
        
        Handler handler = Loader.loadHandler(((HandlerObject)data).getHandler());
        if (handler == null)
            return;
        
        handler.remove(actor, data, count);
    }
    
    private boolean isHandlerObject(ObjectData data) {
        return data instanceof HandlerObject;
    }
}
