/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.HandlerNetwork;
import name.huliqing.fighter.game.service.HandlerService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.game.ConnData;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 使用物品的指令
 * @author huliqing
 */
@Serializable
public class MessItemUse extends MessBase {
    
    // 角色id
    private long actorId;
    // 物品的ID
    private String itemId;

    /**
     * 获取使用物品的角色
     * @return 
     */
    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    /**
     * 获取被使用的物品
     * @return 
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * 设置被使用的物品
     * @param itemId 
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        PlayService playService = Factory.get(PlayService.class);
        HandlerNetwork handlerNetwork = Factory.get(HandlerNetwork.class);
        
       // remove20160615
//        Long clientActorId = source.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
        
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Long clientActorId = cd != null ? cd.getActorId() : null;
        
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return; // 找不到指定的角色或者角色不是客户端所控制的。
        }
        // 使用物品的必须是客户端角色自身或者客户端角色的宠物
        if (actor.getData().getUniqueId() == clientActorId
                || actor.getData().getOwnerId() == clientActorId) {
            handlerNetwork.useObject(actor, itemId); 
        }
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        HandlerService handlerService = Factory.get(HandlerService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            handlerService.useForce(actor, itemId); 
        }
    }
    
}
