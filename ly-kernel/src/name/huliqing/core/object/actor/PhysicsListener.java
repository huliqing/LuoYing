/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import com.jme3.bullet.collision.PhysicsCollisionEvent;

/**
 * 监听目标角色与物体的碰撞
 * @author huliqing
 */
public interface PhysicsListener {
    
    /**
     * 当角色source与目标other发生碰撞时调用。
     * @param source 碰撞的当前角色
     * @param other 碰撞的另一个物体
     * @param event
     */
    void collision(Actor source, Object other, PhysicsCollisionEvent event);
}
