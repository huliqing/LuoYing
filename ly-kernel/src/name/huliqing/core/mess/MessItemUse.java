/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.ItemNetwork;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;

/**
 * 使用物品
 * @author huliqing
 */
@Serializable
public class MessItemUse extends MessBase {
    
    private long actorId;
    private String itemId;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
        if (actor == null) {
            return;
        }
        Factory.get(ItemNetwork.class).useItem(actor, itemId);
    }

    @Override
    public void applyOnClient() {
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
        if (actor == null) {
            return;
        }
        Factory.get(ItemService.class).useItem(actor, itemId);
    }
    
}
