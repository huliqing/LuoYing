/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.object.actor.Actor;

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
        PlayService playService = Factory.get(PlayService.class);
        Actor actor = playService.findActor(actorId);
        if (actor == null) {
            return;
        }
        
        actor.setLocation(location);
        actor.setWalkDirection(walkDirection);
        actor.setViewDirection(viewDirection);
    }
}
