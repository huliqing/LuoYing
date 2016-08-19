/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.actor.Actor;

/**
 * 朝向
 * @author huliqing
 */
@Serializable
public class MessActorLookAt extends MessBase {
 
    private long actorId;
    private Vector3f pos;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    /**
     * 看向的目标位置，注意：是位置，不是方向
     * @return 
     */
    public Vector3f getPos() {
        return pos;
    }

    /**
     * 设置看向的目标位置，注意：是位置，不是方向
     * @param pos 
     */
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    @Override
    public void applyOnClient() {
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
        if (actor != null) {
            Factory.get(ActorService.class).setLookAt(actor, pos);
        }
    }
    
    
}
