/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.actoranim.ActorAnim;

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
