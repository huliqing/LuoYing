/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.SkinNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.data.ConnData;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.save.ClientData;

/**
 * 使用物品的指令
 * @author huliqing
 */
@Serializable
public class MessSkinWeaponTakeOn extends MessBase {
    
    private long actorId;
    private boolean takeOn; // true=takeOn, false=takeOff

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

    public boolean isTakeOn() {
        return takeOn;
    }

    /**
     * 设置是取武器还是摘武器。
     * @param takeOn 取=true/摘=false
     */
    public void setTakeOn(boolean takeOn) {
        this.takeOn = takeOn;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        PlayService playService = Factory.get(PlayService.class);
        SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return; // 找不到指定的角色
        }
        ClientData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        long clientActorId = cd != null ? cd.getActorId() : -1;
        
        // 使用物品的必须是客户端角色自身或者客户端角色的宠物
        if (actor.getData().getUniqueId() == clientActorId
                || actor.getData().getOwnerId() == clientActorId) {
            if (takeOn) {
                skinNetwork.takeOnWeapon(actor, false);
            } else {
                skinNetwork.takeOffWeapon(actor, false);
            }
        }
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        SkinService skinService = Factory.get(SkinService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            if (takeOn) {
                // 如果是客户端，则这里使用强制takeOn,以保证与服务端同步。
                skinService.takeOnWeapon(actor, true);
            } else {
                // 如果是客户端，则这里使用强制takeOff,以保证与服务端同步。
                skinService.takeOffWeapon(actor, true);
            }
        }
    }
    
}
