/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.data.ActorLogicData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actorlogic.ActorLogic;

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
    ActorLogic loadLogic(String logicId);
    
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
     * @return 
     */
    void addLogic(Actor actor, ActorLogicData logicData);
        
    /**
     * 给角色添加逻辑
     * @param actor
     * @param logic
     * @return 
     */
    void addLogic(Actor actor, ActorLogic logic);
    
    /**
     * 移除指定的逻辑，如果移除成功则返回true.
     * @param actor
     * @param logic
     * @return 
     */
    boolean removeLogic(Actor actor, ActorLogic logic);
    
    /**
     * 清除角色身上的所有逻辑
     * @param actor
     * @return 
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
}
