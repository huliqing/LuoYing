/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.item;

import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.EffectService;
import name.huliqing.ly.layer.service.MagicService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SaveService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.layer.service.StateService;
import name.huliqing.ly.layer.service.ViewService;
import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.entity.Entity;

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
    private final EffectService effectService = Factory.get(EffectService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);

    @Override
    public boolean canUse(Entity actor) {
        return true;
    }
    
    @Override
    public void use(Entity actor) {
        super.use(actor);
        
        Entity aa = Loader.load("actorHard");
        actorService.setGroup(aa, 10);
        actorService.setLevel(aa, 2);
        playNetwork.addEntity(actor.getScene(), aa);
        
    }
    
}
