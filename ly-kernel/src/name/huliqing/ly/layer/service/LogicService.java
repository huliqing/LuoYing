/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.data.LogicData;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.logic.Logic;

/**
 *
 * @author huliqing
 */
public interface LogicService extends Inject {
    
    /**
     * 载入一个角色逻辑
     * @param logicId
     * @return 
     */
    Logic loadLogic(String logicId);
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicId
     */
    void addLogic(Actor actor, String logicId);
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicData 
     */
    void addLogic(Actor actor, LogicData logicData);
        
    /**
     * 给角色添加逻辑
     * @param actor
     * @param logic 
     */
    void addLogic(Actor actor, Logic logic);
    
    /**
     * 移除指定的逻辑，如果移除成功则返回true.
     * @param actor
     * @param logic
     * @return 
     */
    boolean removeLogic(Actor actor, Logic logic);
    
    /**
     * 清除角色身上的所有逻辑
     * @param actor 
     */
    void clearLogics(Actor actor);
    
    /**
     * 注：该方法会清理当前角色的逻辑，然后把角色的逻辑重置为玩家类的逻辑。
     * 玩家类的逻辑和普通角色的逻辑有些不同，如玩家逻辑可能没有idle行为，
     * 并且检测敌人的频率可能稍高，技能施
     * 法可能有些限制或区别.
     * @param actor 
     */
    void resetPlayerLogic(Actor actor);
    
    /**
     * 判断角色的逻辑是否打开
     * @param actor
     * @return 
     */
    boolean isAutoLogic(Actor actor);
    
    /**
     * 打开或关闭角色的自动逻辑
     * @param actor
     * @param enabled 
     */
    void setAutoLogic(Actor actor, boolean enabled);
    
    /**
     * 判断角色是否自动侦察敌人
     * @param actor
     * @return 
     */
    boolean isAutoDetect(Actor actor);
    
    /**
     * 设置角色是否自动侦察敌人
     * @param actor 
     * @param autoDetect
     */
    void setAutoDetect(Actor actor, boolean autoDetect);
}
