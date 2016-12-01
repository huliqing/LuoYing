/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.network.HostedConnection;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * 
 * @author huliqing
 */
public interface PlayNetwork extends Inject {
    
    /**
     * 向当前游戏主场景添加物体.(非GUI场景）
     * @param entity 
     */
    void addEntity(Entity entity);
    
    /**
     * 向当前游戏的Gui场景中添加物体
     * @param entity 
     */
    void addGuiEntity(Entity entity);
    
    /**
     * 向指定的场景添加物体。
     * @param scene
     * @param entity 
     */
    void addEntity(Scene scene, Entity entity);
    
    /**
     * 向指定的客户端添加场景实体，不进行广播，也不在本地服务端中添加,一般用于向客户端初始化场景时使用.
     * @param conn
     * @param entity 
     */
    void addEntityToClient(HostedConnection conn, Entity entity);
    
    /**
     * 从指定的场景中移除物体
     * @param entity 
     */
    void removeEntity(Entity entity);
    
    /**
     * 让目标进行攻击。
     * @param actor 源角色
     * @param target 攻击目标
     */
    void attack(Entity actor, Entity target);
    
    /**
     * 更新服务端和客户端的随机种子以重新生成随机数。服务端可以定时调用这个方法来更新服务端和客户端的随机数。
     */
    void updateRandomSeed();
    
}
