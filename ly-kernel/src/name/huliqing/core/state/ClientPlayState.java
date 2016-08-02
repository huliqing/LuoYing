/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.state;

import name.huliqing.core.data.ConnData;
import name.huliqing.core.network.GameClient;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.network.AbstractClientListener;
import name.huliqing.core.network.GameClient.ClientState;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.mess.MessBase;
import name.huliqing.core.mess.MessPlayActorSelectResult;
import name.huliqing.core.mess.MessPlayGetClients;
import name.huliqing.core.mess.MessPlayGetServerState;
import name.huliqing.core.mess.MessPlayInitGame;
import name.huliqing.core.mess.MessPlayLoadSavedActor;
import name.huliqing.core.mess.MessPlayLoadSavedActorResult;
import name.huliqing.core.mess.MessPlayClientExit;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.SaveService;
import name.huliqing.core.view.ShortcutManager;
import name.huliqing.core.object.IntervalLogic;
import name.huliqing.core.object.PlayObject;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.game.Game;
import name.huliqing.core.object.game.Game.GameListener;
import name.huliqing.core.save.ShortcutsSave;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI.Corner;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.network.LanClientListener;

/**
 *
 * @author huliqing
 */
public abstract class ClientPlayState extends NetworkPlayState implements AbstractClientListener.PingListener{
    private static final Logger LOG = Logger.getLogger(ClientPlayState.class.getName());
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final SaveService saveService = Factory.get(SaveService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    // 客户端快捷方式,保存键，格式: SHORTCUTS_KEY_PREFIX + actorId
    // 如： "Shortcuts_actorWolf", 注意，使用的是角色类型ID，不是唯一ID。
    private final static String SHORTCUTS_KEY_PREFIX = "Shortcuts_";
    
    // 客户端
    private final GameClient gameClient;
    private LanClientListener clientListener;
    
    // 用于显示与服务端的Ping值信息
    private Text pingLabel;

    public ClientPlayState(Application app, GameClient gameClient) {
        super(app, gameClient.getGameData());
        this.gameClient = gameClient;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.clientListener = new ClientListener(app);
        this.clientListener.addPingListener(this);
        this.gameClient.setGameClientListener(clientListener);
        this.gameClient.setClientState(ClientState.loading);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void exit() {
        // 保存快捷方式
        saveClientShortcuts();
        // 在退出前告诉服务端.
        gameClient.send(new MessPlayClientExit());
        super.exit();
    }

    @Override
    public void changeGameState(GameState newGameState) {
        // 在切换gameState之前先保存快捷方式
        gameClient.setClientState(ClientState.loading);
        if (gameState != null) {
            saveClientShortcuts();
        }
        
        // 切换游戏
        super.changeGameState(newGameState);
        gameClient.setGameData(gameData);        
        // 重要，客户端是不需要执行游戏逻辑的，只需要载入场景及执行场景逻辑就可以。
        gameState.getGame().setEnabled(false);
        // 在载入场景后才把状态设置为ready,这个状态表示客户端准备就绪 
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onGameStarted(Game game) {
                gameClient.setClientState(ClientState.ready);
                // 先隐藏所有UI,这样不会妨碍角色选择界面
                setUIVisiable(false);
            }
        });
        
        // 循环检查服务端状态，确保服务端已经准备好连接
        PlayObject checkState = new IntervalLogic(0.2f) {
            @Override
            protected void doLogic(float tpf) {
                if (gameClient.getClientState() != ClientState.running) {
                    checkToStartClientInit();
                } else {
                    // 在状态任务完成后移除，避免占用资源
                    removeObject(this);
                }
            }
        };
        addObject(checkState, false);
        
        pingLabel = new Text("PING:00");
        pingLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
        pingLabel.setMargin(0, 0, pingLabel.getWidth(), 0);
        pingLabel.setToCorner(Corner.RB);
        addObject(pingLabel, true);
    }
    
    // 保存客户端的快捷方式到指定存档
    private void saveClientShortcuts() {
        Actor player = getPlayer();
        if (player == null) 
            return;
         // 保存快捷方式
        ShortcutsSave ss = new ShortcutsSave();
        ss.setShortcuts(ShortcutManager.getShortcutSaves());
        saveService.saveSavable(SHORTCUTS_KEY_PREFIX + player.getData().getId(), ss);
    }
    
    /**
     * 开始向服务端发起初始化游戏数据的请求。注：该方法会检查客户端和服务端
     * 的状态，只有在确认客户端处于ClientState.ready和服务端处于ServerState.running
     * 时才发起请求，在发起请求后，客户端将转入waiting_init_game状态，以避免重复发送请
     * 求。
     */
    private void checkToStartClientInit() {
        if (gameClient.getClientState() == ClientState.ready && gameClient.getServerState() == GameServer.ServerState.running) {
            // 获取初始场景信息,注：确保这个命令只发送一次。
            gameClient.setClientState(ClientState.waitting_init_game);
            gameClient.send(new MessPlayInitGame());
            
            // 从服务端重新获取所有客户端联接信息，因为gameClient重新设置了listener,
            // 并且clientsWin需要初始化这部分信息，否则客户端进入后打开看不到列表，除非有新客户端连接。
            // 所以这里应该主动获取一次，进行初始化
            gameClient.send(new MessPlayGetClients());

            // 偿试发送消息给服务端，看看有没有客户端的存档资料，如果存在资料就不需要选择新角色进行游戏了。
            // （在故事模式下即可能存在客户端的存档资料）
            gameClient.send(new MessPlayLoadSavedActor());
            
        } else {
            // 请求服务端状态
            gameClient.send(new MessPlayGetServerState());
        }
    }

    @Override
    public List<ConnData> getClients() {
        if (clientListener != null) {
            return clientListener.getClients();
        }
        return null;
    }
    
    @Override
    protected void onSelectPlayer(String actorId, String actorName) {
        Actor actor = actorService.loadActor(actorId);
        actorService.setName(actor, actorName);
        userCommandNetwork.addSimplePlayer(actor);
    }

    @Override
    public void setPlayer(Actor actor) {
        super.setPlayer(actor);
        // 先清除，然后再重新生成快捷方式
        ShortcutManager.cleanup();
        // 载入客户端玩家的快捷方式
        ShortcutsSave ss = saveService.loadSavable(SHORTCUTS_KEY_PREFIX + actor.getData().getId());
        if (ss != null) {
            ShortcutManager.loadShortcut(ss.getShortcuts(), actor);
        }
    }

    @Override
    public void kickClient(int connId) {
        Logger.getLogger(ClientPlayState.class.getName()).log(Level.WARNING
                , "LanClientPlayState could supported kickClient, connId={0}", connId);
    }

    @Override
    public void onPingUpdate(long ping) {
        pingLabel.setText("PING:" + ping);
    }
    
    private class ClientListener extends LanClientListener {
        
        public ClientListener(Application app) {
            super(app);
        }

        @Override
        protected void onClientsUpdated(GameClient gameClient, List<ConnData> clients) {
            super.onClientsUpdated(gameClient, clients);
            // 通知客户端列表更新，注：这里只响应新连接或断开连接。不包含客户端资料的更新。
            onClientListUpdated();
        }
        
        @Override
        protected void applyMessage(GameClient gameClient, MessBase m) {
            // 服务端成功载入客户端的存档资料
            if (m instanceof MessPlayLoadSavedActorResult) {
                MessPlayLoadSavedActorResult mess = (MessPlayLoadSavedActorResult) m;
                if (mess.isSuccess()) {
                    playService.setMainPlayer(playService.findActor(mess.getActorId()));
                    setUIVisiable(true);
                } else {
                    // 这是在服务端没有客户端的存档的情况下，客户端需要弹出角色选择面板，来选择一个角色进行游戏
                    showSelectPanel(gameClient.getGameData().getAvailableActors());
                }
                return;
            }
            
            // 服务端成功载入客户端所选择的角色
            if (m instanceof MessPlayActorSelectResult) {
                MessPlayActorSelectResult mess = (MessPlayActorSelectResult) m;
                if (mess.isSuccess()) {
                    playService.setMainPlayer(playService.findActor(mess.getActorId()));
                    setUIVisiable(true);
                } else {
                    LOG.log(Level.SEVERE, "Could not load selected Actor, error={0}", mess.getError());
                }
                return;
            }
            
            super.applyMessage(gameClient, m);
        }
        
        
    }
}
