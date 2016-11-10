/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ItemData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.data.SkinData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class ObjectServiceImpl implements ObjectService {
//    private static final Logger LOG = Logger.getLogger(ObjectServiceImpl.class.getName());
    
    private SkinService skinService;
    private SkillService skillService;

    @Override
    public void inject() {
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
//            itemService.addItem(actor, id, count);
//             throw new UnsupportedOperationException("Could not addData, actor=" + actor + ", id" + id);
            ItemData data = Loader.loadData(id);
            actor.addObjectData(data, count);
             
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
//            itemService.removeItem(actor, id, count);
            
            throw new UnsupportedOperationException("Could not addData, actor=" + actor + ", id" + id);
            
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
 
}
