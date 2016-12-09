/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import java.util.List;
import name.huliqing.luoying.layer.network.StateNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.StateListener;
import name.huliqing.luoying.object.state.State;

/**
 * 用于获取角色的当前状态，注意：该类中获取的是角色所有经过状态列表处理后的
 * 动态属性．
 * @author huliqing
 */
public interface StateService extends StateNetwork {
    
    /**
     * 获取角色当前身上的所有状态，注意，这个方法只允许只读，不要随便修改列
     * 表中的信息。添加或删除状态需要通过接口
     * @see #removeState(name.huliqing.fighter.object.actor.Actor, java.lang.String) 
     * @see #addState(name.huliqing.fighter.object.actor.Actor, java.lang.String) 
     * @param actor
     * @return 
     */
    List<State> getStates(Entity actor);
    
    /**
     * 添加状态侦听器
     * @param actor
     * @param listener 
     */
    void addListener(Entity actor, StateListener listener);
    
    /**
     * 移除指定的状态侦听器，如果成功移除则返回true
     * @param actor
     * @param listener
     * @return 
     */
    boolean removeListener(Entity actor, StateListener listener);
}
