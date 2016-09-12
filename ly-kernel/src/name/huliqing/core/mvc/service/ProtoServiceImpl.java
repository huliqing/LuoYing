/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
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
    public void addData(Actor actor, String id, int count) {
        Class<?> cc = DataFactory.getDataClassById(id);
        if (cc == null)
            return;
        
        if (ItemData.class.isAssignableFrom(cc)) {
            itemService.addItem(actor, id, count);
            
        } else if (SkinData.class.isAssignableFrom(cc)) {
            skinService.addSkin(actor, id, count);
            
        } else if (SkillData.class.isAssignableFrom(cc)) {
            skillService.addSkill(actor, id);
            
        } else {
            throw new UnsupportedOperationException("Could not addData, actor=" + actor + ", id" + id);
        }

    }

    @Override
    public void removeData(Actor actor, String id, int count) {
        // remove20160822
//        ObjectData data = actor.getData().getObjectData(id);
//        handlerService.removeObject(actor, data, count);

        Class<?> cc = DataFactory.getDataClassById(id);
        if (cc == null)
            return;
        
        if (ItemData.class.isAssignableFrom(cc)) {
            itemService.removeItem(actor, id, count);
            
        } else if (SkinData.class.isAssignableFrom(cc)) {
//            skinService.removeSkin(actor, id, count);
            throw new UnsupportedOperationException();
            
        } else if (SkillData.class.isAssignableFrom(cc)) {
            skillService.removeSkill(actor, id);
            
        } else {
            throw new UnsupportedOperationException();
        }

    }

    @Override
    public void useData(Actor actor, ObjectData data) {
        if (data == null)
            return;
        
        handlerService.useObject(actor, data);
    }

    @Override
    public ObjectData getData(Actor actor, String id) {
        // remove20160821
//        Class<?> cc = DataFactory.getDataClassById(id);
//        if (cc == null)
//            return null;
//        if (cc == ItemData.class || cc == SkinData.class) {
//            return itemService.getItem(actor, id);
//        }
//        if (cc == SkillData.class) {
//            return skillService.getSkill(actor, id).getData();
//        }
//        LOG.log(Level.WARNING, "Unsupported getData, id={0}, dataClass={1} ", new Object[] {id, cc});
//        return null;

        return actor.getData().getObjectData(id);
    }

    @Override
    public List<ObjectData> getDatas(Actor actor) {
        return Collections.unmodifiableList(actor.getData().getObjectDatas());
    }

    // remove20160830
//    @Override
//    public void syncDataTotal(Actor actor, String id, int total) {
//        Class<?> cc = DataFactory.getDataClassById(id);
//        if (cc == null)
//            return;
//        
//        if (cc == ItemData.class) {
//            itemService.syncItemTotal(actor, id, total);
//            return;
//        } 
//        
//        if (cc == SkinData.class) {
//            throw new UnsupportedOperationException();
//        }
//        
//        LOG.log(Level.WARNING, "Unsupported syncDataTotal, actor={0}, object id={1}, total={2}"
//                , new Object[] {actor, id, total});
//    }

    @Override
    public float getCost(ObjectData data) {
        if (data instanceof CostObject) {
            return ((CostObject)data).getCost();
        }
        return 0;
    }
    
    @Override
    public boolean isSellable(ObjectData data) {
        // 金币不能卖
        return !data.getId().equals(IdConstants.ITEM_GOLD);
    }
 
}
