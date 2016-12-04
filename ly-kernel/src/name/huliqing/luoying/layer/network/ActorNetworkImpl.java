/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import name.huliqing.luoying.network.Network;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.mess.ActorPhysicsMess;
import name.huliqing.luoying.mess.ActorViewDirMess;
import name.huliqing.luoying.mess.ActorLookAtMess;
import name.huliqing.luoying.mess.ActorSetLocationMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class ActorNetworkImpl implements ActorNetwork{
    private final static Network NETWORK = Network.getInstance();
    private ActorService actorService;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
    }

    @Override
    public void setLocation(Entity actor, Vector3f location) {
        if (!NETWORK.isClient()) {
            ActorSetLocationMess mess = new ActorSetLocationMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setLocation(location);
            NETWORK.broadcast(mess);
            actorService.setLocation(actor, location);
        }
    }
    
    @Override
    public void setViewDirection(Entity actor, Vector3f viewDirection) {
        if (!NETWORK.isClient()) {
            ActorViewDirMess mess = new ActorViewDirMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setViewDir(viewDirection);
            NETWORK.broadcast(mess);
            actorService.setViewDirection(actor, viewDirection);
        }
    }

    @Override
    public void setPhysicsEnabled(Entity actor, boolean enabled) {
         if (!NETWORK.isClient()) {
            ActorPhysicsMess mess = new ActorPhysicsMess();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setEnabled(enabled);
            NETWORK.broadcast(mess);
            actorService.setPhysicsEnabled(actor, enabled); 
        }
    }

    @Override
    public void setLookAt(Entity actor, Vector3f position) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                ActorLookAtMess mess = new ActorLookAtMess();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setPos(position);
                NETWORK.broadcast(mess);
            }
            actorService.setLookAt(actor, position); 
        }
    }

    
}
