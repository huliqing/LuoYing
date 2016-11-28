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

//    @Override
//    public void speak(Entity actor, String mess, float useTime) {
//        if (!NETWORK.isClient()) {
//            //broadcast
//            MessActorSpeak mas = new MessActorSpeak();
//            mas.setActorId(actor.getData().getUniqueId());
//            mas.setMess(mess);
//            NETWORK.broadcast(mas);
//            // local speak
//            actorService.speak(actor, mess, useTime); 
//        }
//    }
//
//    @Override
//    public void talk(Talk talk) {
//        talk.setNetwork(true);
//        actorService.talk(talk); 
//    }
//
//    @Override
//    public void kill(Entity actor) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessActorKill mess = new MessActorKill();
//                mess.setKillActorId(actor.getData().getUniqueId());
//                NETWORK.broadcast(mess);
//            }
//            actorService.kill(actor); 
//        }
//    }
//
//    @Override
//    public void setTarget(Entity actor, Entity target) {
//        MessActorSetTarget mess = new MessActorSetTarget();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
//        
//        // client
//        if (NETWORK.isClient()) {
//            NETWORK.sendToServer(mess);
//            return;
//        }
//        
//        // server
//        NETWORK.broadcast(mess);
//        actorService.setTarget(actor, target); 
//    }
//    
//    @Override
//    public void hitNumberAttribute(Entity target, Entity source, String hitAttrName, float hitValue) {
//        if (!NETWORK.isClient()) {
//            actorService.hitNumberAttribute(target, source, hitAttrName, hitValue);
//            
//            // 同步属性值
//            if (NETWORK.hasConnections()) {
//                MessEntityAttributeApply mess = new MessEntityAttributeApply();
//                mess.setTarget(target.getData().getUniqueId());
//                mess.setSource(source.getData().getUniqueId());
//                mess.setAttributeName(hitAttrName);
//                mess.setApplyValue(hitValue);
//                NETWORK.broadcast(mess);
//            }
//        }
//    }
//
//    @Override
//    public void setLevel(Entity actor, int level) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessActorSetLevel mess = new MessActorSetLevel();
//                mess.setActorId(actor.getData().getUniqueId());
//                mess.setLevel(level);
//                NETWORK.broadcast(mess);
//            }
//            actorService.setLevel(actor, level);
//        }
//    }
//    
//    @Override
//    public void setGroup(Entity actor, int group) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessActorSetGroup mess = new MessActorSetGroup();
//                mess.setActorId(actor.getData().getUniqueId());
//                mess.setGroup(group);
//                NETWORK.broadcast(mess);
//            }
//            actorService.setGroup(actor, group);
//        }
//    }
//    
//    @Override
//    public void setTeam(Entity actor, int team) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessActorTeam mess = new MessActorTeam();
//                mess.setActorId(actor.getData().getUniqueId());
//                mess.setTeamId(team);
//                NETWORK.broadcast(mess);
//            }
//            actorService.setTeam(actor, team);
//        }
//    }
//    
//    @Override
//    public void setFollow(Entity actor, long targetId) {
//        MessActorFollow mess = new MessActorFollow();
//        mess.setActorId(actor.getData().getUniqueId());
//        mess.setTargetId(targetId);
//        if (NETWORK.isClient()) {
//            NETWORK.sendToServer(mess);
//        } else {
//            NETWORK.broadcast(mess);
//            actorService.setFollow(actor, targetId);
//        }
//    }
    
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
