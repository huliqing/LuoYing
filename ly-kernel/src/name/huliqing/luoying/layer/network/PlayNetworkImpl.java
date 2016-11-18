/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import java.util.Random;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.mess.MessAutoAttack;
import name.huliqing.luoying.mess.MessEntityAdd;
import name.huliqing.luoying.mess.MessEntityRemove;
import name.huliqing.luoying.mess.MessRandomSeed;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 * @author huliqing
 */
public class PlayNetworkImpl implements PlayNetwork {
//    private static final Logger LOG = Logger.getLogger(PlayNetworkImpl.class.getName());
    private final static Network NETWORK = Network.getInstance();
    private PlayService playService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
    }

    @Override
    public void addEntity(Entity entity) {
        addEntity(playService.getGame().getScene(), entity);
    }
    
    @Override
    public void addGuiEntity(Entity entity) {
        addEntity(playService.getGame().getGuiScene(), entity);
    }
    
    @Override
    public void addEntity(Scene scene, Entity entity) {
        if (NETWORK.isClient()) {
            return;
        }
        
        entity.updateDatas();
        MessEntityAdd mess = new MessEntityAdd();
        mess.setEntityData(entity.getData());
        mess.setSceneId(scene.getData().getUniqueId());
        NETWORK.broadcast(mess);
        
        playService.addEntity(scene, entity);
    }

    @Override
    public void removeEntity(Entity entity) {
        if (NETWORK.isClient()) {
            return;
        }
        MessEntityRemove mess = new MessEntityRemove();
        mess.setEntityId(entity.getEntityId());
        NETWORK.broadcast(mess);

        playService.removeEntity(entity);
    }
    
    @Override
    public void attack(Entity actor, Entity target) {
        // On client
        if (NETWORK.isClient()) {
            MessAutoAttack mess = new MessAutoAttack();
            mess.setTargetId(target != null ? target.getData().getUniqueId() : -1);
            NETWORK.sendToServer(mess);
            return;
        }

        // On Server，这个命令服务端不需要广播到客户端。
        
        playService.attack(actor, target);
    }
    
    @Override
    public void updateRandomSeed() {
        if (NETWORK.isClient()) {
            return;
        }
        int seed = new Random().nextInt();
        
        // 将种子发送到客户端，由客户端更新随机数
        if (NETWORK.hasConnections()) {
            MessRandomSeed mess = new MessRandomSeed();
            mess.setRandomSeed(seed);
            NETWORK.broadcast(mess);
        }
        
        // 服务端更新随机种子。不要实现 PlayService.updateRandomSeed()方法。
        RandomManager.setRandomSeed(seed);
        
    }
    
//    @Override
//    public void addActor(Actor actor) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessPlayActorLoaded mess = createActorLoadedMess(actor);
//                NETWORK.broadcast(mess);
//            }
//            
//            // 在场景中添加角色
//            playService.addActor(actor);
//        }
//    }
//
//    @Override
//    public void addSimplePlayer(Actor actor) {
//        if (NETWORK.isClient())
//            return;
//        
//        // 服务端添加角色
//        playService.addSimplePlayer(actor);
//        
//        // 广播到客户端进行载入角色
//        if (NETWORK.hasConnections()) {
//            MessPlayActorLoaded mess = createActorLoadedMess(actor);
//            NETWORK.broadcast(mess);
//        }
//    }
//
//    @Override
//    public void addView(View view) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessViewAdd mess = new MessViewAdd();
//                mess.setViewData(view.getUpdateData());
//                NETWORK.broadcast(mess);
//            }
//            
//            playService.addView(view);
//        }
//    }
//
//    @Override
//    public void removeObject(Object object) {
//        if (!NETWORK.isClient()) {
//            
//            if (NETWORK.hasConnections()) {
//                if (object instanceof Actor) {
//                    Actor actor = (Actor) object;
//                    MessSCActorRemove mess = new MessSCActorRemove();
//                    mess.setActorId(actor.getData().getUniqueId());
//                    NETWORK.broadcast(mess);
//                    
//                } else if (object instanceof View) {
//                    View view = (View) object;
//                    MessViewRemove mess = new MessViewRemove();
//                    mess.setViewId(view.getData().getUniqueId());
//                    NETWORK.broadcast(mess);
//                    
//                } else if (object instanceof Spatial) {
//                    Spatial spatialObject = (Spatial) object;
//                    Actor actor = spatialObject.getControl(Actor.class);
//                    if (actor != null) {
//                        MessSCActorRemove mess = new MessSCActorRemove();
//                        mess.setActorId(actor.getData().getUniqueId());
//                        NETWORK.broadcast(mess);
//                    }
//                }
//            }
//            
//            playService.removeObject(object);
//        }
//    }
//
//    /**
//     * 发送消息到所有的客户端
//     * @param message
//     * @param type 
//     */
//    @Override
//    public void addMessage(String message, MessageType type) {
//        if (!NETWORK.isClient()) {
//            if (NETWORK.hasConnections()) {
//                MessMessage mess = new MessMessage();
//                mess.setMessage(message);
//                mess.setType(type);
//                NETWORK.broadcast(mess);
//            }
//            
//            playService.addMessage(message, type);
//        }
//    }
//
//    /**
//     * 向指定的客户端发消息，不需要广播
//     * @param actor
//     * @param message
//     * @param type 
//     */
//    @Override
//    public void addMessage(Actor actor, String message, MessageType type) {
//        if (!actorService.isPlayer(actor)) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "Do not addMessage to none player!");
//            }
//            return;
//        }
//        
//        if (!NETWORK.isClient()) {
//            // 服务端主机
//            if (Ly.getPlayState().getPlayer() == actor) {
//                playService.addMessage(message, type);
//                
//            // 发送到指定客户端
//            } else {
//                if (NETWORK.hasConnections()) {
//                    MessMessage mess = new MessMessage();
//                    mess.setMessage(message);
//                    mess.setType(type);
//                    NETWORK.sendToClient(actor, mess);
//                }
//            }
//        }
//    }

    // remove20161006
//    @Override
//    public void moveObject(Actor actor, Vector3f position) {
//        if (!NETWORK.isClient()) {
//            // 先在服务端移动后再同步,因为移动后的位置可能最终被修正
//            playService.moveObject(actor, position);
//            
//            // 广播同步位置
//            if (actor != null) {
//                NETWORK.syncTransformDirect(actor);
//            }
//        }
//    }

    // remove20161006
//    @Override
//    public void addMessageOnlyClients(String message, MessageType type) {
//        if (!NETWORK.isClient()) {
//            // 广播到客户端(主机不要)
//            if (NETWORK.hasConnections()) {
//                MessMessage mess = new MessMessage();
//                mess.setMessage(message);
//                mess.setType(type);
//                NETWORK.broadcast(mess);
//            }
//        }
//    }

//    @Override
//    public void syncGameInitToClient(HostedConnection client) {
//        if (NETWORK.isClient()) 
//            return;
//        
//        // 注意：同步当前场景中的对象时不要一起发送，
//        // 这会导致溢出异常（当场景中角色或物体太多时)
//        // 这些命令是有序的，不用担心角色同步问题
//        
//        // 同步所有角色
//        List<Actor> actors = playService.findAllActor();
//        for (Actor actor : actors) {
//            MessPlayActorLoaded mess = createActorLoadedMess(actor);
//            NETWORK.sendToClient(client, mess);
//        }
//        
//        // 同步所有视图
//        List<View> views = playService.findAllViews();
//        for (View v : views) {
//            MessViewAdd mess = new MessViewAdd();
//            mess.setViewData(v.getUpdateData());
//            NETWORK.sendToClient(client, mess);
//        }
//    }
//    
//    // 创建一个需要通知客户端载入的角色数据
//    private MessPlayActorLoaded createActorLoadedMess(Actor actor) {
//        // 同步角色数据及位置和视角
//        MessPlayActorLoaded mess = new MessPlayActorLoaded();
//        mess.setActorData(actor.getData());
//        mess.setLocation(actor.getSpatial().getWorldTranslation());
//        mess.setViewDirection(actorService.getViewDirection(actor));
//
//        // 同步角色动画
//        ChannelModule cp = actor.getModule(ChannelModule.class);
//        if (cp != null && !cp.getChannels().isEmpty()) {
//            List<Channel> chs = cp.getChannels();
//            String[] channels = new String[chs.size()];
//            String[] anims = new String[chs.size()];
//            byte[] loopModes = new byte[chs.size()];
//            float[] speeds = new float[chs.size()];
//            float[] times = new float[chs.size()];
//            for (int i = 0; i < chs.size(); i++) {
//                Channel c = chs.get(i);
//                channels[i] = c.getId();
//                anims[i] = c.getAnimChannel().getAnimationName();
//                byte lm = 0;
//                LoopMode loopMode = c.getAnimChannel().getLoopMode();
//                if (loopMode == LoopMode.Loop) {
//                    lm = 1;
//                } else if (loopMode == LoopMode.Cycle) {
//                    lm = 2;
//                }
//                loopModes[i] = lm;
//                speeds[i] = c.getAnimChannel().getSpeed();
//                times[i] = c.getAnimChannel().getTime();
//            }
//            mess.setAnims(anims);
//            mess.setChannels(channels);
//            mess.setLoopModes(loopModes);
//            mess.setSpeeds(speeds);
//            mess.setTimes(times);
//        }
//        return mess;
//    }
//    
//    @Override
//    public void syncObject(NetworkObject syncObject, SyncData syncData, boolean reliable) {
//        if (NETWORK.isClient()) 
//            return;
//        
//        MessSyncObject mess = new MessSyncObject();
//        mess.setReliable(reliable);
//        mess.setObjectId(syncObject.getSyncId());
//        mess.setSyncData(syncData);
//        NETWORK.broadcast(mess);
//    }

//    @Override
//    public void changeGame(String gameId) {
//        changeGame(gameService.loadGameData(gameId));
//    }
//
//    @Override
//    public void changeGame(GameData gameData) {
//        if (NETWORK.isClient())
//            return;
//        
//        MessPlayChangeGameState mess = new MessPlayChangeGameState();
//        mess.setGameData(gameData);
//        NETWORK.broadcast(mess);
//
//        playService.changeGame(gameData);
//    }


    
    
}
