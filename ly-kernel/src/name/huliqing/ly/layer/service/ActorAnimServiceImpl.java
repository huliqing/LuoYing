/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.object.Loader;
import name.huliqing.ly.object.actoranim.ActorAnim;

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
        return Loader.load(actorAnimId);
    }
    
}
