/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.network.RequestGameInitMess;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.game.GameAppState;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.view.HelpView;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.Start;
import name.huliqing.luoying.network.DefaultServerListener;
import name.huliqing.ly.LyConfig;
import name.huliqing.ly.object.game.ServerNetworkRpgGame;
//import name.huliqing.ly.LyLanServerPlayState;

/**
 * 服务端在房间里面
 * @author huliqing
 */
public class RoomStateServerImpl extends AbstractAppState implements RoomState {
    private final static Logger LOG = Logger.getLogger(RoomStateClientImpl.class.getName());
    // 客户端连接的机器名称属性名
    public final static String ATTR_MACHINE_NAME = "machineName";
    private Start app;
    
    // 客户端连接列表
    private RoomStatePanel clientPanel;
    
    // 帮助
    private LinearLayout helpPanel;
    private HelpView helpInServerRoom; // lan.help.inServerRoom
    private HelpView helpLoading;
    
    // 控制按钮
    private RoomStateBtnPanel btnPanel;

    // 游戏关卡地图信息
    private final GameData gameData;
    // 标记是否开始了游戏
    private boolean startGame;
    
    private GameServer gameServer;
    
    public RoomStateServerImpl(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        float width = LuoYing.getSettings().getWidth();
        float height = LuoYing.getSettings().getHeight();
        this.app = (Start) app;
        
        try {
            // ---- 界面UI
            float btnHeight = UIFactory.getUIConfig().getButtonHeight() * 1.5f;
            float helpHeight = UIFactory.getUIConfig().getButtonHeight();
            float clientHeight = height - btnHeight - helpHeight;
            
            clientPanel = new RoomStatePanel(width, clientHeight);
            clientPanel.setGameData(gameData);
            
            helpPanel = new LinearLayout(width, helpHeight);
            helpPanel.setMargin(10, 0, 0, 0);
            helpInServerRoom = new HelpView(width, helpHeight, ResourceManager.get("lan.help.inServerRoom"));
            helpLoading = new HelpView(width, helpHeight, ResourceManager.get("lan.help.loading"));
            helpPanel.addView(helpInServerRoom);
            
            btnPanel = new RoomStateBtnPanel(width, btnHeight, this);
            
            LinearLayout localUIRoot = new LinearLayout(width, height);
            localUIRoot.addView(clientPanel);
            localUIRoot.addView(helpPanel);
            localUIRoot.addView(btnPanel);
            UIState.getInstance().addUI(localUIRoot);
            
            // 创建服务端
            gameServer = Network.getInstance().createGameServer(gameData, LyConfig.getGameName()
                    , LyConfig.getVersionName(), LyConfig.getVersionCode(), LyConfig.getServerPort());
            gameServer.setServerListener(new LanRoomServerListener(app));
            // 打开局域网广播功能
            gameServer.setLanDiscoverEnabled(true);
            // 开启服务端
            gameServer.start();
            
            // 刷新客户端列表
            refreshClients();
            
        } catch (Exception ex) {
            Logger.getLogger(RoomStateServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (startGame) {
            LOG.log(Level.INFO, "RoomStateServerImpl.startGame");
            gameServer.setServerListener(null);
            gameServer.setServerState(ServerState.loading);
            
            // remove2016xxxx
//            app.changeState(new LyLanServerPlayState(app, gameServer));

            ServerNetworkRpgGame game = Loader.load(gameData);
            game.setGameServer(gameServer);
            GameAppState gameApp = new GameAppState(game);
            app.changeState(gameApp);
        }
    }
    
    @Override
    public void startGame() {
        // 开始游戏
        startGame = true;
        
        // help tip
        helpPanel.clearViews();
        helpPanel.addView(helpLoading);
    }

    @Override
    public void kickClient() {
        ConnData clientData = clientPanel.getSelected();
        if (clientData == null) {
            return;
        }
        gameServer.kickClient(clientData.getConnId(), "Kick");
    }

    /**
     * 返回局域网房间列表,返回之前需要关闭已经创建的服务器及监听器
     */
    @Override
    public void back() {
        // 在切换到lan状态时 cleanup会自动被调用,所以serverDiscover和server会被关闭
        app.changeLanState();
    }
    
    @Override
    public void cleanup() {
        gameServer.setServerListener(null);
        // 如果创建了游戏后没有开始，则应该在退出的时候关闭discover和server
        if (!startGame) {
            Network.getInstance().cleanup();
        }
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
    /**
     * 刷新客户端列表
     */
    private void refreshClients() {
        // 2.刷新服务端本地列表
        List<ConnData> clients = gameServer.getClients();
        clientPanel.setClients(clients);
    }
    
    private class LanRoomServerListener extends DefaultServerListener {

        public LanRoomServerListener(Application app) {
            super(app);
        }

        @Override
        public void update(float tpf, GameServer gameServer) {}

        @Override
        public void cleanup() {}

        @Override
        protected void onReceiveMessRequestGameInit(GameServer gameServer, HostedConnection conn
                , RequestGameInitMess mess) {
            // 游戏未开始，不需要处理
        }
        
        @Override
        protected void onClientsUpdated(GameServer gameServer) {
            super.onClientsUpdated(gameServer);
            refreshClients();
        }

        @Override
        protected void onReceiveGameMess(GameServer gameServer, HostedConnection source, GameMess m) {}
        
    }
    
}
