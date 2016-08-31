/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import com.jme3.math.Vector3f;
import name.huliqing.core.Inject;
import name.huliqing.core.object.action.Action;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.module.ActionModule;

/**
 * 
 * @author huliqing
 */
public interface ActionService extends Inject {
    
    /**
     * 载入一个Action
     * @param actionId
     * @return 
     */
    Action loadAction(String actionId);
    
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
    
    /**
     * 判断角色当前是否正在执行战斗行为。
     * @param actor
     * @return 
     */
    boolean isPlayingFight(Actor actor);
    
    /**
     * 是否正在执行跑路行为
     * @param actor
     * @return 
     */
    boolean isPlayingRun(Actor actor);
    
    /**
     * 判断目标角色是否正在跟随
     * @param actor
     * @return 
     */
    boolean isPlayingFollow(Actor actor);
    
    /**
     * 获取当前角色正在执行的行为,如果没有任何行为则返回null.
     * @param actor
     * @return 
     */
    Action getPlayingAction(Actor actor);

}
