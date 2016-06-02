/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.actoranim.ActorAnim;

/**
 *
 * @author huliqing
 */
public class ActorAnimServiceImpl implements ActorAnimService {

    @Override
    public void inject() {
        // ignore
    }

    @Override
    public ActorAnim loadAnim(String actorAnimId) {
        return Loader.loadActorAnim(actorAnimId);
    }
    
}
