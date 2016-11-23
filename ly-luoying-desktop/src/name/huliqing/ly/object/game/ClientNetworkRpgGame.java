/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.mess.MessBase;
import name.huliqing.luoying.mess.MessPlayActorSelectResult;
import name.huliqing.luoying.mess.MessPlayClientExit;
import name.huliqing.luoying.mess.MessPlayGetClients;
import name.huliqing.luoying.mess.MessPlayGetServerState;
import name.huliqing.luoying.mess.MessPlayLoadSavedActor;
import name.huliqing.luoying.mess.MessPlayLoadSavedActorResult;
import name.huliqing.luoying.network.AbstractClientListener;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameClient.ClientState;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.object.gamelogic.GameLogic;
import name.huliqing.luoying.save.ShortcutsSave;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.mess.MessPlayInitGame;
import name.huliqing.ly.network.LanClientListener;
import name.huliqing.ly.view.shortcut.ShortcutManager;

/**
 * 局域网模型下的客户端
 * @author huliqing
 */
public class ClientNetworkRpgGame extends NetworkRpgGame implements AbstractClientListener.PingListener{
 
    private static final Logger LOG = Logger.getLogger(ClientNetworkRpgGame.class.getName());
    
    private final SaveService saveService = Factory.get(SaveService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    // 客户端快捷方式,保存键，格式: SHORTCUTS_KEY_PREFIX + actorId
    // 如： "Shortcuts_actorWolf", 注意，使用的是角色类型ID，不是唯一ID。
    private final static String SHORTCUTS_KEY_PREFIX = "Shortcuts_";
    
    // 客户端
    private GameClient gameClient;
    private LanClientListener clientListener;
    
    // 用于显示与服务端的Ping值信息
    private Text pingLabel;
    
    public ClientNetworkRpgGame() {}
    
    public ClientNetworkRpgGame(GameClient gameClient) {
        this.gameClient = gameClient;
        // 这里清理掉gameLogic,是因为客户端不需要这些逻辑，这些逻辑只在服务端运行。
        // 如果同时在客户端运行有可能会冲突. 
        // 另外要注意：客户端是可以运行其它gameLogic的。
        this.gameClient.getGameData().getGameLogicDatas().clear();
        this.setData(gameClient.getGameData());
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        clientListener = new ClientListener(app);
        clientListener.addPingListener(this);
        gameClient.setGameClientListener(clientListener);
        gameClient.setClientState(ClientState.ready);
        
        // 添加一个用于检查服务端状态的逻辑
        GameLogic stateCheck = new AbstractGameLogic(0.2f) {
            @Override
            protected void doLogic(float tpf) {
                if (gameClient.getClientState() != ClientState.running) {
                    checkToStartClientInit();
                } else {
                    // 在状态任务完成后移除
                    removeLogic(this);
                }
            }
        };
        addLogic(stateCheck);
        
        // 先隐藏所有UI,这样不会妨碍角色选择界面
//        setUIVisiable(false);
         
        // 添加Ping UI
        pingLabel = new Text("PING:00");
        pingLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
        pingLabel.setMargin(0, 0, pingLabel.getWidth(), 0);
        pingLabel.setToCorner(Corner.RB);
        UIState.getInstance().addUI(pingLabel);
        
    }
    
    @Override
    public void cleanup() {
        onExit();
        super.cleanup();
    }
    
    private void onExit() {
        // 保存快捷方式
        saveClientShortcuts();
        // 在退出前告诉服务端.
        gameClient.send(new MessPlayClientExit());
    }
    
    // 保存客户端的快捷方式到指定存档
    private void saveClientShortcuts() {
        Entity tempPlayer = getPlayer();
        if (tempPlayer == null) 
            return;
         // 保存快捷方式
        ShortcutsSave ss = new ShortcutsSave();
        ss.setShortcuts(ShortcutManager.getShortcutSaves());
        saveService.saveSavable(SHORTCUTS_KEY_PREFIX + tempPlayer.getData().getId(), ss);
    }
    
    /**
     * 开始向服务端发起初始化游戏数据的请求。注：该方法会检查客户端和服务端
     * 的状态，只有在确认客户端处于ClientState.ready和服务端处于ServerState.running
     * 时才发起请求，在发起请求后，客户端将转入waiting_init_game状态，以避免重复发送请
     * 求。
     */
    private void checkToStartClientInit() {
        if (gameClient.getClientState() == ClientState.ready && gameClient.getServerState() == ServerState.running) {
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
    protected void onSelectPlayer(String actorId, String actorName) {
        Entity actor = Loader.load(actorId);
        actor.getData().setName(actorName);
//        userCommandNetwork.addSimplePlayer(actor);
        gameNetwork.addSimplePlayer(actor);
    }
    
    @Override
    public List<ConnData> getClients() {
        if (clientListener != null) {
            return clientListener.getClients();
        }
        return null;
    }
    
    @Override
    public void kickClient(int connId) {
        // ignore,客户端不能踢玩家。
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
//                    playService.setMainPlayer(playService.findActor(mess.getActorId()));
                    gameService.setPlayer(playService.getEntity(mess.getActorId()));
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
                    gameService.setPlayer(playService.getEntity(mess.getActorId()));
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
