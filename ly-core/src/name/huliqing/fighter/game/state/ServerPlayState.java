/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state;

import java.util.List;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.mess.MessPlayActorSelect;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.game.mess.MessPlayClientExit;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActor;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActorResult;
import name.huliqing.fighter.game.mess.MessSCClientList;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.state.lan.GameServer.ServerState;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.game.Game.GameListener;

/**
 * 局域网模式下的游戏服务端。不保存服务端和客户端的资料，每都都需要选择角色进行游戏。
 * @author huliqing
 */
public class ServerPlayState extends NetworkPlayState  {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 服务端
    private final GameServer gameServer;
    
    public ServerPlayState(Application app, GameServer gameServer) {
        super(app, gameServer.getGameData());
        this.gameServer = gameServer;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // 设置listener
        gameServer.setServerListener(new LanServerListener(app));
    }
    
    @Override
    public void cleanup() {
        super.cleanup(); 
    }

    @Override
    public void changeGameState(GameData gameData) {
        super.changeGameState(gameData);
        
        gameServer.setServerState(ServerState.loading);
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                // 设置状态并向所有客户端广播,这通知所有客户端
                // 可以开始向服务端发送游戏初始化命令的消息
                gameServer.setServerState(ServerState.running);
                // 隐藏其它UI界面
                setUIVisiable(false);
                // 显示角色选择面板
                showSelectPanel(gameState.getGame().getData().getAvailableActors());
            }
        });
        // 服务端需要游戏逻辑，客户端就不需要
        gameState.getGame().setEnabled(true);
    }

    @Override
    public List<ConnData> getClients() {
        return gameServer.getClients();
    }

    @Override
    public void kickClient(int connId) {
        gameServer.kickClient(connId, "kick!");
    }
    
    private class LanServerListener extends DefaultServerListener {
        
        public LanServerListener(Application app) {
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
            // 局域网模式下的服务端不保存客户端资料，所以这里直接返回false，以便让客户端去弹出角色选择窗口，选择一个角色进行游戏
            if (m instanceof MessPlayLoadSavedActor) {
                gameServer.send(source, new MessPlayLoadSavedActorResult(false));
                return;
            }
            
            // 客户端告诉服务端，要选择哪一个角色进行游戏
            if (m instanceof MessPlayActorSelect) {
                // 响应客户端角色选择
                ((MessPlayActorSelect)m).applyOnServer(gameServer, source);
                
                // 当玩家选择完一个角色后交由特定的Game的处理器去拦截处理一些特定的初始化，如角色分组，队伍分组等
                ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
                Actor actor = playService.findActor(cd.getActorId());
                gameState.getGame().onActorSelected(actor);

                // 通知角色选择
                onActorSelected(cd, playService.findActor(cd.getActorId()));
                // 更新本地（服务端）客户端列表
                onClientListUpdated();
                // 通知所有客户更新“客户端列表”
                gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
                return;
            } 
            
            // 当服务端接收到客户端退出游戏的消息时，这里什么也不处理。与故事模式不同。故事模式要保存客户端资料
            if (m instanceof MessPlayClientExit) {
                return;
            }
            
            super.processServerMessage(gameServer, source, m); 
        }
    }
}
