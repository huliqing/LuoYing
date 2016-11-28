/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.talk.Talk;

/**
 *
 * @author huliqing
 */
public interface GameNetwork extends Inject {
    
    void addMessage(String message, MessageType type);
   
    // remove20161126
//    /**
//     * 将当前服务端状态初始化到客户端。 一般在客户端连接到服务端后调用初始化
//     * 状态。
//     * @param client
//     */
//    void syncGameInitToClient(HostedConnection client);
    
    // remove201611xx
//    /**
//     * 同步“同步对象”
//     * @param object
//     * @param syncData
//     * @param reliable 
//     */
//    void syncObject(NetworkObject object, SyncData syncData, boolean reliable);
    
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
    
    /**
     * 设置是否打开角色的逻辑功能，这个范围比AutoAi要大。
     * @param entity
     * @param autoLogic 
     */
    void setAutoLogic(Entity entity, boolean autoLogic);
    
    /**
     * 打开角色的AI功能，
     * @param entity
     * @param autoAi 
     */
    void setAutoAi(Entity entity, boolean autoAi);
    
    /**
     * 设置entity的目标对象
     * @param entity
     * @param target 
     */
    void setTarget(Entity entity, long target);
    
    /**
     * 设置entity的跟随目标
     * @param entity
     * @param target 
     */
    void setFollow(Entity entity, long target);
    
    /**
     * 设置角色是否为不可或缺的,即死亡后不会被移除出场景
     * @param entity
     * @param essential 
     */
    void setEssential(Entity entity, boolean essential);
    
    // remove20161126
//    /**
//     * 设置角色是否为玩家类型的角色
//     * @param entity
//     * @param isPlayer 
//     */
//    void setPlayer(Entity entity, boolean isPlayer);
    
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
    
    /**
     * 设置角色的颜色
     * @param entity
     * @param color 
     */
    void setColor(Entity entity, ColorRGBA color);
    
    /**
     * 让Entity使用一个物品
     * @param entity
     * @param objectUniqueId 
     * @return  
     */
    boolean useObjectData(Entity entity, long objectUniqueId);
    
    /**
     * 删除角色身上指定的物品
     * @param entity
     * @param objectUniqueId
     * @param amount
     * @return 
     */
    boolean removeObjectData(Entity entity, long objectUniqueId, int amount);
    
    // ------------------------- from userCommand
    
    /**
     * 选择一个角色作为玩家角色
     * @param actorId 
     * @param actorName 玩家角色名称
     * @deprecated 不再使用
     */
    void selectPlayer(String actorId, String actorName);

    // remove20161126
//    /**
//     * 服务端上添加客户端玩家角色
//     * @param actor 
//     */
//    void addClientPlayer(Entity actor);
}
