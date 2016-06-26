/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.network;

import name.huliqing.fighter.game.state.lan.Network;
import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.data.ProtoData;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.mess.MessMessage;
import name.huliqing.fighter.game.mess.MessPlayActorLoaded;
import name.huliqing.fighter.game.mess.MessPlayChangeGameState;
import name.huliqing.fighter.game.mess.MessSCActorRemove;
import name.huliqing.fighter.game.mess.MessSyncObject;
import name.huliqing.fighter.game.mess.MessViewAdd;
import name.huliqing.fighter.game.mess.MessViewRemove;
import name.huliqing.fighter.game.service.GameService;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.object.NetworkObject;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.actor.ActorControl;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.object.bullet.Bullet;
import name.huliqing.fighter.object.channel.Channel;
import name.huliqing.fighter.object.channel.ChannelProcessor;
import name.huliqing.fighter.object.effect.Effect;
import name.huliqing.fighter.object.scene.Scene;
import name.huliqing.fighter.object.view.View;

/**
 *
 * @author huliqing
 */
public class PlayNetworkImpl implements PlayNetwork {
    private static final Logger LOG = Logger.getLogger(PlayNetworkImpl.class.getName());
    private final static Network network = Network.getInstance();
    private PlayService playService;
    private ActorService actorService;
    private ActionService actionService;
    private SkinService skinService;
    private GameService gameService;
    
    private ActionNetwork actionNetwork;
    private SkinNetwork skinNetwork;
    private SkillNetwork skillNetwork;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        actorService = Factory.get(ActorService.class);
        actionService = Factory.get(ActionService.class);
        skinService = Factory.get(SkinService.class);
        gameService = Factory.get(GameService.class);
        actionNetwork = Factory.get(ActionNetwork.class);
        skinNetwork = Factory.get(SkinNetwork.class);
        skillNetwork = Factory.get(SkillNetwork.class);
    }

    @Override
    public void addPlayObject(PlayObject playObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePlayObject(PlayObject playObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void addActor(Actor actor) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessPlayActorLoaded mess = createActorLoadedMess(actor);
                network.broadcast(mess);
            }
            
            // 在场景中添加角色
            playService.addActor(actor);
        }
    }
 
    @Override
    public void addEffect(Effect effect) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addBullet(Bullet bullet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void addShortcut(Actor actor, ProtoData data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addView(View view) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessViewAdd mess = new MessViewAdd();
                mess.setViewData(view.getUpdateData());
                network.broadcast(mess);
            }
            
            playService.addView(view);
        }
    }

    @Override
    public void addAnimation(Anim animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAnimation(Anim animation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void addObject(Object object, boolean gui) {        
        throw new UnsupportedOperationException();
    }

    @Override
    public void addObject(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addObjectGui(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void removeObject(Object object) {
        if (!network.isClient()) {
            
            if (network.hasConnections()) {
                if (object instanceof Actor) {
                    Actor actor = (Actor) object;
                    MessSCActorRemove mess = new MessSCActorRemove();
                    mess.setActorId(actor.getData().getUniqueId());
                    network.broadcast(mess);
                    
                } else if (object instanceof View) {
                    View view = (View) object;
                    MessViewRemove mess = new MessViewRemove();
                    mess.setViewId(view.getData().getUniqueId());
                    network.broadcast(mess);
                    
                } else if (object instanceof Spatial) {
                    Spatial spatialObject = (Spatial) object;
                    ActorControl actor = spatialObject.getControl(ActorControl.class);
                    if (actor != null) {
                        MessSCActorRemove mess = new MessSCActorRemove();
                        mess.setActorId(actor.getData().getUniqueId());
                        network.broadcast(mess);
                    }
                }
            }
            
            playService.removeObject(object);
        }
    }

    /**
     * 发送消息到所有的客户端
     * @param message
     * @param type 
     */
    @Override
    public void addMessage(String message, MessageType type) {
        if (!network.isClient()) {
            if (network.hasConnections()) {
                MessMessage mess = new MessMessage();
                mess.setMessage(message);
                mess.setType(type);
                network.broadcast(mess);
            }
            
            playService.addMessage(message, type);
        }
    }

    /**
     * 向指定的客户端发消息，不需要广播
     * @param actor
     * @param message
     * @param type 
     */
    @Override
    public void addMessage(Actor actor, String message, MessageType type) {
        if (!actor.isPlayer()) {
            if (Config.debug) {
                LOG.log(Level.INFO, "Do not addMessage to none player!");
            }
            return;
        }
        
        if (!network.isClient()) {
            // 服务端主机
            if (Common.getPlayState().getPlayer() == actor) {
                playService.addMessage(message, type);
                
            // 发送到指定客户端
            } else {
                if (network.hasConnections()) {
                    MessMessage mess = new MessMessage();
                    mess.setMessage(message);
                    mess.setType(type);
                    network.sendToClient(actor, mess);
                }
            }
        }
    }

    @Override
    public List<Actor> findAllActor() {
        // ignore客户端不需要
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Actor> findOrganismActors(int group, List<Actor> store) {
        // ignore客户端不需要
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Actor findActor(String id) {
        // ignore客户端不需要
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Actor findActor(long actorUniqueId) {
        // ignore客户端不需要
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<View> findAllViews() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public View findView(long uniqueId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NetworkObject findSyncObject(long objectId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NavMeshPathfinder createPathfinder() {
//        return playService.createPathfinder();
        
        // ignore客户端不需要
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Spatial getTerrain() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getTerrainHeight(float x, float z) {
        return playService.getTerrainHeight(x, z);
    }

    @Override
    public Vector3f getActorForwardPosition(Actor actor, float distance, Vector3f store) {
        return playService.getActorForwardPosition(actor, distance, store);
    }

    @Override
    public Actor getPlayer() {
        return playService.getPlayer();
    }

    @Override
    public Vector3f getRandomTerrainPoint(Vector3f store) {
        return playService.getRandomTerrainPoint(store);
    }

    @Override
    public void moveObject(Spatial spatial, Vector3f position) {
        if (!network.isClient()) {
            // 先在服务端移动后再同步,因为移动后的位置可能最终被修正
            playService.moveObject(spatial, position);
            
            // 广播同步位置
            ActorControl actor = spatial.getControl(ActorControl.class);
            if (actor != null) {
                network.syncTransformDirect(actor);
            }
        }
    }

    @Override
    public Scene getScene() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInScene(Actor actor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Actor getTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTarget(Actor target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector3f moveUpToTerrain(Vector3f position) {
        return playService.moveUpToTerrain(position);
    }

    @Override
    public float getScreenWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getScreenHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsPlayer(Actor actor) {
        // 这个命令不需要广播到客户端
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInScene(Spatial spatial) {
        return playService.isInScene(spatial);
    }

    @Override
    public void saveCompleteStage(int storyNum) {
        playService.saveCompleteStage(storyNum);
    }
    
    @Override
    public void showSelectPanel(List<String> selectableActors) {
        playService.showSelectPanel(selectableActors);
    }
    
    @Override
    public void addMessageOnlyClients(String message, MessageType type) {
        if (!network.isClient()) {
            // 广播到客户端(主机不要)
            if (network.hasConnections()) {
                MessMessage mess = new MessMessage();
                mess.setMessage(message);
                mess.setType(type);
                network.broadcast(mess);
            }
        }
    }

    @Override
    public void attack(Actor actor, Actor target) {
        // 客户端不处理
        if (network.isClient()) {
            return;
        }
        
        // 如果角色已经死亡
        if (actorService.isDead(actor)) {
            return;
        }
        
        // 执行战斗行为
        if (target != null) {
            actionNetwork.playFight(actor, target, null);
        }
        
        if (!skinService.isWeaponTakeOn(actor)) {
            skinNetwork.takeOnWeapon(actor, false);
        }
        // 打开或关闭侦察敌人的逻辑,autoDetect不需要广播到客户端，因为客户端不会有
        // 逻辑
        actorService.setAutoDetect(actor, skinService.isWeaponTakeOn(actor));
    }

    @Override
    public void syncGameInitToClient(HostedConnection client) {
        if (network.isClient()) 
            return;
        
        // 注意：同步当前场景中的对象时不要一起发送，
        // 这会导致溢出异常（当场景中角色或物体太多时)
        // 这些命令是有序的，不用担心角色同步问题
        
        // 同步所有角色
        List<Actor> actors = playService.findAllActor();
        for (Actor actor : actors) {
            MessPlayActorLoaded mess = createActorLoadedMess(actor);
            network.sendToClient(client, mess);
        }
        
        // 同步所有视图
        List<View> views = playService.findAllViews();
        for (View v : views) {
            MessViewAdd mess = new MessViewAdd();
            mess.setViewData(v.getUpdateData());
            network.sendToClient(client, mess);
        }
    }
    
    // 创建一个需要通知客户端载入的角色数据
    private MessPlayActorLoaded createActorLoadedMess(Actor actor) {
        // 同步角色数据及位置和视角
        MessPlayActorLoaded mess = new MessPlayActorLoaded();
        mess.setActorData(actor.getData());
        mess.setLocation(actor.getLocation());
        mess.setViewDirection(actor.getViewDirection());

        // 同步角色动画
        ChannelProcessor cp = actor.getChannelProcessor();
        if (cp != null && !cp.getChannels().isEmpty()) {
            List<Channel> chs = cp.getChannels();
            String[] channels = new String[chs.size()];
            String[] anims = new String[chs.size()];
            byte[] loopModes = new byte[chs.size()];
            float[] speeds = new float[chs.size()];
            float[] times = new float[chs.size()];
            for (int i = 0; i < chs.size(); i++) {
                Channel c = chs.get(i);
                channels[i] = c.getId();
                anims[i] = c.getAnimChannel().getAnimationName();
                byte lm = 0;
                LoopMode loopMode = c.getAnimChannel().getLoopMode();
                if (loopMode == LoopMode.Loop) {
                    lm = 1;
                } else if (loopMode == LoopMode.Cycle) {
                    lm = 2;
                }
                loopModes[i] = lm;
                speeds[i] = c.getAnimChannel().getSpeed();
                times[i] = c.getAnimChannel().getTime();
            }
            mess.setAnims(anims);
            mess.setChannels(channels);
            mess.setLoopModes(loopModes);
            mess.setSpeeds(speeds);
            mess.setTimes(times);
        }
        return mess;
    }
    
    @Override
    public void syncObject(NetworkObject syncObject, SyncData syncData, boolean reliable) {
        if (network.isClient()) 
            return;
        
        MessSyncObject mess = new MessSyncObject();
        mess.setReliable(reliable);
        mess.setObjectId(syncObject.getSyncId());
        mess.setSyncData(syncData);
        network.broadcast(mess);
    }

    @Override
    public Application getApplication() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changeGame(String gameId) {
        changeGame(gameService.loadGameData(gameId));
    }

    @Override
    public void changeGame(GameData gameData) {
        if (network.isClient())
            return;
        
        MessPlayChangeGameState mess = new MessPlayChangeGameState();
        mess.setGameData(gameData);
        network.broadcast(mess);

        playService.changeGame(gameData);
    }

    @Override
    public String getGameId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
