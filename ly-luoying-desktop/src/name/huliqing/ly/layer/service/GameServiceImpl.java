/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.network.HostedConnection;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.Start;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.object.game.SimpleRpgGame;
import name.huliqing.ly.view.shortcut.ShortcutManager;
import name.huliqing.ly.view.talk.SpeakManager;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkManager;

/**
 * @author huliqing
 */
public class GameServiceImpl implements GameService {

    private PlayService playService;
    private EntityService entityService;
    private ActionService actionService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        entityService = Factory.get(EntityService.class);
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public void addMessage(String message, MessageType type) {
    }

    @Override
    public void speak(Entity actor, String mess, float useTime) {
        SpeakManager.getInstance().doSpeak(actor, mess, useTime);
    }
    
    @Override
    public void talk(Talk talk) {
        // 不要在这里设置setNetwork(false),这会覆盖掉gameNetwork.talk的设置
        // 因为gameNetwork.talk是直接调用gameService.talk的方法
//        talk.setNetwork(false);
        
        TalkManager.getInstance().startTalk(talk);
    }

    @Override
    public NetworkObject findSyncObject(long objectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getPlayer() {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        return game.getPlayer();
    }

    @Override
    public Entity getTarget() {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        return game.getTarget();
    }

    @Override
    public void setTarget(Entity target) {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        game.setTarget(target);
    }

    @Override
    public void exitGame() {
        Start start = (Start) LuoYing.getApp();
        start.changeStartState();
    }

    @Override
    public void saveCompleteStage(int storyNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncGameInitToClient(HostedConnection client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncObject(NetworkObject object, SyncData syncData, boolean reliable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addShortcut(Entity actor, ObjectData data) {
        ShortcutManager.addShortcut(actor, data);
    }

    @Override
    public void playRunToPos(Entity actor, Vector3f worldPos) {
        actionService.playRun(actor, worldPos);
    }

    @Override
    public void setLevel(Entity entity, int level) {
        entityService.hitAttribute(entity, AttrConstants.LEVEL, level, null);
    }

    @Override
    public void setGroup(Entity entity, int group) {
        entityService.hitAttribute(entity, AttrConstants.GROUP, group, null);
    }

    @Override
    public void setTeam(Entity entity, int team) {
        entityService.hitAttribute(entity, AttrConstants.TEAM, team, null);
    }

    @Override
    public void setAutoLogic(Entity entity, boolean autoLogic) {
        entityService.hitAttribute(entity, AttrConstants.AUTO_LOGIC, autoLogic, null);
    }

    @Override
    public void setTarget(Entity entity, long target) {
        entityService.hitAttribute(entity, AttrConstants.TARGET, target, null);
    }

    @Override
    public void setFollow(Entity entity, long target) {
        entityService.hitAttribute(entity, AttrConstants.FOLLOW, target, null);
    }
    
    
    @Override
    public boolean isDead(Entity entity) {
        return entityService.getBooleanAttributeValue(entity, AttrConstants.DEAD, false);
    }

    @Override
    public boolean isBiology(Entity entity) {
        return entityService.getBooleanAttributeValue(entity, AttrConstants.BIOLOGY, false);
    }

    @Override
    public boolean isRotatable(Entity entity) {
        return entityService.getBooleanAttributeValue(entity, AttrConstants.ROTATABLE, false);
    }

    @Override
    public long getOwner(Entity entity) {
        return entityService.getNumberAttributeValue(entity, AttrConstants.OWNER, -1L).longValue();
    }

    @Override
    public int getGroup(Entity entity) { 
        return entityService.getNumberAttributeValue(entity, AttrConstants.GROUP, -1).intValue();
    }

    @Override
    public int getTeam(Entity entity) {
        return entityService.getNumberAttributeValue(entity, AttrConstants.TEAM, -1).intValue();
    }

    @Override
    public int getLevel(Entity entity) {
        return entityService.getNumberAttributeValue(entity, AttrConstants.LEVEL, -1).intValue();
    }

    @Override
    public long getTarget(Entity entity) {
        return entityService.getNumberAttributeValue(entity, AttrConstants.TARGET, -1).longValue();
    }

    @Override
    public void setPartner(Entity entity, Entity partner) {
        setGroup(partner, getGroup(entity));
        setFollow(partner, entity.getEntityId());
    }

    @Override
    public void kill(Entity entity) {
        entityService.hitAttribute(entity, AttrConstants.HEALTH, 0, null);
    }

    @Override
    public void setEssential(Entity entity, boolean essential) {
        entityService.hitAttribute(entity, AttrConstants.ESSENTIAL, essential, null);
    }

    @Override
    public void setColor(Entity entity, ColorRGBA color) {
        entityService.hitAttribute(entity, AttrConstants.COLOR, new Vector4f(color.r, color.g, color.b, color.a), null);
    }

    @Override
    public boolean isEnemy(Entity entity, Entity target) {
        int group1 = entityService.getNumberAttributeValue(entity, AttrConstants.GROUP, -1).intValue();
        int group2 = entityService.getNumberAttributeValue(target, AttrConstants.GROUP, -1).intValue();
        return group1 != group2;
    }

    @Override
    public int getViewDistance(Entity entity) {
        return entityService.getNumberAttributeValue(entity, AttrConstants.VISION, 0).intValue();
    }
    
    @Override
    public Entity findNearestEnemyExcept(Entity actor, float maxDistance) {
        List<Actor> actors = actor.getScene().getEntities(Actor.class, actor.getSpatial().getWorldTranslation(), maxDistance, null);
        float minDistanceSquared = maxDistance * maxDistance;
        float distanceSquared;
        Entity enemy = null;
        for (Entity target : actors) {
//            if (target == except) { // 被排除的角色（同一实例）
//                continue;
//            }
            // 负值的派系不作为敌人搜寻
            if (getGroup(target) < 0) {
                continue;
            }
            // 角色已经死亡或同一派别
            if (isDead(target) || !isEnemy(target, actor)) {
                continue;
            }
            // 判断可视范围内的敌人.
            distanceSquared = target.getSpatial().getWorldTranslation()
                    .distanceSquared(actor.getSpatial().getWorldTranslation());
            if (distanceSquared < minDistanceSquared) { // 找出最近的敌人
                minDistanceSquared = distanceSquared;
                enemy = target;
            }
        }
        return enemy;
    }
}
