/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.mess.MessEntityHitAttribute;
import name.huliqing.luoying.mess.MessEntityHitNumberAttribute;
import name.huliqing.luoying.mess.MessPlayActorSelect;
import name.huliqing.luoying.mess.MessPlayActorSelectResult;
import name.huliqing.luoying.mess.MessPlayClientExit;
import name.huliqing.luoying.mess.MessSCClientList;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.network.DefaultServerListener;

/**
 * 服务端RpgGame
 * @author huliqing
 */
public class ServerNetworkRpgGame extends NetworkRpgGame {
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);

    protected GameServer gameServer;
 
    public ServerNetworkRpgGame() {}
     
//    public ServerNetworkRpgGame(GameData gameData) {
//        super(gameData);
//    }
    
    public ServerNetworkRpgGame(GameServer gameServer) {
//        super(gameServer.getGameData());
        this.gameServer = gameServer;
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // 创建server
        if (gameServer == null) {
            try {
                gameServer = network.createGameServer(data);
                gameServer.start();
            } catch (IOException ex) {
                Logger.getLogger(ServerNetworkRpgGame.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        gameServer.setServerListener(new NetworkServerListener(app));
        // 将服务端标记为"running"状态
        gameServer.setServerState(GameServer.ServerState.running);
        
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
    protected final void onSelectPlayer(String actorId, String actorName) {
        Entity actor = Loader.load(actorId);
        actor.getData().setName(actorName);
        getScene().addEntity(actor);
        // 设置为当前场景主玩家
        setPlayer(actor);
        // 打开UI
        setUIVisiable(true);
        // 通知所有客户更新“客户端列表
        gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
    }
    
    /**
     * 添加普通类型的玩家角色到场景中（不是当前场景的主玩家）,该方法需要把actor设置为玩家类型的角色，并且在添加到场景
     * 后要通知所有客户端。
     * @param actor 
     */
    protected void addPlayer(Entity actor) {
        gameNetwork.addSimplePlayer(actor);
        // 更新本地（服务端）客户端列表
        onClientListUpdated();
        // 通知所有客户更新“客户端列表
        gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
    }
    
    /**
     * 处理来自服务端的肖息。如果该方法返回true,则阻止后续的处理。
     * @param gameServer
     * @param source
     * @param m
     * @return 
     */
    protected boolean processMessage(GameServer gameServer, HostedConnection source, Message m) {
        // 客户端告诉服务端，要选择哪一个角色进行游戏
        if (m instanceof MessPlayActorSelect) {

            // 选择玩家角色
            MessPlayActorSelect mess = (MessPlayActorSelect) m;
            Entity actor = Loader.load(mess.getActorId());
            actor.getData().setName(mess.getActorName());
            // 默认情况下，不管是在Story模式或是在Lan模式，玩家选择后的角色都为1级.但是在某些情况下会有一些不同，比如：
            // 1.在Lan模式下玩家的初始属性可能会受Game逻辑的影响.参考：gameState.getGame().onPlayerSelected(actor);
            // 2.在Story模式下，如果客户端的资料已经存档在服务端，则连接时直接使用存档进行游戏，而不需要重新选择角色。
            gameService.setLevel(actor, 1);
            
            // 记住客户端所选择的角色,要放在addPlayer之前，因为在gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
            // 之前要先更新ConnData,否则gameServer.getClients()获取不到实时的角色资料更新
            ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            cd.setEntityId(actor.getData().getUniqueId());
            cd.setEntityName(actor.getData().getName());
            
            // 添加到场景
            addPlayer(actor);
            
            // 返回消息给客户端，让客户端知道所选择的角色成功。
            MessPlayActorSelectResult result = new MessPlayActorSelectResult();
            result.setActorId(actor.getData().getUniqueId());
            result.setSuccess(true);
            gameServer.send(source, result);
            
            return true;
        } 

        // 当服务端接收到客户端退出游戏的消息时，这里什么也不处理。与故事模式不同。故事模式要保存客户端资料.
        // 服务端暂时不需要处理任何逻辑
        if (m instanceof MessPlayClientExit) {
            return true;
        }
        
        return false;
    }
    
    private class NetworkServerListener extends DefaultServerListener {
        
        public NetworkServerListener(Application app) {
            super(app);
        }
        
        @Override
        protected void onClientsUpdated(GameServer gameServer) {
            super.onClientsUpdated(gameServer);
            // 通知客户端列表更新，注：这里只响应新连接或断开连接。不包含客户端资料的更新。
            onClientListUpdated();
        }

        @Override
        protected void processServerMessage(GameServer gameServer, HostedConnection source, Message m) {
            // 本地处理
            if (processMessage(gameServer, source, m)) {
                return;
            }
            // 由父类处理
            super.processServerMessage(gameServer, source, m); 
        }
    }
}
