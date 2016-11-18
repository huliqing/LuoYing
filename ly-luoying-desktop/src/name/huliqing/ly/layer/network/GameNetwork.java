/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import name.huliqing.luoying.Inject;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;

/**
 *
 * @author huliqing
 */
public interface GameNetwork extends Inject {
    
    void addMessage(String message, MessageType type);
    
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
    
    /**
     * 让角色说话
     * @param actor
     * @param mess
     * @param useTime 
     */
    void speak(Entity actor, String mess, float useTime);
    
    /**
     * 执行一个谈话
     * @param talk 
     */
    void talk(Talk talk);
    
    // remove20161106
//    /**
//     * 让actor跟随目标target角色
//     * @param actor
//     * @param targetId 目标ID,如果为小于或等于0的值则表示清除跟随
//     */
//    void follow(Entity actor, long targetId);

    /**
     * 让角色走到目标位置
     * @param actor
     * @param worldPos 
     */
    void playRunToPos(Entity actor, Vector3f worldPos);
    
    void setLevel(Entity entity, int level);
    void setGroup(Entity entity, int group);
    void setTeam(Entity entity, int team);
    void setAutoLogic(Entity entity, boolean autoLogic);
    void setTarget(Entity entity, long target);
    void setFollow(Entity entity, long target);
    void setEssential(Entity entity, boolean essential);
    void setColor(Entity entity, ColorRGBA color);
    /**
     * 把partner设置为entity的同伴
     * @param entity
     * @param partner 
     */
    void setPartner(Entity entity, Entity partner);
    
    /**
     * 杀死一个角色
     * @param entity 
     */
    void kill(Entity entity);
    
    // ------------------------- from userCommand
    
    /**
     * 选择一个角色作为玩家角色
     * @param actorId 
     * @param actorName 玩家角色名称
     * @deprecated 不再使用
     */
    void selectPlayer(String actorId, String actorName);

    /**
     * 添加普通的玩家类角色。
     * @param actor 
     */
    void addSimplePlayer(Entity actor);
}
