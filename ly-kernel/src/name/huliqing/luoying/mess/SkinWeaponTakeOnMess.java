/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 使用物品的指令
 * @author huliqing
 */
@Serializable
public class SkinWeaponTakeOnMess extends GameMess {
    
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
        // remove20161102,暂不进行安全判断
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        SkinNetwork skinNetwork = Factory.get(SkinNetwork.class);
//        Entity actor = playService.getEntity(actorId);
//        if (actor == null) {
//            return; // 找不到指定的角色
//        }
//        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
//        long clientActorId = cd != null ? cd.getEntityId() : -1;
//        // 使用物品的必须是客户端角色自身或者客户端角色的宠物
//        if (actor.getData().getUniqueId() == clientActorId
//                || actorService.getOwner(actor) == clientActorId) {
//            if (takeOn) {
//                skinNetwork.takeOnWeapon(actor);
//            } else {
//                skinNetwork.takeOffWeapon(actor);
//            }
//        }

        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor == null) {
            return; // 找不到指定的角色
        }
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
//        long clientActorId = cd != null ? cd.getEntityId() : -1;
        if (takeOn) {
            Factory.get(SkinNetwork.class).takeOnWeapon(actor);
        } else {
            Factory.get(SkinNetwork.class).takeOffWeapon(actor);
        }
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        PlayService playService = Factory.get(PlayService.class);
        SkinService skinService = Factory.get(SkinService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            if (takeOn) {
                // 如果是客户端，则这里使用强制takeOn,以保证与服务端同步。
                skinService.takeOnWeapon(actor);
            } else {
                // 如果是客户端，则这里使用强制takeOff,以保证与服务端同步。
                skinService.takeOffWeapon(actor);
            }
        }
    }
    
}
