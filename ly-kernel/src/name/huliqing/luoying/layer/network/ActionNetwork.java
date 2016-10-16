/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.action.Action;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public interface ActionNetwork extends Inject  {

     /**
     * 让角色执行某个行为
     * @param actor
     * @param action 
     */
    void playAction(Entity actor, Action action);
    
    /**
     * 让角色执行跑路行为
     * @param actor
     * @param pos 目标位置，不是方向。
     */
    void playRun(Entity actor, Vector3f pos);
    
    /**
     * 让角色执行战斗行为
     * @param actor
     * @param target 战斗目标
     * @param skillId 要执行的技能，可为null.
     */
    void playFight(Entity actor, Entity target, String skillId);
    
}
