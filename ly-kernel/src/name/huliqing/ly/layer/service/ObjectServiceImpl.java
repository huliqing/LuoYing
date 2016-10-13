/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.Collections;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.data.ItemData;
import name.huliqing.ly.data.SkillData;
import name.huliqing.ly.data.SkinData;
import name.huliqing.ly.data.ObjectData;
import name.huliqing.ly.xml.DataFactory;
import name.huliqing.ly.data.define.CostObject;
import name.huliqing.ly.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class ObjectServiceImpl implements ObjectService {
//    private static final Logger LOG = Logger.getLogger(ObjectServiceImpl.class.getName());
    
    private ItemService itemService;
    private SkinService skinService;
    private SkillService skillService;

    @Override
    public void inject() {
        itemService = Factory.get(ItemService.class);
        skinService = Factory.get(SkinService.class);
        skillService = Factory.get(SkillService.class);
    }

    @Override
    public ObjectData createData(String id) {
        ObjectData data = DataFactory.createData(id);
        return data;
    }

    @Override
    public void addData(Entity actor, String id, int count) {
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
    public void removeData(Entity actor, String id, int count) {

        Class<?> cc = DataFactory.getDataClassById(id);
        if (cc == null)
            return;
        
        if (ItemData.class.isAssignableFrom(cc)) {
            itemService.removeItem(actor, id, count);
        } else if (SkinData.class.isAssignableFrom(cc)) {
            skinService.removeSkin(actor, id, count);            
        } else if (SkillData.class.isAssignableFrom(cc)) {
            skillService.removeSkill(actor, id); 
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void useData(Entity actor, ObjectData data) {
        if (data == null)
            return;
        
        throw new UnsupportedOperationException("unsupported yet!");
    }

    @Override
    public ObjectData getData(Entity actor, String id) {
        return actor.getData().getObjectData(id);
    }

    @Override
    public List<ObjectData> getDatas(Entity actor) {
        return Collections.unmodifiableList(actor.getData().getObjectDatas());
    }

    @Override
    public float getCost(ObjectData data) {
        if (data instanceof CostObject) {
            return ((CostObject)data).getCost();
        }
        return 0;
    }
    
//    @Override
//    public boolean isSellable(ObjectData data) {
//        // 金币不能卖
//        return !data.getId().equals(IdConstants.ITEM_GOLD);
//    }
 
}
