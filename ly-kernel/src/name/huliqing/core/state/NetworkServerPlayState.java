/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.state;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import name.huliqing.core.network.DefaultServerListener;
import name.huliqing.core.data.ConnData;
import name.huliqing.core.Factory;
import name.huliqing.core.data.GameData;
import name.huliqing.core.mess.MessPlayActorSelect;
import name.huliqing.core.mess.MessPlayActorSelectResult;
import name.huliqing.core.mess.MessPlayClientExit;
import name.huliqing.core.mess.MessSCClientList;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.game.Game;

/**
 * 联网模式的服务端
 * @author huliqing
 */
public abstract class NetworkServerPlayState extends NetworkPlayState {
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);

    protected GameServer gameServer;
    
    public NetworkServerPlayState(Application app, GameData gameData) {
        super(app, gameData);
    }
    
    public NetworkServerPlayState(Application app, GameServer gameServer) {
        super(app, gameServer.getGameData());
        this.gameServer = gameServer;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        // 创建server
        if (gameServer == null) {
            try {
                gameServer = network.createGameServer(gameData);
                gameServer.start();
            } catch (IOException ex) {
                Logger.getLogger(StoryServerPlayState.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        gameServer.setServerListener(new NetworkServerListener(app));
    }

    @Override
    public void changeGameState(GameState newGameState) {
        super.changeGameState(newGameState);
        // 服务端需要游戏逻辑，客户端就不需要
        gameServer.setServerState(GameServer.ServerState.loading);
        newGameState.getGame().addListener(new Game.GameListener() {
            @Override
            public void onGameStarted(Game game) {
                // 设置状态并向所有客户端广播,这通知所有客户端
                // 可以开始向服务端发送游戏初始化命令的消息
                gameServer.setServerState(GameServer.ServerState.running);
            }
        });
        newGameState.getGame().setEnabled(true);
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
        Actor actor = actorService.loadActor(actorId);
        actorService.setName(actor, actorName);
        
        addPlayer(actor);
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
    protected void addPlayer(Actor actor) {
        userCommandNetwork.addSimplePlayer(actor);
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
            Actor actor = actorService.loadActor(mess.getActorId());
            actorService.setName(actor, mess.getActorName());
            // 默认情况下，不管是在Story模式或是在Lan模式，玩家选择后的角色都为1级.但是在某些情况下会有一些不同，比如：
            // 1.在Lan模式下玩家的初始属性可能会受Game逻辑的影响.参考：gameState.getGame().onPlayerSelected(actor);
            // 2.在Story模式下，如果客户端的资料已经存档在服务端，则连接时直接使用存档进行游戏，而不需要重新选择角色。
            actorService.setLevel(actor, 1);
            
            // 记住客户端所选择的角色,要放在addPlayer之前，因为在gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
            // 之前要先更新ConnData,否则gameServer.getClients()获取不到实时的角色资料更新
            ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            cd.setActorId(actor.getData().getUniqueId());
            cd.setActorName(actor.getData().getName());
            
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
            if (processMessage(gameServer, source, m)) {
                return;
            }
            super.processServerMessage(gameServer, source, m); 
        }
    }
}
