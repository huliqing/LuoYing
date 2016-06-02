/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 同步角色物品数量
 * @author huliqing
 */
@Serializable
public class MessItemSync extends MessBase {
    private long actorId;
    // 获得的物品ID
    private String itemId;
    // 同步物品总数
    private int total;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ItemService itemService = Factory.get(ItemService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return;
        }
        itemService.syncItemTotal(actor, itemId, total);
    }
}
