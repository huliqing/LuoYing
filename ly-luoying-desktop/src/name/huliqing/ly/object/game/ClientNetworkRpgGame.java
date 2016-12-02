/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.game;

import com.jme3.app.Application;
import com.jme3.network.ClientStateListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.ActorSelectMess;
import name.huliqing.luoying.mess.ActorSelectResultMess;
import name.huliqing.luoying.mess.network.ClientExitMess;
import name.huliqing.luoying.mess.network.GetClientsMess;
import name.huliqing.luoying.mess.ActorLoadSavedMess;
import name.huliqing.luoying.mess.ActorLoadSavedResultMess;
import name.huliqing.luoying.mess.SceneLoadedMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.network.AbstractClientListener.PingListener;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameClient.ClientState;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.save.ShortcutsSave;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.luoying.network.DefaultClientListener;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.progress.Progress;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.view.shortcut.ShortcutManager;

/**
 * 局域网模型下的客户端
 * @author huliqing
 */
public class ClientNetworkRpgGame extends NetworkRpgGame implements PingListener{
    private static final Logger LOG = Logger.getLogger(ClientNetworkRpgGame.class.getName());
    
    private final SaveService saveService = Factory.get(SaveService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    // 客户端快捷方式,保存键，格式: SHORTCUTS_KEY_PREFIX + actorId
    // 如： "Shortcuts_actorWolf", 注意，使用的是角色类型ID，不是唯一ID。
    private final static String SHORTCUTS_KEY_PREFIX = "Shortcuts_";
    
    // 客户端
    private GameClient gameClient;
    private DefaultClientListener clientListener;
    
    // 用于显示与服务端的Ping值信息
    private Text pingLabel;
    
    private boolean loadingScene = true;
    private int needInitEntities;
    private int initEntitiesCount;
    private Progress progress;
    private boolean clientStarted;
    
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
        setUIVisiable(false);
        
        clientListener = new NetworkClientListener();
        clientListener.addPingListener(this);
        gameClient.setGameClientListener(clientListener);
        gameClient.setClientState(ClientState.ready);
         
        // 添加Ping UI
        pingLabel = new Text("PING:00");
        pingLabel.setFontSize(UIFactory.getUIConfig().getDesSize());
        pingLabel.setMargin(0, 0, pingLabel.getWidth(), 0);
        pingLabel.setToCorner(Corner.RB);
        UIState.getInstance().addUI(pingLabel);
        
        String progressId = scene.getData().getProgress();
        if (progressId != null) {
            progress = Loader.load(progressId);
            progress.initialize(guiScene.getRoot());
        }
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
        gameClient.send(new ClientExitMess());
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
    
    @Override
    protected void onSelectPlayer(EntityData entityData) {
        network.sendToServer(new ActorSelectMess(entityData));
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

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        super.onSceneEntityAdded(scene, entityAdded);
        if (loadingScene) {
            initEntitiesCount++;
            LOG.log(Level.INFO, "GameInit...initEntitiesCount={0}, need={1}", new Object[] {initEntitiesCount, needInitEntities});
            if (progress != null) {
                progress.display((float)initEntitiesCount/ needInitEntities);
            }
            if (initEntitiesCount >= needInitEntities) {
                startClientGame();
            }
        }
    }
    
    protected void startClientGame() {
        if (clientStarted) {
            return;
        }
        clientStarted = true;
        loadingScene = false;

        if (progress != null) {
            progress.cleanup();
        }

        // 从服务端重新获取所有客户端联接信息，因为gameClient重新设置了listener,
        // 并且clientsWin需要初始化这部分信息，否则客户端进入后打开看不到列表，除非有新客户端连接。
        // 所以这里应该主动获取一次，进行初始化
        gameClient.send(new GetClientsMess());

        // 偿试发送消息给服务端，看看有没有客户端的存档资料，如果存在资料就不需要选择新角色进行游戏了。
        // （在故事模式下即可能存在客户端的存档资料）
        gameClient.send(new ActorLoadSavedMess());
    }
    
    private class NetworkClientListener extends DefaultClientListener {

        @Override
        protected void onGameInitialize(int initEntityTotal) {
            LOG.log(Level.INFO, "GameInit...needInitEntityTotal={0}", initEntityTotal);
            needInitEntities = initEntityTotal;
        }
        
        @Override
        protected void processGameMess(GameClient gameClient, GameMess m) {
            // 服务端成功载入客户端的存档资料
            if (m instanceof ActorLoadSavedResultMess) {
                ActorLoadSavedResultMess mess = (ActorLoadSavedResultMess) m;
                if (mess.isSuccess()) {
                    gameService.setPlayer(playService.getEntity(mess.getActorId()));
                    setUIVisiable(true);
                } else {
                    // 这是在服务端没有客户端的存档的情况下，客户端需要弹出角色选择面板，来选择一个角色进行游戏
                    showSelectPanel();
                }
                return;
            }
            
            // 服务端成功载入客户端所选择的角色
            if (m instanceof ActorSelectResultMess) {
                ActorSelectResultMess mess = (ActorSelectResultMess) m;
                if (mess.isSuccess()) {
                    gameService.setPlayer(playService.getEntity(mess.getActorId()));
                    setUIVisiable(true);
                } else {
                    LOG.log(Level.SEVERE, "Could not load selected Actor, error={0}", mess.getError());
                }
                return;
            }
            
            super.processGameMess(gameClient, m);
        }
        
        @Override
        protected void onReceiveMessClients(GameClient gameClient, ClientsMess mess) {
            super.onReceiveMessClients(gameClient, mess);
            // 通知客户端列表更新，注：这里只响应新连接或断开连接。不包含客户端资料的更新。
            onClientListUpdated();
        }

        // 断开、踢出、服务器关闭等提示
        @Override
        protected void onClientDisconnected(GameClient gameClient, ClientStateListener.DisconnectInfo info) {
            String message = ResManager.get("lan.disconnected");
            if (info != null) {
                message += "(" + info.reason + ")";
            }
            gameService.addMessage(message, MessageType.notice);
        }
        
    }
    
    
    
}
