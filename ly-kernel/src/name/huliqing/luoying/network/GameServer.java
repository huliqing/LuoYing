/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.mess.network.ServerStateMess;
import name.huliqing.luoying.network.discover.MessCSFindServer;
import name.huliqing.luoying.network.discover.MessCSPing;
import name.huliqing.luoying.network.discover.UDPDiscover;
import name.huliqing.luoying.network.discover.MessSCStarted;
import name.huliqing.luoying.network.discover.MessSCClosed;
import name.huliqing.luoying.network.discover.UDPListener;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.mess.BaseMess;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 服务端程序，注：不要直接通过new GameServer创建服务端，而是通过 {@link Network#createGameServer(name.huliqing.fighter.data.GameData) }
 * 方式来统一创建。
 * @author huliqing
 */
public class GameServer implements UDPListener, ConnectionListener, MessageListener<HostedConnection> {

    private static final Logger LOG = Logger.getLogger(GameServer.class.getName());
    
    private final SystemService envService = Factory.get(SystemService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    public interface ServerListener {
        
        /**
         * 当一个客户端成功连接到服务端时该方法被调用。
         * @param gameServer 
         * @param conn 
         */
        void clientAdded(GameServer gameServer, HostedConnection conn);
        
        /**
         * 当客户端断开与服务端的连接时该方法被调用。
         * @param gameServer 
         * @param conn 
         */
        void clientRemoved(GameServer gameServer, HostedConnection conn);
        
        /**
         * 处理服务端接收到的消息
         * @param gameServer
         * @param source 客户端连接
         * @param mess 
         */
        void messageReceived(GameServer gameServer, HostedConnection source, Message mess);
        
        /**
         * 添加一个同步实体，当添加后，这个实体将在运行时持续保持与客户端的同步，如：位移、旋转、缩放等.
         * 具体同步的频率、同步的方式由子类实现决定。
         * 直到主动将实体移除 {@link #removeSyncEntity(Entity) }
         * @param syncObject 
         */
        void addSyncEntity(Entity syncObject);
        
        /**
         * 移除一个自动同步的实体,移除后，这个物体将不再与客户端保持同步。
         * @param syncObject
         * @return 
         * @see #addSyncEntity(Entity) 
         */
        boolean removeSyncEntity(Entity syncObject);
        
        /**
         * 更新逻辑
         * @param tpf
         * @param gameServer 
         */
        void update(float tpf, GameServer gameServer);
        
        /**
         * 清理
         */
        void cleanup();
    }
    
    public enum ServerState {

        /** 服务端正在等待连接中,游戏未开始运行,如在房间中等待 */
        waiting,

        /** 
         * 服务端已经开始，但还未准备好与客户端进行游戏数据交互。
         * 这个时间段客户端发送过来的游戏指令都可能被忽略。
         */
        loading,

        /** 服务端游戏处在运行时 */
        running,
        
        /** 服务器处于关闭、未开启状态 */
        shutdown,
    }
    
    // 游戏状态
    public final static int GAME_STATE_WAIT = 0;
    public final static int GAME_STATE_RUN = 1;
    
    private final Server server;
    // 当前游戏状态
    private ServerState serverState = ServerState.waiting;
    // 侦听器
    private ServerListener listener;
    // 服务端已经运行的时间长度，单位秒.
    // 这个时间必须大于客户端的时间gameClient.time,否则当客户端和服务端在局域网
    // 游戏中同时开始游戏时,由于机器性能的差异,客户端可能比服务端的time值要大.这
    // 会造成一些问题,比如客户端在计算offset值的时候会出现一些问题,导致客户端无法
    // 正常初始化场景角色.所以这里预设一个初始值,以确保服务端的运行时间始终大于
    // 客户端(GameClient.time)的运行时间.
    private double time = 60 * 60 * 24 * 7; // 这里是一周的时间
    // 判断GameServer是否已经运行
    private boolean started;
    
    // 游戏数据
    private GameData gameData;
    // 游戏名称
    private final String gameName;
    // 游戏版本，如: "落樱2.0"
    private final String versionName;
    // 游戏版本代码，如: 251
    private final int versionCode;
    // 游戏服务端运行端口
    private final int serverPort;
    
    // 是否打开局域网"发现"功能，打开这个功能可以让局域网中客户端查找到主机。
    private boolean lanDiscoverEnabled;
    // Discover运行端口，一个是服务端的discover端口，一个是客户端的discover端口，
    // 服务端与客户端必须保持一致的设置否则可能找不到主机。
    private int lanDiscoverServerPort = 32992;
    private int lanDiscoverClientPort = 32993;
    // 这个discover运行后允许局域网中的客户端查找到服务端, 客户端同样需要一个UDPDiscover,以便双向通知。
    private UDPDiscover serverDiscover;
    
    /**
     * @param gameData 游戏数据
     * @param gameName 游戏名称
     * @param versionName 游戏的版本
     * @param versionCode 游戏版本代码
     * @param serverPort 端服端运行端口
     * @throws IOException 
     */
    public GameServer(GameData gameData, String gameName, String versionName, int versionCode, int serverPort) throws IOException {
        // 不要设置为true,这会导致在Network.createServer创建Server后，第二次再创建时报异常：
        // java.lang.RuntimeException: Serializer registry locked trying to register class:class com.jme3.network.message.SerializerRegistrationsMessage
        // 这个问题在JME3.1发生，在3.0时没有问题。
        Serializer.setReadOnly(false);
        
        this.gameData = gameData;
        this.gameName = gameName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.serverPort = serverPort;
        server = Network.createServer(gameName, versionCode , serverPort, serverPort);
        server.addConnectionListener(this);
        server.addMessageListener(this);
    }
    
    /**
     * 打开或关闭"局域网发现"功能。
     * @param enabled 
     */
    public void setLanDiscoverEnabled(boolean enabled) {
        lanDiscoverEnabled = enabled;
        if (enabled && started && serverDiscover == null) {
            serverDiscover = new UDPDiscover(lanDiscoverServerPort);
            serverDiscover.setListener(this);
            serverDiscover.start();
            serverDiscover.broadcast(createServerRunMess(), lanDiscoverClientPort);
        }
    }
    
    /**
     * 设置”局域网发现“的服务端端口,默认32992，服务端Discover运行时会监听这个端口，
     * 以便获取来自客户端Discover的广播消息，通过向客户端Discover响应消息来回复当前服务端的运行状态。
     * 注：必须打开局域网发现功能。<br>
     * 注2: 在GameServer运行之前设置这个端口
     * @param serverDiscoverPort 
     * @see #setLanDiscoverEnabled(boolean) 
     */
    public void setLanDiscoverServerPort(int serverDiscoverPort) {
        if (started) {
            throw new IllegalStateException("Server discover is running! could not change the discoverPort!");
        }
        lanDiscoverServerPort = serverDiscoverPort;
    }
    
    /**
     * 设置”局域网发现“的客户端端口,默认32993。 服务端Discover将向局域网的这个端口发送广播消息，
     * 以告知局域网中的主机，当前服务端正在运行的消息。这个端口必须与客户端设置的一致。
     * 注：必须打开局域网发现功能。<br>
     * 注2: 在GameServer运行之前设置这个端口
     * @param clientDiscoverPort 
     * @see #setLanDiscoverEnabled(boolean) 
     */
    public void setLanDiscoverClientPort(int clientDiscoverPort) {
        if (started) {
            throw new IllegalStateException("Server discover is running! could not change the discoverPort!");
        }
        lanDiscoverClientPort = clientDiscoverPort;
    }
    
    /**
     * 判断是否有客户端连接
     * @return 
     */
    public final boolean hasConnections() {
        return server.hasConnections();
    }
    
    public void start() {
        if (started) {
            LOG.log(Level.WARNING, "GameServer is running...");
            return;
        }
        serverState = ServerState.waiting;
        server.start();
        started = true;
        // 服务端发出广播，这样局域内的其它客户端可以看到服务器启动
        if (lanDiscoverEnabled) {
            serverDiscover = new UDPDiscover(lanDiscoverServerPort);
            serverDiscover.setListener(this);
            serverDiscover.start();
            serverDiscover.broadcast(createServerRunMess(), lanDiscoverClientPort);
        }
        
        LOG.log(Level.INFO
                , "Server created: game={0}, version={1}, tcpPort={2}, udpPort={3}, running={4}"
                , new Object[] {server.getGameName()
                        , server.getVersion()
                        , serverPort
                        , serverPort
                        , server.isRunning()});
    }
    
    /**
     * 关闭服务端并清理资源
     */
    public void cleanup() {
        if (listener != null) {
            listener.cleanup();
            listener = null;
        }
        if (server.isRunning()) {
            Collection<HostedConnection> hcs = server.getConnections();
            for (HostedConnection hc : hcs) {
                hc.close(ResManager.get("lan.serverClosed"));
            }
            // 这里必须延迟一下，否则客户端收不到close的消息, 可能是由于Server线程导致的延迟。
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            server.close();
        }
        serverState = ServerState.shutdown;
        server.removeConnectionListener(this);
        server.removeMessageListener(this);
        if (serverDiscover != null && serverDiscover.isRunning()) {
            serverDiscover.broadcast(createServerStopMess(), lanDiscoverClientPort);
            serverDiscover.close();
            serverDiscover = null;
        }
        started = false;
    }
        
    public boolean isRunning() {
        return started;
    }

    public Server getServer() {
        return server;
    }
    
    /**
     * 设置服务器端状态，该状态会同时立即广播到所有客户端
     * @param state 
     */
    public void setServerState(ServerState state) {
        this.serverState = state;
        // 通知所有已经连接到当前Server的客户端
        broadcast(new ServerStateMess(state));
        // 向局域网广播消息，这是向所有未连接到当前server的广播。因为状态发生变化，所以也广播
        // 让局域网知道
        if (lanDiscoverEnabled && serverDiscover != null && serverDiscover.isRunning()) {
            serverDiscover.broadcast(createServerRunMess(), lanDiscoverClientPort);
        }
    }

    public ServerState getServerState() {
        return serverState;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    
    public void setServerListener(ServerListener listener) {
        this.listener = listener;
    }

    public ServerListener getListener() {
        return listener;
    }
    
    /**
     * 踢出一个客户端
     * @param connId 客户端连接ID
     * @param message 原因
     */
    public void kickClient(int connId, String message) {
        HostedConnection hc = server.getConnection(connId);
        if (hc != null) {
            hc.close(message);
        }
    }
    
    /**
     * 广播所有信息给客户端
     * @param message 
     */
    public final void broadcast(BaseMess message) {
        message.setTime(time);
        server.broadcast(message);
    }
    
    /**
     * 发送给指定的客户端
     * @param conn
     * @param message 
     */
    public final void send(HostedConnection conn, BaseMess message) {
        message.setTime(time);
        conn.send(message);
    }
    
    // remove20161126,不要依赖Entity
//    /**
//     * 发送信息给特定角色所在的客户端,如果找不到指定的客户端则什么也不做。
//     * @param actor
//     * @param message 
//     */
//    public void send(Entity actor, MessBase message) {
//        if (!server.isRunning())
//            return;
//        
//        Collection<HostedConnection> conns = server.getConnections();
//        HostedConnection conn = null;
//        ConnData cd;
//        for (HostedConnection hc : conns) {
//            cd = hc.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
//            if (cd != null && cd.getEntityId() == actor.getData().getUniqueId()) {
//                conn = hc;
//                break;
//            }
//        }
//        
//        if (conn == null)
//            return;
//        
//        message.setTime(time);
//        conn.send(message);
//        if (Config.debug) {
//            Logger.getLogger(GameServer.class.getName()).log(Level.INFO
//                    , "send custom message to client,actor={0}, message={1}"
//                    , new Object[] {actor.getData().getId(), message});
//        }
//    }
    
    /**
     * xxx 这个方法要移动到其它地方:AbstractServerListener。
     * 获取当前已经连接的所有客户端,其中包含主机
     * @return 
     */
    public List<ConnData> getClients() {
        // 需要判断游戏是否在运行,在游戏运行时可以获取到玩家角色名字
        boolean gameInPlay = playService.getGame() != null;
        
        // 向客户端广播，告诉所有客户端有新的客户端连接进来，并把客户端列表
        // 发送给所有已经连接的客户端
        Collection<HostedConnection> hcs = server.getConnections();
        List<ConnData> clients = new ArrayList<ConnData>();
        
        for (HostedConnection hc : hcs) {
            ConnData cd = hc.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            clients.add(cd);
        }
        
        // 添加主机信息到客户端列表中,注：在没有网络的情况下getLocalHostIPv4可能会返回null,这时需要判断处理
        InetAddress inetAddress = envService.getLocalHostIPv4();
        String serverAddress = inetAddress != null ? inetAddress.getHostAddress() : "0.0.0.0";
        String serverMachineName = envService.getMachineName();
        ConnData serverConnData = new ConnData();
        serverConnData.setClientId(configService.getClientId());
        serverConnData.setClientName(serverMachineName);
        serverConnData.setConnId(-1);
        serverConnData.setAddress(serverAddress);
        if (gameInPlay) {
            // xxx 重构
            serverConnData.setEntityId(-1);
            serverConnData.setEntityName("--");
        }
        clients.add(0, serverConnData);
        return clients;
    }
    
    
    @Override
    public void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception {
        if (object instanceof MessCSFindServer) {
            // 客户端在查找服务端,如果主机在运行，则响应该信息，告诉客户端，当前服务器打开着
            MessSCStarted mess = createServerRunMess();
            discover.send(mess, packet.getAddress().getHostAddress(), packet.getPort());
            
        } else if (object instanceof MessCSPing) {
            // 如果接收到客户端的Ping消息，则直接返回该消息，什么也不操作
            discover.send((MessCSPing) object, packet.getAddress().getHostAddress(), packet.getPort());
        }
    }

    @Override
    public void connectionAdded(Server server, final HostedConnection conn) {
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                listener.clientAdded(GameServer.this, conn);
                return null;
            }
        });
    }

    @Override
    public void connectionRemoved(Server server, final HostedConnection conn) {
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                listener.clientRemoved(GameServer.this, conn);
                return null;
            }
        });
    }

    @Override
    public void messageReceived(final HostedConnection source, final Message m) {
//        LOG.log(Level.INFO, "GameServer receive message={0}", m.getClass().getSimpleName());
        
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() {
                try {
                    listener.messageReceived(GameServer.this, source, m);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Message error on server!", e);
                }
                return null;
            }
        });
    }
    
    /**
     * 更新GameServer逻辑
     * @param tpf 
     */
    public void update(float tpf) {
        time += tpf;
        if (listener != null) {
            listener.update(tpf, this);
        }
    }
    
    // 创建“服务端开启”的通知消息
    private MessSCStarted createServerRunMess() {
        InetAddress inetAddress = envService.getLocalHostIPv4();
        if (inetAddress == null) {
            LOG.log(Level.WARNING, "Could not getLocalHostIPv4 address");
            return null;
        }
        MessSCStarted mess = new MessSCStarted(inetAddress.getHostAddress()
                , serverPort
                , versionName
                , versionCode
                , envService.getMachineName()
                , gameName
                , serverState);
        return mess;
    }
    
    // 创建“服务端停止”的通知消息
    private MessSCClosed createServerStopMess() {
        InetAddress inetAddress = envService.getLocalHostIPv4();
        if (inetAddress == null) {
            LOG.log(Level.WARNING, "Could not getLocalHostIPv4 address");
            return null;
        }
        String des = gameData != null ? ResManager.get(gameData.getId() + ".name") : "Unknow";
        return new MessSCClosed(inetAddress.getHostAddress()
                , serverPort
                , versionName
                , versionCode
                , envService.getMachineName()
                , des
                , serverState);
    }
}
