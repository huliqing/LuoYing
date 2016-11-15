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
import name.huliqing.luoying.object.entity.Entity;

/**
 * 直接同步角色的位置
 * @author huliqing
 */
@Serializable
public class MessActorTransformDirect extends MessBase {
    
    private long actorId = -1;
    private Vector3f location = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private Vector3f viewDirection = new Vector3f();
    
    public MessActorTransformDirect() {}

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

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection = viewDirection;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor == null) {
            return;
        }
        
        ActorService actorService = Factory.get(ActorService.class);
        actorService.setLocation(actor, location);
        actorService.setWalkDirection(actor, walkDirection);
        actorService.setViewDirection(actor, viewDirection);
    }
}
