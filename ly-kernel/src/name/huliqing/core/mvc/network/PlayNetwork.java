/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import name.huliqing.core.Inject;
import name.huliqing.core.data.GameData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.object.SyncData;
import name.huliqing.core.object.NetworkObject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.view.View;

/**
 *
 * @author huliqing
 */
public interface PlayNetwork extends Inject {
    
    /**
     * 添加一个角色到战场
     * @param actor
     */
    void addActor(Actor actor);
    
    /**
     * 把角色作为普通玩家角色类型载入（非Main player,即不一定是当前主场景中的角色。)
     * @param actor 
     */
    void addSimplePlayer(Actor actor);
    
    /**
     * 添加视图组件到界面
     * @deprecated 后续要重构并使用 addPlayObject代替 
     * @param view 
     */
    void addView(View view);
    
    /**
     * 从场景中移除物体
     * @param object
     */
    void removeObject(Object object);
    
    /**
     * 添加MESS
     * @param message
     * @param type 
     */
    void addMessage(String message, MessageType type);
    
    /**
     * 添加信息到指定的客户端
     * @param actor
     * @param message
     * @param type 
     */
    void addMessage(Actor actor, String message, MessageType type);
    
    /**
     * 移动角色到指定的地点
     * 也需要使用该方式进行移动。
     * @param actor
     * @param position 
     */
    void moveObject(Actor actor, Vector3f position);
    
    /**
     * 切换游戏
     * @param gameId 
     */
    void changeGame(String gameId);
    
    /**
     * 切换游戏
     * @param gameData 
     */
    void changeGame(GameData gameData);
    
    /**
     * 广播信息到所有客户端(即除服务端主机外的所有客户端)
     * @param message
     * @param type 
     */
    void addMessageOnlyClients(String message, MessageType type);
    
    /**
     * 让目标进行攻击。
     * @param actor 源角色
     * @param target 攻击目标
     */
    void attack(Actor actor, Actor target);
    
    /**
     * 将当前服务端状态初始化到客户端。 一般在客户端连接到服务端后调用初始化
     * 状态。
     * @param client
     */
    void syncGameInitToClient(HostedConnection client);
    
    /**
     * 同步“同步对象”
     * @param object
     * @param syncData
     * @param reliable 
     */
    void syncObject(NetworkObject object, SyncData syncData, boolean reliable);
    
}
