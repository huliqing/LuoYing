/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.SceneService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.log.StateCode;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.ly.mess.ActionRunMess;
import name.huliqing.luoying.mess.EntityHitAttributeMess;
import name.huliqing.luoying.mess.EntityRemoveDataMess;
import name.huliqing.luoying.mess.EntityUseDataByIdMess;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.mess.ActorSpeakMess;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.mess.MessageMess;
import name.huliqing.ly.mess.PlaySkillMess;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkManager;

/**
 *
 * @author huliqing
 */
public class GameNetworkImpl implements GameNetwork {
//    private static final Logger LOG = Logger.getLogger(GameNetworkImpl.class.getName());
    private final Network network = Network.getInstance();
    
    private GameService gameService;
    private SceneService sceneService;
    private EntityNetwork entityNetwork;
    private PlayNetwork playNetwork;
    private SkillService skillService;
    private SkillNetwork skillNetwork;
    
    @Override
    public void inject() {
        gameService = Factory.get(GameService.class);
        sceneService = Factory.get(SceneService.class);
        entityNetwork = Factory.get(EntityNetwork.class);
        playNetwork = Factory.get(PlayNetwork.class);
        skillService = Factory.get(SkillService.class);
        skillNetwork = Factory.get(SkillNetwork.class);
    }

    @Override
    public void addMessage(String message, MessageType type) {
        if (network.isClient()) {
            return;
        }
        
        MessageMess mess = new MessageMess();
        mess.setMessage(message);
        mess.setType(type);
        network.broadcast(mess);
        
        gameService.addMessage(message, type);
    }

    @Override
    public void speak(Entity actor, String mess, float useTime) {
        if (network.isClient()) {
            // ignore
        } else {
            ActorSpeakMess mas = new ActorSpeakMess();
            mas.setActorId(actor.getData().getUniqueId());
            mas.setMess(mess);
            network.broadcast(mas);
            gameService.speak(actor, mess, useTime);
        }
    }
    
    @Override
    public void talk(Talk talk) {
        if (network.isClient()) {
            // ignore
        } else {
            talk.setNetwork(true);
            TalkManager.getInstance().startTalk(talk);
        }
    }
    
    @Override
    public void playRunToPos(Entity actor, Vector3f worldPos) {
        // 死亡后不能再移动
        if (gameService.isDead(actor)) {
            return;
        }
        
        if (network.isClient()) {
            ActionRunMess runAction = new ActionRunMess();
            runAction.setActorId(actor.getData().getUniqueId());
            runAction.setPos(worldPos);
            network.sendToServer(runAction);
        } else {
            
            // 不需要广播
            gameService.playRunToPos(actor, worldPos);
        }
    }
    
    // 发送一个属性修改的命令到服务端
    private void sendAttributeHitToServer(Entity entity, String attribute, Object value) {
        EntityHitAttributeMess mess = new EntityHitAttributeMess();
        mess.setEntityId(entity.getEntityId());
        mess.setAttribute(attribute);
        mess.setValue(value);
        network.sendToServer(mess);
    }

    @Override
    public void setLevel(Entity entity, int level) {
        entityNetwork.hitAttribute(entity, AttrConstants.LEVEL, level, null);
    }

    @Override
    public void setGroup(Entity entity, int group) {
        entityNetwork.hitAttribute(entity, AttrConstants.GROUP, group, null);
    }

    @Override
    public void setTeam(Entity entity, int team) {
        entityNetwork.hitAttribute(entity, AttrConstants.TEAM, team, null);
    }

    @Override
    public void setAutoLogic(Entity entity, boolean autoLogic) {
        entityNetwork.hitAttribute(entity, AttrConstants.AUTO_LOGIC, autoLogic, null);
    }
    
    @Override
    public void setAutoAi(Entity entity, boolean autoAi) {
        if (network.isClient()) {
            sendAttributeHitToServer(entity, AttrConstants.AUTO_AI, autoAi);
            return;
        }
        entityNetwork.hitAttribute(entity, AttrConstants.AUTO_AI, autoAi, null);
    }

    @Override
    public void setTarget(Entity entity, long target) {
        if (network.isClient()) {
            sendAttributeHitToServer(entity, AttrConstants.TARGET, target);
            return;
        }
        entityNetwork.hitAttribute(entity, AttrConstants.TARGET, target, null);
    }

    @Override
    public void setFollow(Entity entity, long target) {
        if (network.isClient()) {
            sendAttributeHitToServer(entity, AttrConstants.FOLLOW, target);
            return;
        }
        entityNetwork.hitAttribute(entity, AttrConstants.FOLLOW, target, null);
    }
    
    @Override
    public void setPartner(Entity entity, Entity partner) {
        setGroup(partner, gameService.getGroup(entity));
        setFollow(partner, entity.getEntityId());
    }

    @Override
    public void setEssential(Entity entity, boolean essential) {
        entityNetwork.hitAttribute(entity, AttrConstants.ESSENTIAL, essential, null);
    }

    @Override
    public void setName(Entity entity, String name) {
        entityNetwork.hitAttribute(entity, AttrConstants.NAME, name, null);
    }

    @Override
    public void setPlayer(Entity entity, boolean isPlayer) {
        entityNetwork.hitAttribute(entity, AttrConstants.PLAYER, isPlayer, null);
    }
    
    @Override
    public void kill(Entity entity) {
        entityNetwork.hitAttribute(entity, AttrConstants.HEALTH, 0, null);
    }
    
    @Override
    public void setLocation(Entity entity, Vector3f location) {
        entityNetwork.hitAttribute(entity, AttrConstants.LOCATION, location, null);
    }

    @Override
    public void setOnTerrain(Entity entity) {
        Vector3f loc = entity.getSpatial().getWorldTranslation();
        sceneService.getSceneHeight(loc.x, loc.z, loc);
        setLocation(entity, loc);
    }
    
    @Override
    public void setColor(Entity entity, ColorRGBA color) {
        entityNetwork.hitAttribute(entity, AttrConstants.COLOR, new Vector4f(color.r, color.g, color.b, color.a), null);
    }

    @Override
    public boolean useObjectData(Entity entity, long objectUniqueId) {
        if (network.isClient()) {
            EntityUseDataByIdMess mess = new EntityUseDataByIdMess();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectUniqueId(objectUniqueId);
            network.sendToServer(mess);
            return false;
        }
        return entityNetwork.useObjectData(entity, objectUniqueId);
    }

    @Override
    public boolean removeObjectData(Entity entity, long objectUniqueId, int amount) {
        if (network.isClient()) {
            EntityRemoveDataMess  mess = new EntityRemoveDataMess();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectId(objectUniqueId);
            mess.setAmount(amount);
            network.sendToServer(mess);
            return false;
        }
        return entityNetwork.removeObjectData(entity, objectUniqueId, amount);
    }
    
    @Override
    public void playSkill(Entity entity, String skill) {
        PlaySkillMess mess = new PlaySkillMess();
        mess.setEntityId(entity.getEntityId());
        mess.setSkillId(skill);
        
        // on client
        if (network.isClient()) {
            network.sendToServer(mess);
            return;
        }
        
        // on server
        network.broadcast(mess);
        gameService.playSkill(entity, skill);
    }
    
    
}
