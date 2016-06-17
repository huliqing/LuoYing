/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import name.huliqing.fighter.game.state.lan.GameClient;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.DefaultClientListener;
import name.huliqing.fighter.game.state.lan.GameClient.ClientState;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.game.mess.MessBase;
import name.huliqing.fighter.game.mess.MessPlayActorSelectResult;
import name.huliqing.fighter.game.mess.MessPlayClientData;
import name.huliqing.fighter.game.mess.MessPlayGetClients;
import name.huliqing.fighter.game.mess.MessPlayGetServerState;
import name.huliqing.fighter.game.mess.MessPlayInitGame;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActor;
import name.huliqing.fighter.game.mess.MessPlayLoadSavedActorResult;
import name.huliqing.fighter.game.mess.MessPlayClientExit;
import name.huliqing.fighter.game.service.SaveService;
import name.huliqing.fighter.manager.ShortcutManager;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.game.Game.GameListener;
import name.huliqing.fighter.save.ShortcutsSave;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class ClientPlayState extends LanPlayState implements DefaultClientListener.PingListener{
    private static final Logger LOG = Logger.getLogger(ClientPlayState.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final SaveService saveService = Factory.get(SaveService.class);
    // 客户端需要保存角色的快捷方式,格式是这样的： CLIENT_SHORTCUT_SAVE_KEY + "_" + actorUniqueId;
    // 如: "Shortcuts_12203030",即针对不同的角色将使用不同的存档。
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
    public void changeGameState(GameData gameData) {
        // 在切换gameState之前先保存快捷方式
        if (gameState != null) {
            saveClientShortcuts();
        }
        
        // 切换游戏
        super.changeGameState(gameData);
        gameClient.setGameData(gameData);
        gameClient.setClientState(ClientState.ready);
        
        // 重要，客户端是不需要执行游戏逻辑的，只需要载入场景及执行场景逻辑就可以。
        gameState.getGame().setEnabled(false);
        // 在载入场景后才把状态设置为ready,这个状态表示客户端准备就绪 
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                gameClient.setClientState(ClientState.ready);
            }
        });
        
        // 先隐藏所有UI,这样不会妨碍角色选择界面
        setUIVisiable(false);
        
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
        saveService.saveSavable(SHORTCUTS_KEY_PREFIX + player.getData().getUniqueId(), ss);
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
    public List<MessPlayClientData> getClients() {
        if (clientListener != null) {
            return clientListener.getClients();
        }
        return null;
    }

    @Override
    public void setPlayer(Actor actor) {
        super.setPlayer(actor);
        // 先清除，然后再重新生成快捷方式
        ShortcutManager.cleanup();
        // 载入客户端玩家的快捷方式
        ShortcutsSave ss = saveService.loadSavable(SHORTCUTS_KEY_PREFIX + actor.getData().getUniqueId());
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
        protected void applyMessage(GameClient gameClient, MessBase m) {
            // 服务端成功载入客户端的存档资料
            if (m instanceof MessPlayLoadSavedActorResult) {
                MessPlayLoadSavedActorResult mess = (MessPlayLoadSavedActorResult) m;
                if (mess.isSuccess()) {
                    playService.setAsPlayer(playService.findActor(mess.getActorId()));
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
                    playService.setAsPlayer(playService.findActor(mess.getActorId()));
                } else {
                    LOG.log(Level.SEVERE, "Could not load selected Actor, error={0}", mess.getError());
                }
                return;
            }
            
            super.applyMessage(gameClient, m);
        }
        
        
    }
}
