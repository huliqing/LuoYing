/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.play;

import name.huliqing.fighter.game.state.lan.GameClient;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.game.state.lan.DefaultClientListener;
import name.huliqing.fighter.game.state.lan.GameClient.ClientState;
import name.huliqing.fighter.game.state.lan.Network;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.PlayObject;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class LanClientPlayState extends LanPlayState implements DefaultClientListener.PingListener{
    // 客户端
    private GameClient gameClient;
    private LanClientListener clientListener;
    
    // 用于显示与服务端的Ping值信息
    private Text pingLabel;

    public LanClientPlayState(GameClient gameClient) {
        super(gameClient.getGameData());
        this.gameClient = gameClient;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        addObject(Network.getInstance(), false);
        
        // 1.设置listener后立即标记为ready，因为接收到的message最终是放在同步队列中处理的。
        clientListener = new LanClientListener(app);
        clientListener.addPingListener(this);
        gameClient.setGameClientListener(clientListener);
        gameClient.setClientState(ClientState.ready);
        
        // 2.向服务端发送情况后再加载场景是没有问题的
        initScene(gameClient.getGameData().getSceneData());
        
        // 3.循环检查服务端状态，确保服务端已经准备好连接
        PlayObject checkState = new IntervalLogic(0.2f) {
            @Override
            protected void doLogic(float tpf) {
                if (gameClient.getClientState() != ClientState.running) {
                    clientListener.checkToStartClientInit(gameClient);
                } else {
                    // 在状态任务完成后移除，避免占用资源
                    removeObject(this);
                }
            }
        };
        addObject(checkState, false);
        
        // ==== 先隐藏所有UI,这样不会妨碍角色选择界面
        setUIVisiable(false);
        // 客户端不应该显示客户端列表的控制按钮，即客户端不应该有“踢玩家”之类的权限
        ui.getClientsWin().setBtnPanelVisiable(false);
        
        pingLabel = new Text("PING:00");
        pingLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
        pingLabel.setMargin(0, 0, pingLabel.getWidth(), 0);
        pingLabel.setToCorner(Corner.RB);
        addObject(pingLabel, true);
    }

    @Override
    public List<MessPlayClientData> getClients() {
        if (clientListener != null) {
            List<MessPlayClientData> clients = clientListener.getClients();
            return clients;
        }
        return null;
    }

    @Override
    public void kickClient(int connId) {
        Logger.getLogger(LanClientPlayState.class.getName()).log(Level.WARNING
                , "LanClientPlayState could supported kickClient, connId={0}", connId);
    }
    
    @Override
    public void cleanup() {
        Network.getInstance().cleanup();
        gameClient.cleanup();
        gameClient = null;
        super.cleanup();
    }

    @Override
    public void onPingUpdate(long ping) {
        pingLabel.setText("PING:" + ping);
    }
    
}
