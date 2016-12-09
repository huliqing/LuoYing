/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.data.LogicData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.logic.Logic;

/**
 *
 * @author huliqing
 */
public interface LogicService extends Inject {
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicId
     */
    void addLogic(Entity actor, String logicId);
    
    /**
     * 给角色添加一个逻辑
     * @param actor
     * @param logicData 
     */
    void addLogic(Entity actor, LogicData logicData);
        
    /**
     * 给角色添加逻辑
     * @param actor
     * @param logic 
     */
    void addLogic(Entity actor, Logic logic);
    
    /**
     * 移除指定的逻辑，如果移除成功则返回true.
     * @param actor
     * @param logic
     * @return 
     */
    boolean removeLogic(Entity actor, Logic logic);
    
    /**
     * 清除角色身上的所有逻辑
     * @param actor 
     */
    void clearLogics(Entity actor);
    
}
