/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import java.util.List;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.game.state.lan.GameServer.ServerState;
import name.huliqing.fighter.game.mess.MessPlayClientData;
import name.huliqing.fighter.object.game.Game.GameListener;

/**
 * 局域网模式下的游戏服务端。
 * @author huliqing
 */
public class ServerPlayState extends LanPlayState  {
//    private static final Logger LOG = Logger.getLogger(LanServerPlayState.class.getName());
    
    // 服务端是否准备好
    private final GameServer gameServer;
    
    public ServerPlayState(Application app, GameServer gameServer) {
        super(app, gameServer.getGameData());
        this.gameServer = gameServer;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // 设置listener后立即转为running状态,
        gameServer.setServerListener(new LanServerListener(app));
        
        // 显示可选角色面板
        setUIVisiable(false);
        gameState.getGame().addListener(new GameListener() {
            @Override
            public void onSceneLoaded() {
                showSelectPanel(gameState.getGame().getData().getAvailableActors());
            }
        });
        
        // 设置状态并向所有客户端广播,这通知所有客户端
        // 可以开始向服务端发送游戏初始化命令的消息
        gameServer.setServerState(ServerState.running);
    }
    
    @Override
    public void cleanup() {
        super.cleanup(); 
    }

    @Override
    public void changeGameState(GameData gameData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MessPlayClientData> getClients() {
        return gameServer.getClients();
    }

    @Override
    public void kickClient(int connId) {
        gameServer.kickClient(connId, "kick!");
    }
    
}
