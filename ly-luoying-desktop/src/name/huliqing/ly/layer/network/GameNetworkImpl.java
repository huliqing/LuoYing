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
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.ly.mess.MessActionRun;
import name.huliqing.luoying.mess.EntityHitAttributeMess;
import name.huliqing.luoying.mess.EntityRemoveDataMess;
import name.huliqing.luoying.mess.EntityUseDataByIdMess;
import name.huliqing.luoying.mess.ActorSelectMess;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.mess.MessActorSpeak;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.mess.MessMessage;
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
//    private EntityService entityService;
//    private ActorService actorService;
//    private ActionService actionService;
//    private ActorNetwork actorNetwork;
    private EntityNetwork entityNetwork;
    private PlayNetwork playNetwork;
//    private PlayService playService;
    
    @Override
    public void inject() {
//        entityService = Factory.get(EntityService.class);
        gameService = Factory.get(GameService.class);
//        actorService = Factory.get(ActorService.class);
//        actionService = Factory.get(ActionService.class);
//        actorNetwork = Factory.get(ActorNetwork.class);
        entityNetwork = Factory.get(EntityNetwork.class);
        playNetwork = Factory.get(PlayNetwork.class);
//        playService = Factory.get(PlayService.class);
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

    // remove20161126
//    @Override
//    public void syncGameInitToClient(HostedConnection client) {
//         if (network.isClient()) 
//            return;
//        
//        // 注意：同步当前场景中的对象时不要一起发送，
//        // 这会导致溢出异常（当场景中角色或物体太多时)
//        // 这些命令是有序的，不用担心角色同步问题
//        
//        // 同步所有角色
//        SimpleRpgGame rpgGame = (SimpleRpgGame) playService.getGame();
//        Scene scene = rpgGame.getScene();
//        List<Entity> entities = rpgGame.getScene().getEntities(Entity.class, null);
//        for (Entity entity : entities) {
//            entity.updateDatas();
//            MessEntityAdd mess = new MessEntityAdd();
//            mess.setEntityData(entity.getData());
//            mess.setSceneId(scene.getData().getUniqueId());
//            network.sendToClient(client, mess);
//        }
//    }

    // remove201611xx
//    @Override
//    public void syncObject(NetworkObject object, SyncData syncData, boolean reliable) {
//        LOG.log(Level.WARNING, "不再支持这个方法！");
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

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
    
    // remove20161128
//    @Override
//    public void selectPlayer(EntityData entityData) {
//        if (network.isClient()) {
//            network.sendToServer(new ActorSelectMess(entityData));
//        } else {
//            
//            Entity actor = Loader.load(entityData);
//            
//            // 暂时以1作为默认分组
////            gameService.setTeam(actor, 1);
//            // xxx 这一段要重构
////            List<Skill> waitSkills = skillService.getSkillWait(actor);
////            if (waitSkills != null && !waitSkills.isEmpty()) {
////                skillService.playSkill(actor, waitSkills.get(0), false);
////            }
//
//            playNetwork.addEntity(actor);
//            gameService.setPlayer(actor);
//            
//            // 通知所有客户端
//            String message = ResourceManager.get("lan.enterGame", new Object[] {actorName});
//            MessageType type = MessageType.item;
//            MessMessage mess = new MessMessage();
//            mess.setMessage(message);
//            mess.setType(type);
//            network.broadcast(mess); 
//            
//            // 通知主机
//            gameService.addMessage(message, type);
//        }
//    }
    
    
}
