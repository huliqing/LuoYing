/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;
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
        
//        actor.updateDatas();
//        EntityData ed = actor.getData().clone();
//        ed.setUniqueId(DataFactory.generateUniqueId());
//        Entity other = Loader.load(ed);
//        playNetwork.addEntity(other);

//        Entity entity = Loader.load("actorDiNa");
//        Entity entity = Loader.load("actorTreasure");
        Entity entity = Loader.load("actorTower");


        entity.getAttribute("attributeGroup", NumberAttribute.class).setValue(actor.getAttribute("attributeGroup", NumberAttribute.class).intValue() + 1);
        entity.getAttribute("attributeLevel", NumberAttribute.class).setValue(5);
//        entity.getAttributeManager().getAttribute("attributeHealth", NumberAttribute.class).setValue(10);
        playNetwork.addEntity(entity);
        

//        StateData sd = Loader.loadData("stateSnowFrost");
//        actor.addObjectData(sd, 1);
        
        entityNetwork.hitAttribute(actor, "attributeHealthRestore", 20, null);
    }
    
    private <T extends ObjectData> void removeTypes(EntityData ed, Class<T> type) {
        List<T> ods = ed.getObjectDatas(type, null);
        for (ObjectData od : ods) {
            ed.removeObjectData(od);
        }
    }
}
