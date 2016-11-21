/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import com.jme3.network.base.MessageProtocol;
import java.nio.ByteBuffer;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ActorData;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.MagicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.StateService;
import name.huliqing.luoying.mess.MessSCGameData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;

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
    private final EntityNetwork entityNetwork = Factory.get(EntityNetwork.class);

    @Override
    public boolean canUse(Entity actor) {
        return true;
    }
    
    @Override
    protected void doUse(Entity actor) {
        
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
    
        MessSCGameData mess = new MessSCGameData();
        GameData gameData = Loader.loadData("gameStoryTreasure");

        addActor(gameData, "actorAiLin");
        addActor(gameData, "actorTreasure");
        addActor(gameData, actor.getData().getId());
        
        mess.setGameData(gameData);
        ByteBuffer buffer = MessageProtocol.messageToBuffer(mess, null);
        System.out.println("");
    }
    
        
    private void addActor(GameData gameData, String id) {
        ActorData actorData = Loader.loadData(id);
        gameData.getSceneData().addEntityData(actorData);
    }
}
