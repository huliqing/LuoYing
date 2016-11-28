/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 直接同步角色的位置
 * @author huliqing
 */
@Serializable
public class ActorSetLocationMess extends GameMess {
    
    private long actorId = -1;
    private Vector3f location;
    
    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }
    
    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor == null) {
            return;
        }
        Factory.get(ActorService.class).setLocation(actor, location);
    }
}
