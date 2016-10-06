/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import name.huliqing.ly.Inject;
import name.huliqing.ly.data.GameData;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.object.SyncData;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.view.View;

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
     * @deprecated 
     * 切换游戏
     * @param gameId 
     */
    void changeGame(String gameId);
    
    /**
     * @deprecated 
     * 切换游戏
     * @param gameData 
     */
    void changeGame(GameData gameData);
    
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
