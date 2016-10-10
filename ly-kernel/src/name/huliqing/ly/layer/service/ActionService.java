/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.layer.network.ActionNetwork;
import name.huliqing.ly.object.action.Action;
import name.huliqing.ly.object.entity.Entity;

/**
 * 
 * @author huliqing
 */
public interface ActionService extends ActionNetwork{
    
    /**
     * 载入一个Action
     * @param actionId
     * @return 
     */
    Action loadAction(String actionId);
    
    /**
     * 判断角色当前是否正在执行战斗行为。
     * @param actor
     * @return 
     */
    boolean isPlayingFight(Entity actor);
    
    /**
     * 是否正在执行跑路行为
     * @param actor
     * @return 
     */
    boolean isPlayingRun(Entity actor);
    
    /**
     * 判断目标角色是否正在跟随
     * @param actor
     * @return 
     */
    boolean isPlayingFollow(Entity actor);
    
    /**
     * 获取当前角色正在执行的行为,如果没有任何行为则返回null.
     * @param actor
     * @return 
     */
    Action getPlayingAction(Entity actor);

}
