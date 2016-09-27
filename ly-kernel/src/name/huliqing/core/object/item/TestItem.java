/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.item;

import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.MagicService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SaveService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.mvc.service.ViewService;
import name.huliqing.core.object.actor.Actor;

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

    @Override
    public boolean canUse(Actor actor) {
        return true;
    }
    
    @Override
    public void use(Actor actor) {
        super.use(actor);
        
        Actor aa = actorService.loadActor("actorTreasure");
        actorService.setGroup(aa, 10);
        actorService.setLevel(aa, 20);
        playService.addActor(aa);
        
        actorService.setGroup(playService.getPlayer(), 10);
    }
    
}
