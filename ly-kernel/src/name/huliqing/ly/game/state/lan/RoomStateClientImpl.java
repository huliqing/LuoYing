/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.state.lan;

import name.huliqing.core.network.GameClient;
import name.huliqing.core.network.Network;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.LY;
import name.huliqing.ly.Fighter;
import name.huliqing.core.data.GameData;
import name.huliqing.ly.game.state.ClientPlayState;
import name.huliqing.core.network.GameServer.ServerState;
import name.huliqing.core.game.mess.MessPlayGetGameData;
import name.huliqing.core.data.ConnData;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.ly.game.view.HelpView;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.state.UIState;

/**
 * 客户端在房间里面,进入房间后才开始连接
 * @author huliqing
 */
public class RoomStateClientImpl extends AbstractAppState implements RoomState {
    private final static Logger LOG = Logger.getLogger(RoomStateClientImpl.class.getName());
    private Fighter app; 
    
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
        float width = LY.getSettings().getWidth();
        float height = LY.getSettings().getHeight();
        app = (Fighter) _app;
        
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
                gameClient = Network.getInstance().createGameClient(roomData.getHost(), roomData.getPort());
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
        if (startGame) {
            LOG.log(Level.INFO, "RoomStateClientImpl.startGame");
            ClientPlayState clientPlayState = new ClientPlayState(app, gameClient);
            app.changeState(clientPlayState);
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
        ServerState state = gameClient.getServerState();
        helpPanel.clearViews();
        if (state == ServerState.waiting) {
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
        protected void onGameDataLoaded(GameClient gameClient, GameData gameData) {
            super.onGameDataLoaded(gameClient, gameData);
            clientPanel.setGameData(gameData);
            checkToStart(gameClient);
        }
        
        @Override
        protected void processClientDisconnected(GameClient gameClient, Client client, ClientStateListener.DisconnectInfo info) {
            back();
        }

        @Override
        protected void processClientMessage(GameClient gameClient, Client source, Message m) {
            // ignore
        }

        @Override
        public void update(float tpf, GameClient gameClient) {
            // ignore
        }

        @Override
        protected void onServerStateChange(final GameClient gameClient, final ServerState newState) {
            super.onServerStateChange(gameClient, newState);
            updateHelp();
            checkToStart(gameClient);
        }
        
        // 检查是否应该开始游戏，当客户端连接到服务端时，这个时候服务端存在三种可能
        // 的状态：
        // 1.在房间中等待其它玩家进入
        // 2.在载入游戏过程中（这个时候可能阻塞）
        // 3.已经在玩游戏
        // 根据这三种状态应该确定如何开始游戏,不要出现bug.
        private void checkToStart(GameClient gameClient) {
            ServerState state = gameClient.getServerState();
            if (state == ServerState.loading || state == ServerState.running) {
                // 这种情况发生在当服务端正阻塞在loading时，客户端刚好请求连接，这时服务端无法响应客户端的连接
                // 可能发送不了gameData,就会造成gameData==null, 这种情况需要重新请求GameData,然后
                // 交由onGameDataLoaded接收数据时再开始(startGame)
                if (gameClient.getGameData() == null) {
                    gameClient.send(new MessPlayGetGameData());
                } else {
                    startGame = true;
                    updateHelp();
                }
            }
        }

        @Override
        protected void onClientsUpdated(GameClient gameClient, List<ConnData> clients) {
            super.onClientsUpdated(gameClient, clients); 
            clientPanel.setClients(clients);
        }
    }
}
