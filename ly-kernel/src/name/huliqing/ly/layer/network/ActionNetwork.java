/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.ly.Inject;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.actor.Actor;

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
    void playAction(Actor actor, Action action);
    
    /**
     * 让角色执行跑路行为
     * @param actor
     * @param pos 目标位置，不是方向。
     */
    void playRun(Actor actor, Vector3f pos);
    
    /**
     * 让角色执行战斗行为
     * @param actor
     * @param target 战斗目标
     * @param skillId 要执行的技能，可为null.
     */
    void playFight(Actor actor, Actor target, String skillId);
    
}
