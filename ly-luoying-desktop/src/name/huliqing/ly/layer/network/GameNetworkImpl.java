/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.HostedConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.ly.mess.MessActionRun;
import name.huliqing.luoying.mess.MessEntityAdd;
import name.huliqing.luoying.mess.MessEntityHitAttribute;
import name.huliqing.luoying.mess.MessEntityRemoveData;
import name.huliqing.luoying.mess.MessEntityUseDataById;
import name.huliqing.luoying.mess.MessPlayActorSelect;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.mess.MessActorSpeak;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.mess.MessMessage;
import name.huliqing.ly.object.game.SimpleRpgGame;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkManager;

/**
 *
 * @author huliqing
 */
public class GameNetworkImpl implements GameNetwork {

    private static final Logger LOG = Logger.getLogger(GameNetworkImpl.class.getName());
    private final static Network network = Network.getInstance();
    
    private GameService gameService;
//    private EntityService entityService;
//    private ActorService actorService;
//    private ActionService actionService;
//    private ActorNetwork actorNetwork;
    private EntityNetwork entityNetwork;
    private PlayNetwork playNetwork;
    private PlayService playService;
    
    @Override
    public void inject() {
//        entityService = Factory.get(EntityService.class);
        gameService = Factory.get(GameService.class);
//        actorService = Factory.get(ActorService.class);
//        actionService = Factory.get(ActionService.class);
//        actorNetwork = Factory.get(ActorNetwork.class);
        entityNetwork = Factory.get(EntityNetwork.class);
        playNetwork = Factory.get(PlayNetwork.class);
        playService = Factory.get(PlayService.class);
    }

    @Override
    public void addMessage(String message, MessageType type) {
        if (network.isClient()) {
            return;
        }
        
        MessMessage mess = new MessMessage();
        mess.setMessage(message);
        mess.setType(type);
        network.broadcast(mess);
        
        gameService.addMessage(message, type);
    }

    @Override
    public void syncGameInitToClient(HostedConnection client) {
         if (network.isClient()) 
            return;
        
        // 注意：同步当前场景中的对象时不要一起发送，
        // 这会导致溢出异常（当场景中角色或物体太多时)
        // 这些命令是有序的，不用担心角色同步问题
        
        // 同步所有角色
        SimpleRpgGame rpgGame = (SimpleRpgGame) playService.getGame();
        Scene scene = rpgGame.getScene();
        List<Entity> entities = rpgGame.getScene().getEntities(Entity.class, null);
        for (Entity entity : entities) {
            entity.updateDatas();
            MessEntityAdd mess = new MessEntityAdd();
            mess.setEntityData(entity.getData());
            mess.setSceneId(scene.getData().getUniqueId());
            network.sendToClient(client, mess);
        }
    }

    @Override
    public void syncObject(NetworkObject object, SyncData syncData, boolean reliable) {
        LOG.log(Level.WARNING, "不再支持这个方法！");
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void speak(Entity actor, String mess, float useTime) {
        if (network.isClient()) {
            // ignore
        } else {
            MessActorSpeak mas = new MessActorSpeak();
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
            MessActionRun runAction = new MessActionRun();
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
        MessEntityHitAttribute mess = new MessEntityHitAttribute();
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
    public void kill(Entity entity) {
        entityNetwork.hitAttribute(entity, AttrConstants.HEALTH, 0, null);
    }

    @Override
    public void setEssential(Entity entity, boolean essential) {
        entityNetwork.hitAttribute(entity, AttrConstants.ESSENTIAL, essential, null);
    }

    @Override
    public void setColor(Entity entity, ColorRGBA color) {
        entityNetwork.hitAttribute(entity, AttrConstants.COLOR, new Vector4f(color.r, color.g, color.b, color.a), null);
    }

    @Override
    public boolean useObjectData(Entity entity, long objectUniqueId) {
        if (network.isClient()) {
            MessEntityUseDataById mess = new MessEntityUseDataById();
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
            MessEntityRemoveData  mess = new MessEntityRemoveData();
            mess.setEntityId(entity.getEntityId());
            mess.setObjectId(objectUniqueId);
            mess.setAmount(amount);
            network.sendToServer(mess);
            return false;
        }
        return entityNetwork.removeObjectData(entity, objectUniqueId, amount);
    }
    
    @Override
    public void selectPlayer(String actorId, String actorName) {
        if (network.isClient()) {
            network.sendToServer(new MessPlayActorSelect(actorId, actorName));
        } else {
            Entity actor = Loader.load(actorId);
            actor.getData().setName(actorName);
            // 暂时以1作为默认分组
            gameService.setTeam(actor, 1);
            
            // xxx 这一段要重构
//            List<Skill> waitSkills = skillService.getSkillWait(actor);
//            if (waitSkills != null && !waitSkills.isEmpty()) {
//                skillService.playSkill(actor, waitSkills.get(0), false);
//            }
            
            // 这是主机,所以要设置为当前主场景玩家,与actor.setPlayer(true)不同
            // 注:在设置名字之后再setAsPlayer,否则FacePanel中的player名字不会更新
//            playService.setMainPlayer(actor);
            gameService.setPlayer(actor);
            playNetwork.addEntity(actor);
            
            // 通知所有客户端
            String message = ResourceManager.get("lan.enterGame", new Object[] {actorName});
            MessageType type = MessageType.item;
            MessMessage mess = new MessMessage();
            mess.setMessage(message);
            mess.setType(type);
            network.broadcast(mess); 
            
            // 通知主机
            gameService.addMessage(message, type);
        }
    }

    @Override
    public void addSimplePlayer(Entity actor) {
         if (network.isClient()) {
            EntityData data = actor.getData();
            network.sendToServer(new MessPlayActorSelect(data.getId(), data.getName()));
        } else {
             
            // 广播到客户端进行载入角色
            // XXX actor need to setAsSimplePlayer
            playNetwork.addEntity(actor);
            
            // 通知所有客户端
            String message = ResourceManager.get("lan.enterGame", new Object[] {actor.getData().getName()});
            MessageType type = MessageType.item;
            MessMessage notice = new MessMessage();
            notice.setMessage(message);
            notice.setType(type);
            if (network.hasConnections()) {
                network.broadcast(notice);                          
            }
            // 通知主机
            gameService.addMessage(message, type);
        }
    }
    
}
