/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Inject;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.entity.Entity;

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
