/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import com.jme3.animation.LoopMode;
import com.jme3.math.ColorRGBA;
import name.huliqing.core.network.Network;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ActorData;
import name.huliqing.core.data.AttributeData;
import name.huliqing.core.enums.HurtFace;
import name.huliqing.core.enums.Sex;
import name.huliqing.core.view.talk.Talk;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mess.MessActorApplyXp;
import name.huliqing.core.mess.MessActorFollow;
import name.huliqing.core.mess.MessActorKill;
import name.huliqing.core.mess.MessActorPhysics;
import name.huliqing.core.mess.MessActorSetGroup;
import name.huliqing.core.mess.MessActorSetLevel;
import name.huliqing.core.mess.MessActorSetTarget;
import name.huliqing.core.mess.MessActorSpeak;
import name.huliqing.core.mess.MessActorTeam;
import name.huliqing.core.mess.MessActorViewDir;
import name.huliqing.core.mess.MessAttributeSync;
import name.huliqing.core.mess.MessActorLookAt;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.actor.ActorListener;

/**
 *
 * @author huliqing
 */
public class ActorNetworkImpl implements ActorNetwork{
    private final static Network NETWORK = Network.getInstance();
    private ActorService actorService;
    private AttributeService attributeService;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
        attributeService = Factory.get(AttributeService.class);
    }

    @Override
    public Actor loadActor(String actorId) {
        return actorService.loadActor(actorId);
    }

    @Override
    public Actor loadActor(ActorData actorData) {
        return actorService.loadActor(actorData);
    }

    @Override
    public String createRandomName(Sex sex) {
        return actorService.createRandomName(sex); 
    }

    @Override
    public boolean hasObstacleActor(Actor self, List<Actor> actors) {
        return actorService.hasObstacleActor(self, actors); 
    }

    @Override
    public HurtFace checkFace(Spatial self, Actor target) {
        return actorService.checkFace(self, target); 
    }

    @Override
    public Actor findNearestEnemyExcept(Actor actor, float maxDistance, Actor except) {
        return actorService.findNearestEnemyExcept(actor, maxDistance, except); 
    }

    @Override
    public List<Actor> findNearestEnemies(Actor actor, float maxDistance, List<Actor> store) {
        return actorService.findNearestEnemies(actor, maxDistance, store); 
    }

    @Override
    public List<Actor> findNearestFriendly(Actor actor, float maxDistance, List<Actor> store) {
        return actorService.findNearestFriendly(actor, maxDistance, store); 
    }

    @Override
    public List<Actor> findNearestActors(Actor actor, float maxDistance, List<Actor> store) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Actor> findNearestActors(Actor actor, float maxDistance, float angle, List<Actor> store) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSkillUpdated(Actor actor, long timePoint) {
        return actorService.isSkillUpdated(actor, timePoint); 
    }

    @Override
    public float getHeight(Actor actor) {
        return actorService.getHeight(actor); 
    }

    @Override
    public void setPartner(Actor owner, Actor partner) {
        throw new UnsupportedOperationException();
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
    public Vector3f getLocalToWorld(Actor actor, Vector3f localPos, Vector3f store) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean checkAndLoadAnim(Actor actor, String animName) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void reborn(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.暂不实现");
//        actorService.reborn(actor); 
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
    public Actor getTarget(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAutoAi(Actor actor, boolean autoAi) {
        throw new UnsupportedOperationException("Not supported yet.不需要实现");
    }

    @Override
    public boolean isAutoAi(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDead(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnemy(Actor actor, Actor target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setColor(Actor actor, ColorRGBA color) {
        throw new UnsupportedOperationException("暂不实现");
    }
    
    @Override
    public void hitAttribute(Actor target, Actor source, String hitAttribute, float hitValue) {
        if (!NETWORK.isClient()) {
            actorService.hitAttribute(target, source, hitAttribute, hitValue);
            
            // 同步生命值
            AttributeData attrData = attributeService.getAttributeData(target, hitAttribute);
            if (NETWORK.hasConnections() && attrData != null) {
                MessAttributeSync mess = new MessAttributeSync();
                mess.setActorId(target.getData().getUniqueId());
                mess.setAttribute(attrData.getId());
                mess.setLevelValue(attrData.getLevelValue());
                mess.setStaticValue(attrData.getStaticValue());
                mess.setDynamicValue(attrData.getDynamicValue());
                NETWORK.broadcast(mess);
            }
        }
    }

    @Override
    public int getLevel(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public int getXpReward(Actor attacker, Actor dead) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int applyXp(Actor actor, int xp) {
        if (!NETWORK.isClient()) {
            if (NETWORK.hasConnections()) {
                MessActorApplyXp mess = new MessActorApplyXp();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setXp(xp);
                NETWORK.broadcast(mess);
            }
            
            return actorService.applyXp(actor, xp);
        }
        return 0;
    }

    @Override
    public int getNextLevelXp(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAutoDetect(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAutoDetect(Actor actor, boolean autoDetect) {
        // 客户端是不需要该参数的，所以不需要实现在这里
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isMoveable(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getViewDistance(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addActorListener(Actor actor, ActorListener actorListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeActorListener(Actor actor, ActorListener actorListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // remove20160815
//    @Override
//    public void addSkillListener(Actor actor, SkillListener skillListener) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public void setName(Actor actor, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    @Override
    public String getName(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getGroup(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public int getTeam(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Sex getSex(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEssential(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEssential(Actor actor, boolean essential) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRace(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOwner(Actor actor, long ownerId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getOwner(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public long getFollow(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // remove20160817
//    @Override
//    public int getTalentPoints(Actor actor) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public void syncTransform(Actor actor, Vector3f location, Vector3f viewDirection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void syncAnimation(Actor actor, String[] channelIds, String[] animNames, byte[] loopModes, float[] speeds, float[] times) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLife(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void setLocation(Actor actor, Vector3f location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public boolean isPhysicsEnabled(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector3f getViewDirection(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setWalkDirection(Actor actor, Vector3f walkDirection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector3f getWalkDirection(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playAnim(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setChannelLock(Actor actor, boolean locked, String... channelIds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restoreAnimation(Actor actor, String animName, LoopMode loop, float useTime, float startTime, String... channelIds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean reset(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPlayer(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPlayer(Actor actor, boolean player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getViewAngle(Actor actor, Vector3f position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getMass(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isKinematic(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setKinematic(Actor actor, boolean kinematic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector3f getLocation(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void resetToAnimationTime(Actor actor, String animation, float timePoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float distance(Actor actor, Vector3f position) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float distance(Actor actor, Actor target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float distanceSquared(Actor actor, Actor target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
