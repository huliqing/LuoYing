/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.item;

import com.jme3.math.Vector4f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.MagicService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.StateService;
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
        super.use(actor);
        
        Entity aa = Loader.load("actorWolf");
        entityNetwork.hitAttribute(aa, "attributeGroup", 9, null);
        entityNetwork.hitAttribute(aa, "attributeLevel", 1, null);
        entityNetwork.hitAttribute(actor, "attributeColor", new Vector4f(1,1,1.5f,1), null);
        playNetwork.addEntity(actor.getScene(), aa);

//        Effect eff = Loader.load("effectTonic");
//        Emitter eff = Loader.load("emitterRandomFire");
//        actor.getScene().getRoot().attachChild(eff.getParticleEmitter());
  
//        eff.getSpatial().setQueueBucket(RenderQueue.Bucket.Translucent);
//        actor.getScene().getRoot().attachChild(eff.getSpatial());
//        ((Node)actor.getSpatial()).attachChild(eff.getSpatial());
    
    }
    
}
