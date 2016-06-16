/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.network;

import com.jme3.math.ColorRGBA;
import name.huliqing.fighter.game.state.lan.Network;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ActorData;
import name.huliqing.fighter.data.AttributeData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.HurtFace;
import name.huliqing.fighter.enums.Sex;
import name.huliqing.fighter.game.dao.ItemDao;
import name.huliqing.fighter.manager.talk.Talk;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.mess.MessActorApplyXp;
import name.huliqing.fighter.game.mess.MessActorFollow;
import name.huliqing.fighter.game.mess.MessActorKill;
import name.huliqing.fighter.game.mess.MessActorPhysics;
import name.huliqing.fighter.game.mess.MessActorSetGroup;
import name.huliqing.fighter.game.mess.MessActorSetLevel;
import name.huliqing.fighter.game.mess.MessActorSetTarget;
import name.huliqing.fighter.game.mess.MessActorSpeak;
import name.huliqing.fighter.game.mess.MessActorTeam;
import name.huliqing.fighter.game.mess.MessActorViewDir;
import name.huliqing.fighter.game.mess.MessAttributeSync;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorListener;
import name.huliqing.fighter.object.actor.SkillListener;

/**
 *
 * @author huliqing
 */
public class ActorNetworkImpl implements ActorNetwork{
    private final static Network network = Network.getInstance();
    private ActorService actorService;
    private AttributeService attributeService;
    private ItemDao actorDao;
    
    @Override
    public void inject() {
        actorService = Factory.get(ActorService.class);
        attributeService = Factory.get(AttributeService.class);
        actorDao = Factory.get(ItemDao.class);
    }

    @Override
    public Actor loadActor(String actorId) {
        return actorService.loadActor(actorId);
    }

    @Override
    public Actor loadActor(ActorData actorData) {
        return actorService.loadActor(actorData);
    }

     // remove 0221
//    @Override
//    public boolean rewardItem(Actor actor, String objectId, int count) {
//        if (!network.isClient()) {
//            actorService.rewardItem(actor, objectId, count); 
//            
//            // 同步物品数量
//            ProtoData data = actorDao.getItemExceptSkill(actor, objectId);
//            MessSCSyncItemGet messSyn = new MessSCSyncItemGet();
//            messSyn.setActorId(actor.getData().getUniqueId());
//            messSyn.setItemId(objectId);
//            messSyn.setAddCount(count);
//            messSyn.setSyncTotal(data != null ? data.getTotal() : 0);
//            network.broadcast(messSyn);
//            return true;
//        }
//        return false;
//    }

    @Override
    public String createRandomName(Sex sex) {
        return actorService.createRandomName(sex); 
    }

    // remove20160504
//    @Override
//    public boolean hasObstacle(Actor actor) {
////        return actorService.hasObstacle(actor); 
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public boolean hasObstacleActor(Actor self, List<Actor> actors) {
        return actorService.hasObstacleActor(self, actors); 
    }

    @Override
    public ProtoData getItem(Actor actor, String objectId) {
        return actorService.getItem(actor, objectId); 
    }

    @Override
    public List<ProtoData> getItems(Actor actor, List<ProtoData> store) {
        return actorService.getItems(actor, store);
    }

    @Override
    public HurtFace checkFace(Spatial self, Actor target) {
        return actorService.checkFace(self, target); 
    }

    @Override
    public Actor getActor(Spatial spatial) {
        return actorService.getActor(spatial); 
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
        if (!network.isClient()) {
            //broadcast
            MessActorSpeak mas = new MessActorSpeak();
            mas.setActorId(actor.getData().getUniqueId());
            mas.setMess(mess);
            network.broadcast(mas);
            
            // local speak
            actorService.speak(actor, mess, useTime); 
        }
    }

    @Override
    public void talk(Talk talk) {
        talk.setNetwork(true);
        actorService.talk(talk); 
    }

    // remove20151230
//    @Override
//    public void clearState(Actor actor) {
//        actorService.clearState(actor); 
//    }

    @Override
    public Vector3f getLocalToWorld(Actor actor, Vector3f localPos, Vector3f store) {
//        return actorService.getLocalToWorld(actor, localPos, store); 
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean checkAndLoadAnim(Actor actor, String animName) {
//        return actorService.checkAndLoadAnim(actor, animName); // remove20151210
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void kill(Actor actor) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorKill mess = new MessActorKill();
                mess.setKillActorId(actor.getData().getUniqueId());
                network.broadcast(mess);
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
        if (!network.isClient()) {
            MessActorSetTarget mess = new MessActorSetTarget();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            network.broadcast(mess);
            
            actorService.setTarget(actor, target); 
        }
    }

    @Override
    public Actor getTarget(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return actorService.getTarget(actor); 
    }

    @Override
    public void setAutoAi(Actor actor, boolean autoAi) {
        throw new UnsupportedOperationException("Not supported yet.不需要实现");
    }

    @Override
    public boolean isAutoAi(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return actorService.isAutoAi(actor);
    }

    @Override
    public boolean isDead(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return actorService.isDead(actor); 
    }

    @Override
    public boolean isEnemy(Actor actor, Actor target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // remove 0221
//    @Override
//    public void itemSynTotal(Actor actor, String itemId, int total) {
//        throw new UnsupportedOperationException("Not supported yet.");
////        actorService.itemSynTotal(actor, itemId, total);
//    }

    @Override
    public void setPhysics(Actor actor, boolean enabled) {
        if (!network.isClient()) {
            MessActorPhysics mess = new MessActorPhysics();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setEnabled(enabled);
            network.broadcast(mess);
            
            actorService.setPhysics(actor, enabled); 
        }
    }

    @Override
    public void setViewDirection(Actor actor, Vector3f viewDirection) {
        if (!network.isClient()) {
            MessActorViewDir mess = new MessActorViewDir();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setViewDir(viewDirection);
            network.broadcast(mess);
            
            actorService.setViewDirection(actor, viewDirection);
        }
    }

    @Override
    public void setColor(Actor actor, ColorRGBA color) {
        throw new UnsupportedOperationException("暂不实现");
    }
    
    @Override
    public void hitAttribute(Actor target, Actor source, String hitAttribute, float hitValue) {
        if (!network.isClient()) {
            actorService.hitAttribute(target, source, hitAttribute, hitValue);
            
            // 同步生命值
            AttributeData attrData = attributeService.getAttributeData(target, hitAttribute);
            if (network.hasConnections() && attrData != null) {
                MessAttributeSync mess = new MessAttributeSync();
                mess.setActorId(target.getData().getUniqueId());
                mess.setAttribute(attrData.getId());
                mess.setLevelValue(attrData.getLevelValue());
                mess.setStaticValue(attrData.getStaticValue());
                mess.setDynamicValue(attrData.getDynamicValue());
                network.broadcast(mess);
            }
        }
    }

    @Override
    public int getLevel(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLevel(Actor actor, int level) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorSetLevel mess = new MessActorSetLevel();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setLevel(level);
                network.broadcast(mess);
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
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorApplyXp mess = new MessActorApplyXp();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setXp(xp);
                network.broadcast(mess);
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

    @Override
    public void addSkillListener(Actor actor, SkillListener skillListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeSkillListener(Actor actor, SkillListener skillListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorSetGroup mess = new MessActorSetGroup();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setGroup(group);
                network.broadcast(mess);
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
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorTeam mess = new MessActorTeam();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTeamId(team);
                network.broadcast(mess);
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
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessActorFollow mess = new MessActorFollow();
                mess.setActorId(actor.getData().getUniqueId());
                mess.setTargetId(targetId);
                network.broadcast(mess);
            }
            
            actorService.setFollow(actor, targetId);
        }
        
    }

    public long getFollow(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTalentPoints(Actor actor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
    
}
