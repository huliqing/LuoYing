/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.data.GameData;
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
import name.huliqing.luoying.save.SaveHelper;
import name.huliqing.luoying.save.SaveStory;

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
        
//        Entity aa = Loader.load("actorWolf");
//        entityNetwork.hitAttribute(aa, "attributeGroup", 9, null);
//        entityNetwork.hitAttribute(aa, "attributeLevel", 1, null);
//        entityNetwork.hitAttribute(actor, "attributeColor", new Vector4f(1,1,1.5f,1), null);
//        playNetwork.addEntity(actor.getScene(), aa);

//        Effect eff = Loader.load("effectTonic");
//        Emitter eff = Loader.load("emitterRandomFire");
//        actor.getScene().getRoot().attachChild(eff.getParticleEmitter());
  
//        eff.getSpatial().setQueueBucket(RenderQueue.Bucket.Translucent);
//        actor.getScene().getRoot().attachChild(eff.getSpatial());
//        ((Node)actor.getSpatial()).attachChild(eff.getSpatial());
        
//        Entity aa = Loader.load("actorTreasure");
//        entityNetwork.hitAttribute(aa, "attributeGroup", actor.getAttributeManager().getAttribute("attributeGroup", NumberAttribute.class).intValue(), null);
//        playNetwork.addEntity(actor.getScene(), aa);
        
//        if (count <= 1) {
//            Entity bb = Loader.load("actorJaime");
//            entityService.hitAttribute(bb, "attributeGroup", actor.getAttributeManager().getAttribute("attributeGroup", NumberAttribute.class).intValue(), null);
//            playNetwork.addEntity(actor.getScene(), bb);
//        }
//        
//        for (int i = 0; i < 1; i++) {
//            Entity cc = Loader.load("actorSpider");
//            entityService.hitAttribute(cc, "attributeGroup", 3, null);
//            playNetwork.addEntity(actor.getScene(), cc);
//        }
        
        SaveStory saveStory = new SaveStory();
        saveStory.setPlayer(actor.getData());
        
        SaveHelper.saveStoryLast(saveStory);
        
        
        SaveStory lastSave = SaveHelper.loadStoryLast();
        System.out.println("lastSave...");

    }
        
    private void addActor(GameData gameData, String id) {
        ActorData actorData = Loader.loadData(id);
        gameData.getSceneData().addEntityData(actorData);
    }
}
