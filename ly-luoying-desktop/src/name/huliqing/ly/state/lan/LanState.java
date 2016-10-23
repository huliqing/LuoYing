/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.network.discover.UDPDiscover;
import name.huliqing.luoying.network.discover.MessCSFindServer;
import name.huliqing.luoying.network.discover.MessCSPing;
import name.huliqing.luoying.network.discover.MessSCStarted;
import name.huliqing.luoying.network.discover.MessSCClosed;
import name.huliqing.luoying.network.discover.UDPListener;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.ly.view.HelpView;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.utils.ThreadHelper;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.UIUtils;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.view.IpAddressPanel;
import name.huliqing.ly.Start;

/**
 * 局域网络,这里主要显示房间列表，创建房间，加入房间，退出等按钮
 * @author huliqing
 */
public class LanState extends AbstractAppState {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private Start app;
    
    // 房间列表
    private LanStateRoomListPanel roomPanel;
    // 手动连接面板
    private IpAddressPanel manualPanel;
    
    // 帮助提示
    private LinearLayout helpPanel;
    // 提示:当前无人创建游戏
    private HelpView helpNoGame;
    // 提示:当前有人创建游戏
    private HelpView helpHaveGame;
    
    // 局域网控制按钮
    private LanStateBtnPanel btnPanel;
    // 用于监听获得房间列表信息
    private UDPDiscover clientDiscover;
    
    // 手动连接
    private Future<GameClient> manualFuture;
    
    // 这是一个Ping列表，当发现服务端之后会在这个列表中添加服务端信息，然后
    // PingThread线程会每隔一段时间去Ping测试这个列表中的房间中的Ping值。
    private final List<PingRoom> pingList = new ArrayList<PingRoom>();
    private PingThread pingThread;
    
    public LanState() {}
    
    @Override
    public void initialize(AppStateManager stateManager, Application _app) {
        super.initialize(stateManager, _app);
        float width = LuoYing.getSettings().getWidth();
        float height = LuoYing.getSettings().getHeight();
        app = (Start) _app;
        app.getInputManager().setCursorVisible(true);
        
        float btnHeight = UIFactory.getUIConfig().getButtonHeight() * 1.5f;
        float helpHeight = UIFactory.getUIConfig().getButtonHeight();
        float roomHeight = height - helpHeight - btnHeight;
        
        // 房间列表
        roomPanel = new LanStateRoomListPanel(width, roomHeight);
        
        // 手动连接面板
        manualPanel = new IpAddressPanel(width * 0.5f, height * 0.65f);
        manualPanel.setDragEnabled(true);
        manualPanel.setCloseable(true);
        manualPanel.setToCorner(Corner.CC);
        manualPanel.setConfirmListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                manualConnect(manualPanel.getIpAddress());
                manualPanel.removeFromParent();
            }
        });
        
        // 控制按钮
        btnPanel = new LanStateBtnPanel(width, btnHeight, this);
        
        // 帮助信息
        float helpPanelWidth = width - 20;
        helpPanel = new LinearLayout();
        helpPanel.setWidth(helpPanelWidth);
        helpPanel.setHeight(helpHeight);
        helpPanel.setMargin(10, 0, 0, 0);
        helpNoGame = new HelpView(helpPanelWidth, helpHeight
                , ResourceManager.get("lan.help.noGame"));
        helpHaveGame = new HelpView(helpPanelWidth, helpHeight
                , ResourceManager.get("lan.help.haveGame"
                , new Object[] {configService.getVersionName()}));
        helpPanel.addView(helpNoGame);
        
        // 房间监听器,broadcast messCS1000进行服务端查找。
        clientDiscover = new UDPDiscover(configService.getPortDiscoverClient());
        clientDiscover.setListener(new ClientDiscoverListener());
        clientDiscover.start();
        clientDiscover.broadcast(new MessCSFindServer(), configService.getPortDiscoverServer());
        
        LinearLayout localUIRoot = new LinearLayout(width, height);
        localUIRoot.addView(roomPanel);
        localUIRoot.addView(helpPanel);
        localUIRoot.addView(btnPanel);
        UIState.getInstance().addUI(localUIRoot);
        
        // 起动一个线程，每几秒对Ping列别中的房间进行Ping值测试。 
        pingThread = new PingThread();
        pingThread.start();
    }
    
    // 进入选中的游戏房间
    public void enterRoom() {
        RoomData roomData = roomPanel.getSelected();
        if (roomData == null) 
            return;
        RoomStateClientImpl roomState = new RoomStateClientImpl(roomData);
        app.changeState(roomState);
    }
    
    public void createRoom() {
        app.changeState(new CreateRoomState());
    }
    
    public void backToStart() {
        app.changeStartState();
    }
    
    /**
     * 显示手动连接面板
     */
    public void showManualPanel() {
        UIState.getInstance().addUI(manualPanel);
    }
    
    /**
     * 手动使用IP地址连接到目标主机.
     * @param ipAddress 
     */
    public void manualConnect(final String ipAddress) {
        // remove 不要直接在当前线程连接主机，这会造成UI阻塞，特别是当主机无法识
        // 别时,在超时时间内会使UI一直阻塞。
//        RoomData roomData = new RoomData(
//                ipAddress, configService.getPort()
//                , "", "", "", GameServer.ServerState.waiting);
//        RoomStateClientImpl roomState = new RoomStateClientImpl(roomData);
//        app.changeState(roomState);
        
        // 避免重复连接
        if (manualFuture != null) {
            return;
        }
        
        // 检查目标主机端口是否打开,可避免在主机无法识别时一直阻塞UI
        try {
            Socket client = new Socket(ipAddress, configService.getPort());
            client.close();
        } catch (Exception e) {
            UIUtils.showAlert(ResourceManager.get(ResConstants.COMMON_TIP)
                    ,ResourceManager.get(ResConstants.LAN_SERVER_UNKNOW)
                    ,ResourceManager.get(ResConstants.COMMON_CONFIRM));
            return;
        }
        
        // 连接
        manualFuture = ThreadHelper.submit(new Callable<GameClient>() {
            @Override
            public GameClient call() {
                try {
                    GameClient gameClient = Network.getInstance()
                            .createGameClient(ipAddress, configService.getPort());
                    return gameClient;
                } catch (Exception e) {
                    UIUtils.showAlert(ResourceManager.get(ResConstants.COMMON_TIP)
                        ,ResourceManager.get(ResConstants.LAN_SERVER_UNKNOW)
                        ,ResourceManager.get(ResConstants.COMMON_CONFIRM));
                    return null;
                }
            }
        });
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); 
        if (manualFuture != null && manualFuture.isDone()) {
            try {
                GameClient gameClient = manualFuture.get();
                if (gameClient != null) {
                    RoomStateClientImpl roomState = new RoomStateClientImpl(gameClient);
                    app.changeState(roomState);
                }
            } catch (Exception ex) {
                Logger.getLogger(LanState.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                manualFuture = null;
            }
        }
    }
    
    @Override
    public void cleanup() {
        // 在退出这个状态时必须关闭侦听器
        if (clientDiscover != null) {
            clientDiscover.close();
            clientDiscover = null;
        }
        if (pingThread != null) {
            pingThread.end();
            pingThread = null;
        }
        
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
    private void updateHelpUI(int roomSize) {
        helpPanel.clearViews();
        if (roomSize <= 0) {
            helpPanel.addView(helpNoGame);
        } else {
            helpPanel.addView(helpHaveGame);
        }
    }
    
    // 用于发现服务端
    private class ClientDiscoverListener implements UDPListener {
        
        @Override
        public void receive(final Object object, final UDPDiscover discover, final DatagramPacket packet) throws Exception {;
            app.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    if (object instanceof MessSCStarted) {
                        // 发现服务器后把服务端添加到Room列表中
                        MessSCStarted mess = (MessSCStarted) object;
                        RoomData roomData = new RoomData(mess.getHost()
                                , mess.getPort()
                                , mess.getVersion()
                                , mess.getHostName()
                                , mess.getDes()
                                , mess.getState());
                        roomPanel.addOrUpdateRoom(roomData);
                        
                        // 把服务端房间放入需要进行测试的Ping列表中，以便测试Ping值
                        putPingRoom(roomData, packet.getAddress().getHostAddress()
                                // 注意：这里的端口和roomData中的端口是不一样的。roomData中的端
                                // 口是游戏端口，而这里的端口是Discover（服务端）的UDP监听端口.
                                // 如果发送到roomData端口是不会有响应的。因为还没有连接到游戏。
                                , packet.getPort());
                        
                    } else if (object instanceof MessSCClosed) {
                        // 服务器关闭
                        MessSCClosed mess = (MessSCClosed) object;
                        RoomData roomData = new RoomData(mess.getHost()
                                , mess.getPort()
                                , mess.getVersion(), mess.getHostName(), mess.getDes(), mess.getState());
                        roomPanel.removeRoom(roomData);
                        // 当服务器关闭后从Ping列表中移除，这样就不需要再去Ping这个服务端
                        removePingRoom(roomData);
                        
                    } else if (object instanceof MessCSPing) {
                        // 如果接收到从服务端返回来的ping消息，则更新Ping值
                        MessCSPing mess = (MessCSPing) object;
                        PingRoom pingRoom = updateRoomPing(mess);
                        if (pingRoom != null) {
                            roomPanel.addOrUpdateRoom(pingRoom.roomData);
                        }
                    }
                    
                    // 更新提示信息
                    updateHelpUI(roomPanel.getRoomSize());
                    return null;
                }
            });
        }
    }
    
    // ----- Ping test
    
    // 更新某个房间的Ping值
    private PingRoom updateRoomPing(MessCSPing mess) {
        for (PingRoom pr : pingList) {
            if (pr.pingId == mess.getPingId()) {
                pr.roomData.setPing((int)((System.nanoTime() - pr.pingTime) * (1.0f / 1000000L)));
                return pr;
            }
        }
        return null;
    }
    
    // 把一个房间放入需要进行Ping测试的列表中
    private void putPingRoom(RoomData roomData, String discoverAddress, int discoverPort) {
        for (PingRoom pr : pingList) {
            if (pr.roomData.compare(roomData)) {
                pr.roomData = roomData;
                pr.discoverAddress = discoverAddress;
                pr.discoverPort = discoverPort;
                // 当更新或添加了房间后立即进行一次Ping测试
                sendPing(pr);
                return;
            }
        }
        PingRoom pr = new PingRoom();
        pr.roomData = roomData;
        pr.discoverAddress = discoverAddress;
        pr.discoverPort = discoverPort;
        pingList.add(pr);
        // 当更新或添加了房间后立即进行一次Ping测试
        sendPing(pr);
    }
    
    // 把服务端房间从Ping列表中移除,这样就不需要对这个房间再进行Ping值测试了，一般
    // 当服务端关闭后执行这个方法。
    private void removePingRoom(RoomData roomData) {
        Iterator<PingRoom> it = pingList.iterator();
        while (it.hasNext()) {
            if (it.next().roomData.compare(roomData)) {
                it.remove();
                break;
            }
        }
    }
    
    // 立即发送一次Ping请求
    private synchronized void sendPing(PingRoom pr) {
        MessCSPing pingMess = new MessCSPing();
        pr.pingId = pingMess.getPingId();
        pr.pingTime = System.nanoTime();
        clientDiscover.send(pingMess, pr.discoverAddress, pr.discoverPort);
    }
    
    // 起动一个线程，每几秒对Ping列别中的房间进行Ping值测试。
    private class PingThread extends Thread {
        private boolean started = true;
        // Ping测试的时间间隔，每一次Ping的时候pingId都不一样，所以这个时间间隔不
        // 能太小，因为在这个时间内如果服务端不返回消息，则重新发起测试(PingId会更新）
        // 所以大于这个时间的延迟将不会更新Ping值
        private long interval = 5000;
        public void end() {
            started = false;
        }
        @Override
        public void run() {
            while (started) {
                if (!pingList.isEmpty()) {
                    for (PingRoom pr : pingList) {
                        sendPing(pr);
                    }
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LanState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private class PingRoom {
        RoomData roomData;
        String discoverAddress;
        int discoverPort;
        // PingId标识
        short pingId;
        // 开始执行Ping测试的时间
        long pingTime;
    }
}
