/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
public interface ActorNetwork extends Inject {
    
    /**
     * 打开或关闭角色的物理功能
     * @param actor
     * @param enabled 
     */
    void setPhysicsEnabled(Entity actor, boolean enabled);

    /**
     * 设置角色位置
     * @param actor
     * @param location
     */
    void setLocation(Entity actor, Vector3f location);
    
    /**
     * 设置角色视角方向
     * @param actor
     * @param viewDirection 
     */
    void setViewDirection(Entity actor, Vector3f viewDirection);
    
    /**
     * 让角色看向指定<b>位置</b>(非方向)
     * @param actor
     * @param position 
     */
    void setLookAt(Entity actor, Vector3f position);
    
}
