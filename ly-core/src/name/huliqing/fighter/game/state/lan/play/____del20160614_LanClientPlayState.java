///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.play;
//
//import name.huliqing.fighter.game.state.lan.GameClient;
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.game.state.lan.DefaultClientListener;
//import name.huliqing.fighter.game.state.lan.GameClient.ClientState;
//import name.huliqing.fighter.game.state.lan.GameServer;
//import name.huliqing.fighter.game.state.lan.Network;
//import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
//import name.huliqing.fighter.game.state.lan.mess.MessPlayGetClients;
//import name.huliqing.fighter.game.state.lan.mess.MessPlayGetServerState;
//import name.huliqing.fighter.game.state.lan.mess.MessPlayInitGame;
//import name.huliqing.fighter.object.IntervalLogic;
//import name.huliqing.fighter.object.PlayObject;
//import name.huliqing.fighter.ui.Text;
//import name.huliqing.fighter.ui.UI.Corner;
//import name.huliqing.fighter.ui.UIFactory;
//
///**
// *
// * @author huliqing
// */
//public class LanClientPlayState extends LanPlayState implements DefaultClientListener.PingListener{
//    // 客户端
//    private GameClient gameClient;
//    private LanClientListener clientListener;
//    
//    // 用于显示与服务端的Ping值信息
//    private Text pingLabel;
//
//    public LanClientPlayState(GameClient gameClient) {
//        super(gameClient.getGameData());
//        this.gameClient = gameClient;
//    }
//
//    @Override
//    public void initialize(AppStateManager stateManager, Application app) {
//        super.initialize(stateManager, app);
//        addObject(Network.getInstance(), false);
//        
//        // 重要，客户端是不需要执行游戏逻辑的，只需要载入场景及执行场景逻辑就可以。
//        game.setEnabled(false);
//        
//        // 1.设置listener后立即标记为ready，因为接收到的message最终是放在同步队列中处理的。
//        clientListener = new LanClientListener(app);
//        clientListener.addPingListener(this);
//        gameClient.setGameClientListener(clientListener);
//        gameClient.setClientState(ClientState.ready);
//        
//        // 3.循环检查服务端状态，确保服务端已经准备好连接
//        PlayObject checkState = new IntervalLogic(0.2f) {
//            @Override
//            protected void doLogic(float tpf) {
//                if (gameClient.getClientState() != ClientState.running) {
////                    clientListener.checkToStartClientInit(gameClient); // remove20160613
//                    checkToStartClientInit();
//                } else {
//                    // 在状态任务完成后移除，避免占用资源
//                    removeObject(this);
//                }
//            }
//        };
//        addObject(checkState, false);
//        
//        // ==== 先隐藏所有UI,这样不会妨碍角色选择界面
//        setUIVisiable(false);
//        // 客户端不应该显示客户端列表的控制按钮，即客户端不应该有“踢玩家”之类的权限
//        ui.getClientsWin().setBtnPanelVisiable(false);
//        
//        pingLabel = new Text("PING:00");
//        pingLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
//        pingLabel.setMargin(0, 0, pingLabel.getWidth(), 0);
//        pingLabel.setToCorner(Corner.RB);
//        addObject(pingLabel, true);
//    }
//    
//    /**
//     * 开始向服务端发起初始化游戏数据的请求。注：该方法会检查客户端和服务端
//     * 的状态，只有在确认客户端处于ClientState.ready和服务端处于ServerState.running
//     * 时才发起请求，在发起请求后，客户端将转入waiting_init_game状态，以避免重复发送请
//     * 求。
//     */
//    public void checkToStartClientInit() {
//        if (gameClient.getClientState() == ClientState.ready && gameClient.getServerState() == GameServer.ServerState.running) {
//            // 获取初始场景信息,注：确保这个命令只发送一次。
//            gameClient.setClientState(ClientState.waitting_init_game);
//            gameClient.send(new MessPlayInitGame());
//            
//            // 从服务端重新获取所有客户端联接信息，因为gameClient重新设置了listener,
//            // 并且clientsWin需要初始化这部分信息，否则客户端进入后打开看不到列表，除非有新客户端连接。
//            // 所以这里应该主动获取一次，进行初始化
//            gameClient.send(new MessPlayGetClients());
//            
//            // 显示角色选择面板
//            showSelectPanel(gameClient.getGameData().getAvailableActors());
//        } else {
//            // 请求服务端状态
//            gameClient.send(new MessPlayGetServerState());
//        }
//    }
//
//    @Override
//    public List<MessPlayClientData> getClients() {
//        if (clientListener != null) {
//            List<MessPlayClientData> clients = clientListener.getClients();
//            return clients;
//        }
//        return null;
//    }
//
//    @Override
//    public void kickClient(int connId) {
//        Logger.getLogger(LanClientPlayState.class.getName()).log(Level.WARNING
//                , "LanClientPlayState could supported kickClient, connId={0}", connId);
//    }
//    
//    @Override
//    public void cleanup() {
//        Network.getInstance().cleanup();
//        
//        // remove20160613,已经在Networ中清理
////        gameClient.cleanup();
////        gameClient = null;
//
//        super.cleanup();
//    }
//
//    @Override
//    public void onPingUpdate(long ping) {
//        pingLabel.setText("PING:" + ping);
//    }
//    
//}
