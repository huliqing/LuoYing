/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.loader.Loader;
import name.huliqing.core.object.actoranim.ActorAnim;

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
