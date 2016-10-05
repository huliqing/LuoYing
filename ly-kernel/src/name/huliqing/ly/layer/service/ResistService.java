/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface ResistService extends Inject {
    
    /**
     * 获取角色的抗性:0.0~1.0
     * @param actor 角色
     * @param stateId 指定的状态．
     * @return 
     */
    float getResist(Actor actor, String stateId);
}
