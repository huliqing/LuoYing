/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.xml.DataFactory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.define.CostObject;

/**
 *
 * @author huliqing
 */
public class ProtoServiceImpl implements ProtoService {
    private static final Logger LOG = Logger.getLogger(ProtoServiceImpl.class.getName());
    
    private ItemService itemService;
    private SkinService skinService;
    private SkillService skillService;
    private HandlerService handlerService;

    @Override
    public void inject() {
        itemService = Factory.get(ItemService.class);
        skinService = Factory.get(SkinService.class);
        skillService = Factory.get(SkillService.class);
        handlerService = Factory.get(HandlerService.class);
    }

    @Override
    public ObjectData createData(String id) {
        ObjectData data = DataFactory.createData(id);
        return data;
    }

    @Override
    public ObjectData getData(Actor actor, String id) {
        Class<?> cc = DataFactory.getDataClassById(id);
        if (cc == null)
            return null;
        
        if (cc == ItemData.class || cc == SkinData.class) {
            return itemService.getItem(actor, id);
        }
        if (cc == SkillData.class) {
            return skillService.getSkill(actor, id);
        }
        LOG.log(Level.WARNING, "Unsupported getData, id={0}, dataClass={1} ", new Object[] {id, cc});
        return null;
    }

    @Override
    public void addData(Actor actor, ObjectData data, int count) {
        if (data == null)
            return;

        if ((data instanceof ItemData) || (data instanceof SkinData)) {
            itemService.addItem(actor, data.getId(), count);
        } else if (data instanceof SkillData) {
            skillService.addSkill(actor, data.getId());
        } else {
            throw new UnsupportedOperationException("Could not addData, actor=" + actor + ", data" + data);
        }

    }

    @Override
    public void useData(Actor actor, ObjectData data) {
        if (data == null)
            return;
        
        handlerService.useObject(actor, data);
    }

    @Override
    public void removeData(Actor actor, ObjectData data, int count) {
        if (data == null)
            return;
        
        handlerService.removeObject(actor, data, count);
    }

    @Override
    public void syncDataTotal(Actor actor, String id, int total) {
        Class<?> cc = DataFactory.getDataClassById(id);
        if (cc == null)
            return;
        
        if (cc == ItemData.class || cc == SkinData.class) {
            itemService.syncItemTotal(actor, id, total);
            return;
        } 
        LOG.log(Level.WARNING, "Unsupported syncDataTotal, actor={0}, object id={1}, total={2}"
                , new Object[] {actor, id, total});
    }

    @Override
    public float getCost(ObjectData data) {
        if (data instanceof CostObject) {
            return ((CostObject)data).getCost();
        }
        return 0;
    }
 
}
