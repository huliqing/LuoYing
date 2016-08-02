/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.game.network.PlayNetwork;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.EffectService;
import name.huliqing.core.game.service.MagicService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.SkillService;
import name.huliqing.core.game.service.StateService;
import name.huliqing.core.game.service.ViewService;

/**
 *
 * @author huliqing
 */
public class TestHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(TestHandler.class.getName());
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final MagicService magicService = Factory.get(MagicService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    private final SkillService skillService = Factory.get(SkillService.class);

    @Override
    public void setData(HandlerData data) {
        super.setData(data);
    }

    @Override
    public boolean canUse(Actor actor, ProtoData data) {
        return true;
    }
    
    @Override
    protected void useObject(Actor actor, ProtoData data) {
        
//        Actor aa = actorService.loadActor("actorSkeleton");
//        actorService.setGroup(aa, FastMath.nextRandomInt(1, 100));
//        actorService.setLevel(aa, 1);
//        skillService.playSkill(aa, skillService.getSkill(aa, SkillType.wait).getId(), false);
//        playService.addActor(aa);

//        Actor target = actorService.getTarget(actor);
//        if (target != null) {
////            stateService.addState(target, "stateSpiderWeb");
////            stateService.addState(target, "stateIceFrozen");
//            stateService.addState(target, "stateScorpionVenom", actor);
//        }

        Actor aa = actorService.loadActor("actorAltar");
        playService.addActor(aa);

    }

   
    
  

}
