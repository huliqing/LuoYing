/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.service;

import name.huliqing.core.Inject;
import name.huliqing.core.object.actoranim.ActorAnim;

/**
 *
 * @author huliqing
 */
public interface ActorAnimService extends Inject {
    
    /**
     * 载入一个ActorAnim
     * @param actorAnimId
     * @return 
     */
    ActorAnim loadAnim(String actorAnimId);
}
