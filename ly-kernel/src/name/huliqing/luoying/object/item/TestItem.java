/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import java.util.List;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class TestItem extends AbstractItem {

    private static final Logger LOG = Logger.getLogger(TestItem.class.getName());
    
    private final SaveService saveService = Factory.get(SaveService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private static int count;
    
    @Override
    protected void doUse(Entity actor) {
        count++;
        
        actor.updateDatas();
        EntityData ed = actor.getData().clone();
        ed.setUniqueId(DataFactory.generateUniqueId());
        Entity other = Loader.load(ed);
        playNetwork.addEntity(other);

//        Entity entity = Loader.load("actorFairy");
////        Entity entity = Loader.load("actorTower");
//        entity.getAttributeManager().getAttribute("attributeGroup", NumberAttribute.class).setValue(2);
//        entity.getAttributeManager().getAttribute("attributeLevel", NumberAttribute.class).setValue(1);
//        playNetwork.addEntity(entity);
//        
//        entityNetwork.hitAttribute(actor, "attributeLevel", 20, null);

            
        
    }
    
    private <T extends ObjectData> void removeTypes(EntityData ed, Class<T> type) {
        List<T> ods = ed.getObjectDatas(type, null);
        for (ObjectData od : ods) {
            ed.removeObjectData(od);
        }
    }
}
