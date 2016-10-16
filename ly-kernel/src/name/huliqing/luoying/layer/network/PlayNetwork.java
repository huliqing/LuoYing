/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

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
     * 向指定的场景添加物体
     * @param scene
     * @param entity 
     */
    void addEntity(Scene scene, Entity entity);
    
    /**
     * 从指定的场景中移除物体
     * @param entity 
     */
    void removeEntity(Entity entity);
    
    // --------------------------------------------------------------------------------------------------------------------------------
//    
//    /**
//     * @deprecated 
//     * 添加一个角色到战场
//     * @param actor
//     */
//    void addActor(Actor actor);
//    
//    /**
//     * @deprecated 
//     * 把角色作为普通玩家角色类型载入（非Main player,即不一定是当前主场景中的角色。)
//     * @param actor 
//     */
//    void addSimplePlayer(Actor actor);
//    
//    /**
//     * @deprecated 
//     * 添加视图组件到界面
//     * @deprecated 后续要重构并使用 addPlayObject代替 
//     * @param view 
//     */
//    void addView(View view);
//    
//    /**
//     * @deprecated 
//     * 从场景中移除物体
//     * @param object
//     */
//    void removeObject(Object object);
//    
//    /**
//     * 添加MESS
//     * @param message
//     * @param type 
//     */
//    void addMessage(String message, MessageType type);
//    
//    /**
//     * 添加信息到指定的客户端
//     * @param actor
//     * @param message
//     * @param type 
//     */
//    void addMessage(Actor actor, String message, MessageType type);
//    
//    /**
//     * @deprecated 
//     * 切换游戏
//     * @param gameId 
//     */
//    void changeGame(String gameId);
//    
//    /**
//     * @deprecated 
//     * 切换游戏
//     * @param gameData 
//     */
//    void changeGame(GameData gameData);
    
    /**
     * 让目标进行攻击。
     * @param actor 源角色
     * @param target 攻击目标
     */
    void attack(Entity actor, Entity target);
    
//    /**
//     * 将当前服务端状态初始化到客户端。 一般在客户端连接到服务端后调用初始化
//     * 状态。
//     * @param client
//     */
//    void syncGameInitToClient(HostedConnection client);
    
//    /**
//     * 同步“同步对象”
//     * @param object
//     * @param syncData
//     * @param reliable 
//     */
//    void syncObject(NetworkObject object, SyncData syncData, boolean reliable);
    
}
