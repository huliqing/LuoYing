/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.ActorSelectMess;
import name.huliqing.luoying.mess.ActorSelectResultMess;
import name.huliqing.luoying.mess.SceneLoadedMess;
import name.huliqing.luoying.mess.network.ClientExitMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.mess.network.RequestGameInitMess;
import name.huliqing.luoying.mess.network.RequestGameInitStartMess;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.mess.MessageMess;
import name.huliqing.luoying.network.DefaultServerListener;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.skill.Skill;
import name.huliqing.ly.LyConfig;
import name.huliqing.ly.constants.AttrConstants;

/**
 * 服务端RpgGame的抽象实现.
 * @author huliqing
 */
public abstract class ServerNetworkRpgGame extends NetworkRpgGame {
    private final GameService gameService = Factory.get(GameService.class);
//    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final EntityService entityService = Factory.get(EntityService.class);

    protected GameServer gameServer;
 
    public ServerNetworkRpgGame() {}
     
    /**
     * 设置服务端GameServer
     * @param gameServer 
     */
    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // 创建server
        if (gameServer == null) {
            try {
                gameServer = network.createGameServer(data
                        , LyConfig.getGameName()
                        , LyConfig.getVersionName()
                        , LyConfig.getVersionCode()
                        , LyConfig.getServerPort());
                // 打开局域网广播功能
                gameServer.setLanDiscoverEnabled(true);
                gameServer.start();
            } catch (IOException ex) {
                Logger.getLogger(ServerNetworkRpgGame.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        gameServer.setServerListener(new NetworkServerListener());
        // 将服务端标记为"running"状态
        gameServer.setServerState(GameServer.ServerState.running);
    }
    
    @Override
    public void onSceneLoaded(Scene scene) {
        super.onSceneLoaded(scene); 
        // 场景载入完成时发一个广播通知所有客户端。
        SceneLoadedMess mess = new SceneLoadedMess(scene.getData().getId());
        gameServer.broadcast(mess);
    }
    
    @Override
    public List<ConnData> getClients() {
        return gameServer.getClients();
    }

    @Override
    public void kickClient(int connId) {
        gameServer.kickClient(connId, "kick!");
    }
    
    @Override
    protected final void onSelectPlayer(EntityData entityData) {
        Entity actor = Loader.load(entityData);
        onAddServerPlayer(actor);
    }
    
    /**
     * 当服务端添加主玩家时该方法被调用
     * @param actor 
     */
    protected void onAddServerPlayer(Entity actor) {
        // 设置为当前场景主玩家
        setPlayer(actor);
        // 打开UI
        setUIVisiable(true);
        // 加一些生命值避免角色在死亡时存档，导致进入游戏仍是死亡状态
        entityService.hitNumberAttribute(actor, AttrConstants.HEALTH, 100, null);
        // 让角色处于“等待”
//        skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
        Skill waitSkill = skillService.getSkillWaitDefault(actor);
        if (waitSkill != null) {
            entityService.useObjectData(actor, waitSkill.getData().getUniqueId());
        }
        
        // 添加客户端角色
        playNetwork.addEntity(actor);
        
        // 通知所有客户更新“客户端列表
        gameServer.broadcast(new ClientsMess(gameServer.getClients()));
    }
    
    /**
     * 添加客户端玩家角色到场景中,该方法会把角色添加到服务端场景中，并广播到所有客户端。
     * @param connData 客户端玩家连接信息
     * @param actor 客户端玩家最终选择的角色
     */
    protected void onAddClientPlayer(ConnData connData, Entity actor) {
        // 加一些生命值避免角色在死亡时存档，导致进入游戏仍是死亡状态
        entityService.hitNumberAttribute(actor, AttrConstants.HEALTH, 100, null);
        // 让角色处于“等待”
//        skillService.playSkill(actor, skillService.getSkillWaitDefault(actor), false);
        Skill waitSkill = skillService.getSkillWaitDefault(actor);
        if (waitSkill != null) {
            entityService.useObjectData(actor, waitSkill.getData().getUniqueId());
        }
        
        // 确保处于”死亡后不被清理“
        gameService.setEssential(actor, true);
        // 标记为玩家
        gameService.setPlayer(actor, true);
        // 在服务端上角色的逻辑都必须是始终打开的，这个参数可能会在客户端选择角色的时候被关闭，这里必须打开。
        gameService.setAutoLogic(actor, true);
        // 打开消息输出功能
        gameService.setMessageEnabled(actor, true);
        
        // 添加客户端角色
        playNetwork.addEntity(actor);
        
        // 更新本地（服务端）客户端列表
        onClientListUpdated();
        
        // 消息：通知所有客户端有新的玩家进入
        String message = ResourceManager.get(ResConstants.LAN_ENTER_GAME, new Object[] {gameService.getName(actor)});
        MessageType type = MessageType.item;
        MessageMess notice = new MessageMess();
        notice.setMessage(message);
        notice.setType(type);
        if (network.hasConnections()) {
            network.broadcast(notice);                          
        }
        
        // 通知主机
        gameService.addMessage(message, type);
        
        // 通知所有客户更新“客户端列表
        gameServer.broadcast(new ClientsMess(gameServer.getClients()));
    }
    
    /**
     * 处理来自服务端的肖息。如果该方法返回true,则阻止后续的处理。
     * @param gameServer
     * @param source
     * @param m
     */
    protected void processGameMess(GameServer gameServer, HostedConnection source, GameMess m) {
        // 客户端告诉服务端，要选择哪一个角色进行游戏.
        // 在必要时可以对接收到的ActorSelectMess进行一些验证,比如确保选择的角色数据没有异常。
        // 名称不会重复等。
        if (m instanceof ActorSelectMess) {

            // 选择玩家角色
            ActorSelectMess mess = (ActorSelectMess) m;
            Entity actor = Loader.load(mess.getEntityData());

            // 默认情况下，不管是在Story模式或是在Lan模式，玩家选择后的角色都为1级.但是在某些情况下会有一些不同，比如：
            // 1.在Lan模式下玩家的初始属性可能会受Game逻辑的影响.参考：gameState.getGame().onPlayerSelected(actor);
            // 2.在Story模式下，如果客户端的资料已经存档在服务端，则连接时直接使用存档进行游戏，而不需要重新选择角色。
            gameService.setLevel(actor, 1);
            
            // 记住客户端所选择的角色,要放在addPlayer之前，因为在gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
            // 之前要先更新ConnData,否则gameServer.getClients()获取不到实时的角色资料更新
            ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            cd.setEntityId(actor.getData().getUniqueId());
            cd.setEntityName(gameService.getName(actor));
            
            // 添加到场景
            onAddClientPlayer(cd, actor);
            
            // 返回消息给客户端，让客户端知道所选择的角色成功。
            ActorSelectResultMess result = new ActorSelectResultMess();
            result.setActorId(actor.getData().getUniqueId());
            result.setSuccess(true);
            gameServer.send(source, result);
            
            return;
        } 

        // 当服务端接收到客户端退出游戏的消息时，这里什么也不处理。与故事模式不同。故事模式要保存客户端资料.
        // 服务端暂时不需要处理任何逻辑
        if (m instanceof ClientExitMess) {
            return;
        }
        
        m.applyOnServer(gameServer, source);
    }
    
    private final class NetworkServerListener extends DefaultServerListener {
        
        @Override
        protected void onReceiveMessRequestGameInit(GameServer gameServer, HostedConnection conn, RequestGameInitMess mess) {
            
            int needInitEntities = scene.getData().getEntityDatas() != null ? scene.getData().getEntityDatas().size() : 0;
            
            // 向客户端返回初始化OK的消息
            RequestGameInitStartMess result = new RequestGameInitStartMess(needInitEntities);
            gameServer.send(conn, result);
            
            // 立即向客户端初始化当前场景中已经载入的实体。
            List<Entity> initEntities = scene.getEntities();
            for (Entity e : initEntities) {
                playNetwork.addEntityToClient(conn, e);
            }
        }
        
        @Override
        protected void onClientsUpdated(GameServer gameServer) {
            super.onClientsUpdated(gameServer);
            // 通知客户端列表更新，注：这里只响应新连接或断开连接。不包含客户端资料的更新。
            onClientListUpdated();
        }

        @Override
        protected void onReceiveGameMess(GameServer gameServer, HostedConnection source, GameMess m) {
            processGameMess(gameServer, source, m);
        }

        @Override
        protected void onClientRemoved(GameServer gameServer, HostedConnection conn) {
            super.onClientRemoved(gameServer, conn); 
            ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            if (cd == null)
                return;

            Entity clientPlayer = playService.getEntity(cd.getEntityId());
            if (clientPlayer == null)
                return;
            
            // 1.将客户端角色的所有宠物移除出场景,注意是宠物，不要把非生命的（如防御塔）也一起移除
            List<Entity> actors = playService.getEntities(Entity.class, null);
            if (actors != null && !actors.isEmpty()) {
                for (Entity actor : actors) {
                    if (gameService.getOwner(actor) == clientPlayer.getData().getUniqueId() && gameService.isBiology(actor)) {
                        playNetwork.removeEntity(actor);
                    }
                }
            }

            // 2.将客户端角色移除出场景
            playNetwork.removeEntity(clientPlayer);

            // 3.通知所有客户端（不含主机）
            String message = ResManager.get(ResConstants.LAN_CLIENT_EXISTS, new Object[] {gameService.getName(clientPlayer)});
            MessageMess notice = new MessageMess();
            notice.setMessage(message);
            notice.setType(MessageType.notice);
            gameServer.broadcast(notice);

            // 4.通知主机
            addMessage(message, MessageType.notice);
        }

        
    }
}
