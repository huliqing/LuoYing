/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.network.AbstractClientListener;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.mess.network.GetGameDataMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.mess.network.GameDataMess;
import name.huliqing.luoying.mess.network.ServerStateMess;
import name.huliqing.luoying.object.game.GameAppState;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.view.HelpView;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.LyConfig;
import name.huliqing.ly.Start;
import name.huliqing.ly.object.game.ClientNetworkRpgGame;
//import name.huliqing.ly.LyClientPlayState;

/**
 * 客户端在房间里面,进入房间后才开始连接
 * @author huliqing
 */
public class RoomStateClientImpl extends AbstractAppState implements RoomState {
    private final static Logger LOG = Logger.getLogger(RoomStateClientImpl.class.getName());
    private Start app; 
    
    // Client list
    private RoomStatePanel clientPanel;
    // 帮助面板
    private LinearLayout helpPanel;
    private HelpView helpStateCheck;    // 正在
    private HelpView helpWaitForStart;
    private HelpView helpLoading;
    
    // Button
    private RoomStateBtnPanel btnPanel;
    
    // 当前房间信息
    private RoomData roomData;
    // 用于标记是否开始了游戏，如果标记为true，则在cleanup的时候不应该清理client
    private boolean startGame;
    private GameClient gameClient;
    
    public RoomStateClientImpl(RoomData roomData) {
        this.roomData = roomData;
    }
    
    public RoomStateClientImpl(GameClient gameClient) {
        if (gameClient == null) {
            throw new NullPointerException("GameClient could not be null!");
        }
        this.gameClient = gameClient;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application _app) {
        super.initialize(stateManager, app); 
        float width = LuoYing.getSettings().getWidth();
        float height = LuoYing.getSettings().getHeight();
        app = (Start) _app;
        
        float btnHeight = UIFactory.getUIConfig().getButtonHeight() * 1.5f;
        float helpHeight = UIFactory.getUIConfig().getButtonHeight(); 
        float clientHeight = height - btnHeight - helpHeight;
        
        try {
            // UI
            clientPanel = new RoomStatePanel(width, clientHeight);
            
            // helpPanel
            helpPanel = new LinearLayout(width, helpHeight);
            helpPanel.setMargin(10, 0, 0, 0);
            helpStateCheck = new HelpView(width, helpHeight, ResourceManager.get("lan.help.stateCheck"));
            helpWaitForStart = new HelpView(width, helpHeight, ResourceManager.get("lan.help.waitForStart"));
            helpLoading = new HelpView(width, helpHeight, ResourceManager.get("lan.help.loading"));
            helpPanel.addView(helpStateCheck);
            
            btnPanel = new RoomStateBtnPanel(width, btnHeight, this);
            // 客户端不允许“开始游戏”或“踢人”
            btnPanel.getBtnStart().setVisible(false);
            btnPanel.getBtnKick().setVisible(false);
            
            LinearLayout localUIRoot = new LinearLayout(width, height);
            localUIRoot.addView(clientPanel);
            localUIRoot.addView(helpPanel);
            localUIRoot.addView(btnPanel);
            UIState.getInstance().addUI(localUIRoot);
            
            // 客户端连接开始
            if (gameClient == null) {
                gameClient = Network.getInstance().createGameClient(
                        LyConfig.getGameName()
                        , LyConfig.getVersionCode()
                        , roomData.getHost(), roomData.getPort());
            }
            gameClient.setGameClientListener(new RoomClientListener(app));
            gameClient.start();
        } catch (Exception ex) {
            back();
            Logger.getLogger(RoomStateClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Network.getInstance().update(tpf);
        
        if (startGame) {
            LOG.log(Level.INFO, "RoomStateClientImpl.startGame");

            // remove201611xx
//            ClientPlayState clientPlayState = new LyClientPlayState(app, gameClient);
//            app.changeState(clientPlayState);

            ClientNetworkRpgGame clientRpgGame = new ClientNetworkRpgGame(gameClient);
            GameAppState gameAppState = new GameAppState(clientRpgGame);
            app.changeState(gameAppState);
        }
    }

    @Override
    public void startGame() {
        // 客户端不允许主动开始游戏,不能让客户端主动调用这个方法。所以在界面中要屏蔽
    }

    @Override
    public void kickClient() {
        // 客户端不允许踢人
    }

    @Override
    public void back() {
        app.changeLanState();
    }
    
    @Override
    public void cleanup() {
        gameClient.setGameClientListener(null);
        if (!startGame) {
            // 如果没有开始游戏（如退出或被踢），则应该关闭client，以释放资源
            Network.getInstance().cleanup();
        }
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
    private void updateHelp() {
        helpPanel.clearViews();
        if (gameClient.getServerState() == ServerState.waiting) {
            helpPanel.addView(helpWaitForStart);
        } else if (startGame) {
            helpPanel.addView(helpLoading);
        } else {
            helpPanel.addView(helpStateCheck);
        }
    }

    private class RoomClientListener extends AbstractClientListener {

        public RoomClientListener(Application app) {
            super(app);
        }

        @Override
        protected void onReceiveMessGameData(GameClient gameClient, GameDataMess mess) {
            super.onReceiveMessGameData(gameClient, mess);
            clientPanel.setGameData(mess.getGameData());
            checkToStart(gameClient);
        }
        
        @Override
        protected void onReceiveMessServerState(final GameClient gameClient, ServerStateMess mess) {
            super.onReceiveMessServerState(gameClient, mess);
            updateHelp();
            checkToStart(gameClient);
        }

        @Override
        protected void onReceiveMessClients(GameClient gameClient, ClientsMess mess) {
            super.onReceiveMessClients(gameClient, mess); 
            clientPanel.setClients(mess.getClients());
        }
        
        // 检查是否应该开始游戏，当客户端连接到服务端时，这个时候服务端存在三种可能
        // 的状态：
        // 1.在房间中等待其它玩家进入
        // 2.在载入游戏过程中（这个时候可能阻塞）
        // 3.已经在玩游戏
        // 根据这三种状态应该确定如何开始游戏,不要出现bug.
        private void checkToStart(GameClient gameClient) {
            ServerState serverState = gameClient.getServerState();
            if (serverState == ServerState.loading || serverState == ServerState.running) {
                // 这种情况发生在当服务端正阻塞在loading时，客户端刚好请求连接，这时服务端无法响应客户端的连接
                // 可能发送不了gameData,就会造成gameData==null, 这种情况需要重新请求GameData,然后
                // 交由onGameDataLoaded接收数据时再开始(startGame)
                if (gameClient.getGameData() == null) {
                    gameClient.send(new GetGameDataMess());
                } else {
                    startGame = true;
                    updateHelp();
                }
            }
        }
        
        @Override
        protected void onClientDisconnected(GameClient gameClient, ClientStateListener.DisconnectInfo info) {
            back();
        }
        
        @Override
        protected void onGameInitialized() {
        }
        
        @Override
        protected void onReceiveGameMess(GameClient gameClient, Message m) {
            // ignore
        }


    }
}
