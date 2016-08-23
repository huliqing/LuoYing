/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.data.StateData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.state.State;

/**
 * 监听角色状态的添加、删除等
 * @author huliqing
 */
public interface StateListener {
    
    /**
     * 该方法在状态添加到角色身上时触发,也就是在角色source被成功添加了状态后
     * 调用。
     * @param source 被添加了状态的角色
     * @param stateAdded 添加的状态
     */
    void onStateAdded(Actor source, State stateAdded);
    
    /**
     * 该方法在状态被成功从角色source身上移除时触发,即成功从角色成上移除后
     * 才调用。
     * @param source
     * @param stateRemoved 
     */
    void onStateRemoved(Actor source, State stateRemoved);
}
