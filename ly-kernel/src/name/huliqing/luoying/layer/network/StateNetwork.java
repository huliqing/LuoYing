/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.Inject;

/**
 *
 * @author huliqing
 */
public interface StateNetwork extends Inject {
    
//    /**
//     * 检查是否可以加指定的状态到角色身上。包含计算机率等。返回值是一个0.0~1.0
//     * 之间的最终抵抗值resist。如果resist=1.0说明完全抵抗，这时不需要添加状
//     * 态到角色身上。<br>
//     * 注：如果指定的状态不存在，则返回一个大于或等于1.0的值来阻止继续添加状态。
//     * @param actor
//     * @param stateId
//     * @return resist
//     */
//    float checkAddState(Entity actor, String stateId);
//    
//    /**
//     * 给角色添加一个状态,注意：如果当前角色身上已经存在相同ID的状态，则旧的
//     * 将会被移除，并添加新的。注：该方法并不一定保证能成功添加状态。如果
//     * 角色对该状态刚好存在抵抗则可能添加状态失败！
//     * @param actor 被添加状态的角色
//     * @param stateId 状态ID
//     * @param sourceActor 产生这个状态的源角色，如果不存在，则设置为null。
//     * @return 
//     */
//    boolean addState(Entity actor, String stateId, Entity sourceActor);
//    
//    /**
//     * 强制添加状态到角色身上,注：这个方法不会检查角色的抵抗值以及其它。
//     * 一般在添加状态之前可以先调用 {@link #checkAddState(Actor, String) }
//     * 来确认是否添加状态.
//     * @param actor
//     * @param stateId
//     * @param resist 最终抵抗值，该值是一个对于状态发生作用的抵消值，取值0.0~1.0
//     * 0表示无抵消作用，1.0表示完全抵消状态的作用。
//     * @param sourceActor 产生这个状态的源角色，如果不存在，则设置为null。 
//     */
//    void addStateForce(Entity actor, String stateId, float resist, Entity sourceActor);
//
//    /**
//     * 从角色身上删除指定ID的状态，如果存在多个相同ID的状态，则一并被移除。
//     * @param actor 
//     * @param stateId 
//     * @return  
//     */
//    boolean removeState(Entity actor, String stateId);
    
}
