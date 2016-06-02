/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.handler;

import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.HandlerData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.EffectService;
import name.huliqing.fighter.game.service.MagicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.object.effect.Effect;

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

    @Override
    public void initData(HandlerData data) {
        super.initData(data);
    }

    @Override
    public boolean canUse(Actor actor, ProtoData data) {
        return true;
    }
    
    @Override
    protected void useObject(Actor actor, ProtoData data) {

        Actor target = actorService.getTarget(actor);
        if (target != null) {
//            stateService.addState(target, "stateSpiderWeb");
//            stateService.addState(target, "stateIceFrozen");
            stateService.addState(target, "stateScorpionVenom", actor);
        }

    }

   
    
  

}
