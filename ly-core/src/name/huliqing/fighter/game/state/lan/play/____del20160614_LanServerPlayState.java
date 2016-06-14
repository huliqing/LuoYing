///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.lan.play;
//
//import java.util.List;
//import com.jme3.app.Application;
//import com.jme3.app.state.AppStateManager;
//import name.huliqing.fighter.game.state.lan.GameServer;
//import name.huliqing.fighter.game.state.lan.GameServer.ServerState;
//import name.huliqing.fighter.game.state.lan.Network;
//import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
//import name.huliqing.fighter.object.game.Game.GameListener;
//
///**
// *
// * @author huliqing
// */
//public class LanServerPlayState extends LanPlayState {
////    private static final Logger LOG = Logger.getLogger(LanServerPlayState.class.getName());
//    
//    // 服务端是否准备好
//    private final GameServer gameServer;
//    
//    public LanServerPlayState(GameServer gameServer) {
//        super(gameServer.getGameData());
//        this.gameServer = gameServer;
//    }
//
//    @Override
//    public void initialize(AppStateManager stateManager, Application app) {
//        super.initialize(stateManager, app);
//        addObject(Network.getInstance(), false);
//        
//        // 设置listener后立即转为running状态,
//        gameServer.setServerListener(new LanServerListener(app));
//        
//        // 显示可选角色面板
//        setUIVisiable(false);
//        game.addListener(new GameListener() {
//            @Override
//            public void onSceneLoaded() {
//                showSelectPanel(gameData.getAvailableActors());
//            }
//        });
//        
//        // 设置状态并向所有客户端广播,这通知所有客户端
//        // 可以开始向服务端发送游戏初始化命令的消息
//        gameServer.setServerState(ServerState.running);
//    }
//
//    @Override
//    public List<MessPlayClientData> getClients() {
//        return gameServer.getClients();
//    }
//
//    @Override
//    public void kickClient(int connId) {
//        gameServer.kickClient(connId, "kick!");
//    }
//
//    @Override
//    public void cleanup() {
//        Network.getInstance().cleanup();
//        
//        // remove20160613,game由PlayManager控制，不需要在这里调用清理
////        game.cleanup(); 
//
//        super.cleanup(); 
//    }
//    
//}
