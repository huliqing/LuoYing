/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.entity.Entity;

/**
 * 打开或关闭角色的physics
 * @author huliqing
 */
@Serializable
public class MessActorPhysics extends MessBase {
    
    private long actorId;
    private boolean enabled;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
//            actor.setEnabled(enabled); // remove0813
            actorService.setPhysicsEnabled(actor, enabled);
        }
    }
    
}
