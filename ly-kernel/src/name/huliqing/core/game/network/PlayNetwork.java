/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.network;

import com.jme3.network.HostedConnection;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.object.SyncData;
import name.huliqing.core.object.NetworkObject;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface PlayNetwork extends PlayService {
    
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
