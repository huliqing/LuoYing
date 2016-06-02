/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import java.util.List;
import name.huliqing.fighter.Inject;
import name.huliqing.fighter.data.StateData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.StateListener;
import name.huliqing.fighter.object.state.State;

/**
 * 用于获取角色的当前状态，注意：该类中获取的是角色所有经过状态列表处理后的
 * 动态属性．
 * @author huliqing
 */
public interface StateService extends Inject{
    
    /**
     * 检查是否可以加指定的状态到角色身上。包含计算机率等。返回值是一个0.0~1.0
     * 之间的最终抵抗值resist。如果resist=1.0说明完全抵抗，这时不需要添加状
     * 态到角色身上。<br />
     * 注：如果指定的状态不存在，则返回一个大于或等于1.0的值来阻止继续添加状态。
     * @param actor
     * @param stateId
     * @return resist
     */
    float checkAddState(Actor actor, String stateId);
    
    /**
     * 给角色添加一个状态,注意：如果当前角色身上已经存在相同ID的状态，则旧的
     * 将会被移除，并添加新的。注：该方法并不一定保证能成功添加状态。如果
     * 角色对该状态刚好存在抵抗则可能添加状态失败！
     * @param actor 被添加状态的角色
     * @param state 状态ID
     * @param sourceActor 产生这个状态的源角色，如果不存在，则设置为null。
     */
    boolean addState(Actor actor, String stateId, Actor sourceActor);
    
    /**
     * 强制添加状态到角色身上,注：这个方法不会检查角色的抵抗值以及其它。
     * 一般在添加状态之前可以先调用 {@link #checkAddState(Actor, String) }
     * 来确认是否添加状态.
     * @param actor
     * @param stateId
     * @param resist 最终抵抗值，该值是一个对于状态发生作用的抵消值，取值0.0~1.0
     * 0表示无抵消作用，1.0表示完全抵消状态的作用。
     * @param sourceActor 产生这个状态的源角色，如果不存在，则设置为null。
     * @return 
     */
    void addStateForce(Actor actor, String stateId, float resist, Actor sourceActor);

    /**
     * 从角色身上删除指定ID的状态，如果存在多个相同ID的状态，则一并被移除。
     * @param actor
     * @param state 
     */
    boolean removeState(Actor actor, String stateId);
    
    /**
     * 从角色身上获得指定的状态,如果当前角色身上没有任何状态或找不到这个指
     * 定ID的状态则返回null.
     * @param actor
     * @param stateId
     * @return 
     */
    State findState(Actor actor, String stateId);
    
    /**
     * 清除角色当前身上的所有状态。
     * @param actor 
     */
    void clearStates(Actor actor);
    
    /**
     * 判断系统是否存在指定ID的状态,在给角色添加状态之前最好判断是否存在这样
     * 一个状态，否则添加状态时可能会报错。
     * @param stateId
     * @return 
     */
    boolean existsState(String stateId);
    
    /**
     * 判断角色当前是否存在指定的状态。
     * @param actor
     * @param stateId
     * @return 
     */
    boolean existsState(Actor actor, String stateId);
    
    /**
     * 获取角色当前身上的所有状态，注意，这个方法只允许只读，不要随便修改列
     * 表中的信息。添加或删除状态需要通过接口
     * @see #removeState(name.huliqing.fighter.object.actor.Actor, java.lang.String) 
     * @see #addState(name.huliqing.fighter.object.actor.Actor, java.lang.String) 
     * @param actor
     * @return 
     */
    List<StateData> getStates(Actor actor);
    
    /**
     * 添加状态侦听器
     * @param actor
     * @param listener 
     */
    void addListener(Actor actor, StateListener listener);
    
    /**
     * 移除指定的状态侦听器，如果成功移除则返回true
     * @param actor
     * @param listener
     * @return 
     */
    boolean removeListener(Actor actor, StateListener listener);
}
