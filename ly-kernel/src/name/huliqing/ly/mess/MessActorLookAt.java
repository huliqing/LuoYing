/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.entity.Entity;

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
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor != null) {
            Factory.get(ActorService.class).setLookAt(actor, pos);
        }
    }
    
    
}
