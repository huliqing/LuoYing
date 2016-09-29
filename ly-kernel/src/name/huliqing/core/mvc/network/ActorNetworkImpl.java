/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.network.Network;
import com.jme3.math.Vector3f;
import name.huliqing.core.Factory;
import name.huliqing.core.view.talk.Talk;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mess.MessActorFollow;
import name.huliqing.core.mess.MessActorKill;
import name.huliqing.core.mess.MessActorPhysics;
import name.huliqing.core.mess.MessActorSetGroup;
import name.huliqing.core.mess.MessActorSetLevel;
import name.huliqing.core.mess.MessActorSetTarget;
import name.huliqing.core.mess.MessActorSpeak;
import name.huliqing.core.mess.MessActorTeam;
import name.huliqing.core.mess.MessActorViewDir;
import name.huliqing.core.mess.MessActorLookAt;
import name.huliqing.core.mess.MessAttributeNumberHit;
import name.huliqing.core.object.actor.Actor;

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
    public void speak(Actor actor, String mess, float useTime) {
        if (!NETWORK.isClient()) {
            //broadcast
            MessActorSpeak mas = new MessActorSpeak();
            mas.setActorId(actor.getData().getUniqueId());
            mas.setMess(mess);
            NETWORK.broadcast(mas);
            // local speak
            actorService.speak(actor, mess, useTime); 
        }
    }

    @Override
    public void talk(Talk talk) {
        talk.setNetwork(true);
        actorService.talk(talk); 
    }

    @Override
    public void kill(Actor actor) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorKill mess = new MessActorKill();
                mess.setKillActorId(actor.getData().getUniqueId());
                NETWORK.broadcast(mess);
            }
            actorService.kill(actor); 
        }
    }

    @Override
    public void setTarget(Actor actor, Actor target) {
        if (!NETWORK.isClient()) {
            MessActorSetTarget mess = new MessActorSetTarget();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            NETWORK.broadcast(mess);
            actorService.setTarget(actor, target); 
        }
    }
    
    @Override
    public void hitNumberAttribute(Actor target, Actor source, String hitAttrName, float hitValue) {
        if (!NETWORK.isClient()) {
            actorService.hitNumberAttribute(target, source, hitAttrName, hitValue);
            
            // 同步属性值
            if (NETWORK.hasConnections()) {
                MessAttributeNumberHit mess = new MessAttributeNumberHit();
                mess.setTargetActor(target.getData().getUniqueId());
                mess.setSourceActor(source.getData().getUniqueId());
                mess.setAttrName(hitAttrName);
                mess.setValue(hitValue);
                NETWORK.broadcast(mess);
            }
        }
    }

    @Override
    public void setLevel(Actor actor, int level) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorSetLevel mess = new MessActorSetLevel();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setLevel(level);
                NETWORK.broadcast(mess);
            }
            actorService.setLevel(actor, level);
        }
    }
    
    @Override
    public void setGroup(Actor actor, int group) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorSetGroup mess = new MessActorSetGroup();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setGroup(group);
                NETWORK.broadcast(mess);
            }
            actorService.setGroup(actor, group);
        }
    }

    @Override
    public void setTeam(Actor actor, int team) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorTeam mess = new MessActorTeam();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTeamId(team);
                NETWORK.broadcast(mess);
            }
            actorService.setTeam(actor, team);
        }
    }

    @Override
    public void setFollow(Actor actor, long targetId) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorFollow mess = new MessActorFollow();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTargetId(targetId);
                NETWORK.broadcast(mess);
            }
            actorService.setFollow(actor, targetId);
        }
    }

    @Override
    public void setViewDirection(Actor actor, Vector3f viewDirection) {
        if (!NETWORK.isClient()) {
            MessActorViewDir mess = new MessActorViewDir();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setViewDir(viewDirection);
            NETWORK.broadcast(mess);
            actorService.setViewDirection(actor, viewDirection);
        }
    }

    @Override
    public void setPhysicsEnabled(Actor actor, boolean enabled) {
         if (!NETWORK.isClient()) {
            MessActorPhysics mess = new MessActorPhysics();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setEnabled(enabled);
            NETWORK.broadcast(mess);
            actorService.setPhysicsEnabled(actor, enabled); 
        }
    }

    @Override
    public void setLookAt(Actor actor, Vector3f position) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorLookAt mess = new MessActorLookAt();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setPos(position);
                NETWORK.broadcast(mess);
            }
            actorService.setLookAt(actor, position); 
        }
    }

    
}
