/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.MagicService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.mvc.service.ViewService;
import name.huliqing.core.object.effect.ProjectionEffect;

/**
 *
 * @author huliqing
 */
public class TestHandlerProjectionEffect {
    
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final MagicService magicService = Factory.get(MagicService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    private final ViewService viewService = Factory.get(ViewService.class);
    
    public void test() {
//        ProjectionEffect effect = (ProjectionEffect) effectService.loadEffect("effectTest");
//        Vector3f pos = playService.getActorForwardPosition(playService.getPlayer(), 3, null);
//        effect.setScale(new Vector3f(10,10, 10));
//        effect.setLocation(pos);
//        effect.addReceiver(playService.getTerrain());
//        playService.addObject(effect);
    }
}
