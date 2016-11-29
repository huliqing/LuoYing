/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.MagicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.StateService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.xml.DataFactory;
import name.huliqing.luoying.xml.ObjectData;

/**
 *
 * @author huliqing
 */
public class TestItem extends AbstractItem {

    private final SaveService saveService = Factory.get(SaveService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final MagicService magicService = Factory.get(MagicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final EntityService entityService = Factory.get(EntityService.class);
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);
    
    private static int count;

    @Override
    public boolean canUse(Entity actor) {
        return true;
    }
    
    @Override
    protected void doUse(Entity actor) {
        count++;
      
        actor.updateDatas();
        EntityData ed = actor.getData().clone();
        ed.setUniqueId(DataFactory.generateUniqueId());
        
        Entity other = Loader.load(ed);
        
        playNetwork.addEntity(other);
    }
        
    private <T extends ObjectData> void removeTypes(EntityData ed, Class<T> type) {
        List<T> ods = ed.getObjectDatas(type, null);
        for (ObjectData od : ods) {
            ed.removeObjectData(od);
        }
    }
}
