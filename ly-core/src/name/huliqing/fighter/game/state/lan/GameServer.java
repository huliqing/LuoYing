/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import name.huliqing.fighter.game.state.lan.mess.MessPlayClientId;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.game.state.lan.mess.MessSCServerState;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.ActorConstants;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.EnvService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.discover.MessCSFindServer;
import name.huliqing.fighter.game.state.lan.discover.MessCSPing;
import name.huliqing.fighter.game.state.lan.discover.UDPDiscover;
import name.huliqing.fighter.game.state.lan.discover.MessSCStarted;
import name.huliqing.fighter.game.state.lan.discover.MessSCClosed;
import name.huliqing.fighter.game.state.lan.discover.UDPListener;
import name.huliqing.fighter.game.state.lan.mess.MessBase;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 服务端程序，注：不要直接通过new GameServer创建服务端，而是通过 {@link Network#createGameServer(name.huliqing.fighter.data.GameData) }
 * 方式来统一创建。
 * @author huliqing
 */
public class GameServer implements UDPListener, ConnectionListener, MessageListener<HostedConnection> {
    private final EnvService envService = Factory.get(EnvService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    
    // 客户端连接的机器名称属性名
    public final static String ATTR_CLIENT_ID = "ATTR_CLIENT_ID";
    // 客户端所控制的角色的唯一ID
    public final static String ATTR_ACTOR_UNIQUE_ID = "ATTR_ACTOR_UNIQUE_ID";
    
    public interface ServerListener<T> {
        
        /**
         * 客户端添加时
         * @param gameServer 
         */
        void clientAdded(GameServer gameServer, HostedConnection conn);
        
        /**
         * 客户端离开时
         * @param gameServer 
         */
        void clientRemoved(GameServer gameServer, HostedConnection conn);
        
        /**
         * 处理服务端接收到的消息
         * @param gameServer
         * @param source 客户端连接
         * @param mess 
         */
        void serverMessage(GameServer gameServer, HostedConnection source, Message mess);
        
        /**
         * 添加一个同步OBJECT
         * @param syncObject 
         */
        void addSyncObject(T syncObject);
        
        /**
         * 移除一个自动同步的object
         * @param syncObject
         * @return 
         */
        boolean removeSyncObject(T syncObject);
        
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
        
        /** 服务器处于关闭状态 */
        shutdown,

        /** 服务端正在等待连接中,游戏未开始运行,如在房间中等待 */
        waiting,

        /** 
         * 服务端已经开始，但还未准备好与客户端进行游戏数据交互。
         * 这个时间段客户端发送过来的游戏指令都可能被忽略。
         */
        loading,

        /** 服务端游戏处在运行时 */
        running
    }
    
    // 游戏状态
    public final static int GAME_STATE_WAIT = 0;
    public final static int GAME_STATE_RUN = 1;
    
    private Server server;
    // 这个discover允许服务端在开始游戏之后仍然能让客户端知道服务器在运行，
    // 这允许游戏运行以后仍能让客户端连接进来
    private UDPDiscover serverDiscover;
    // 当前游戏状态
    private ServerState serverState = ServerState.waiting;
    // 游戏数据
    private GameData gameData;
    // 侦听器
    private ServerListener listener;
    // 服务端已经运行的时间长度，单位秒.
    // 这个时间必须大于客户端的时间gameClient.time,否则当客户端和服务端在局域网
    // 游戏中同时开始游戏时,由于机器性能的差异,客户端可能比服务端的time值要大.这
    // 会造成一些问题,比如客户端在计算offset值的时候会出现一些问题,导致客户端无法
    // 正常初始化场景角色.所以这里预设一个初始值,以确保服务端的运行时间始终大于
    // 客户端(GameClient.time)的运行时间.
    public double time = 60 * 60 * 24 * 7; // 这里是一周的时间
    
    GameServer(GameData gameData) throws IOException {
        this.gameData = gameData;
        
        // 不要设置为true,这会导致在Network.createServer创建Server后，第二次再创建时报异常：
        // java.lang.RuntimeException: Serializer registry locked trying to register class:class com.jme3.network.message.SerializerRegistrationsMessage
        // 这个问题在JME3.1发生，在3.0时没有问题。
        Serializer.setReadOnly(false);
        
        server = Network.createServer(configService.getGameName()
                , configService.getVersionCode()
                , configService.getPort()
                , configService.getPort());
        server.addConnectionListener(this);
        server.addMessageListener(this);
        
        serverDiscover = new UDPDiscover(configService.getPortDiscoverServer());
        serverDiscover.setListener(this);
        
    }
    
    /**
     * 判断是否有客户端连接
     * @return 
     */
    public boolean hasConnections() {
        return server.hasConnections();
    }
    
    public void start() {
        if (server.isRunning()) {
            return;
        }
        serverState = ServerState.waiting;
        server.start();
        // 服务端发出广播，这样局域内的其它客户端可以看到服务器启动
        serverDiscover.start();
        serverDiscover.broadcast(createServerRunMess(), configService.getPortDiscoverClient());
        
        if (Config.debug) {
            Logger.getLogger(GameServer.class.getName()).log(Level.INFO
                    , "Server created: game={0}, version={1}, tcpPort={2}, udpPort={3}, running={4}"
                    , new Object[] {server.getGameName()
                            , server.getVersion()
                            , configService.getPort()
                            , configService.getPort()
                            , server.isRunning()});
        }
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
                hc.close(ResourceManager.get("lan.serverClosed"));
            }
            // 这里必须延迟一下，否则客户端收不到close的消息
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            server.close();
        }
        serverState = ServerState.shutdown;
        server.removeConnectionListener(this);
        server.removeMessageListener(this);
        serverDiscover.broadcast(createServerStopMess(), configService.getPortDiscoverClient());
        serverDiscover.close();
    }

    /**
     * 设置服务器端状态，该状态会同时立即广播到所有客户端
     * @param state 
     */
    public void setServerState(ServerState state) {
        this.serverState = state;
        // 通知所有已经连接到当前Server的客户端
        broadcast(new MessSCServerState(state));
        // 向局域网广播消息，这是向所有未连接到当前server的广播。因为状态发生变化，所以也广播
        // 让局域网知道
        serverDiscover.broadcast(createServerRunMess(), configService.getPortDiscoverClient());
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
    
    public boolean isRunning() {
        return server.isRunning();
    }
    
    /**
     * 广播所有信息给客户端
     * @param message 
     */
    public void broadcast(Message message) {
        if (message instanceof MessBase) {
            ((MessBase) message).time = time;
        }
        server.broadcast(message);
    }
    
    /**
     * 发送给指定的客户端
     * @param conn
     * @param message 
     */
    public void send(HostedConnection conn, Message message) {
        if (message instanceof MessBase) {
            ((MessBase) message).time = time;
        }
        conn.send(message);
    }
    
    /**
     * 发送信息给特定角色所在的客户端,如果找不到指定的客户端则什么也不做。
     * @param actor
     * @param message 
     */
    public void send(Actor actor, Message message) {
        Integer connId = actor.getModel().getUserData(ActorConstants.USER_DATA_CLIENT_CONNECTION_ID);
        if (connId == null || !server.isRunning())
            return;
        HostedConnection conn = server.getConnection(connId);
        if (conn == null)
            return;
        if (message instanceof MessBase) {
            ((MessBase) message).time = time;
        }
        conn.send(message);
        if (Config.debug) {
            Logger.getLogger(GameServer.class.getName()).log(Level.INFO
                    , "send custom message to client,actor={0}, message={1}"
                    , new Object[] {actor.getData().getId(), message});
        }
    }
    
    /**
     * 获取当前已经连接的所有客户端,其中包含主机
     * @return 
     */
    public List<MessPlayClientData> getClients() {
        // 需要判断游戏是否在运行,在游戏运行时可以获取到玩家角色名字
        boolean gameInPlay = Common.getPlayState() != null;
        
        // 向客户端广播，告诉所有客户端有新的客户端连接进来，并把客户端列表
        // 发送给所有已经连接的客户端
        Collection<HostedConnection> hcs = server.getConnections();
        List<MessPlayClientData> clients = new ArrayList<MessPlayClientData>();
        for (HostedConnection hc : hcs) {
            // 客户端名称
            MessPlayClientId clientId = hc.getAttribute(ATTR_CLIENT_ID);
            String clientName = clientId != null ? clientId.getClientName() : "unknow";
            
            // 客户端所选的角色名称,这里需要判断服务端游戏是否正在运行
            String actorName = null;
            Long actorId = hc.getAttribute(ATTR_ACTOR_UNIQUE_ID);
            if (actorId != null && gameInPlay) {
                Actor actor = playService.findActor(actorId.longValue());
                if (actor != null) {
                    actorName = actor.getData().getName();
                }
            }
            MessPlayClientData mcd = new MessPlayClientData();
            mcd.setConnId(hc.getId());
            mcd.setAddress(hc.getAddress());
            mcd.setName(clientName);
            mcd.setActorName(actorName);
            clients.add(mcd);
        }
        // 添加主机信息到客户端列表中,注：在没有网络的情况下getLocalHostIPv4可能会返回null,这时需要判断处理
        InetAddress inetAddress = envService.getLocalHostIPv4();
        String serverAddress = inetAddress != null ? inetAddress.getHostAddress() : "0.0.0.0";
        String serverMachineName = envService.getMachineName();
        MessPlayClientData serverMcd = new MessPlayClientData(-1, serverAddress, serverMachineName);
        if (gameInPlay) {
            Actor serverPlayer = playService.getPlayer();
            if (serverPlayer != null) {
                serverMcd.setActorName(serverPlayer.getData().getName());
            }
        }
        clients.add(0, serverMcd);
        return clients;
    }
    
    // -------------------------------------------------------------------------
    
    // 创建“服务端开启”的通知消息
    private MessSCStarted createServerRunMess() {
        InetAddress inetAddress = envService.getLocalHostIPv4();
        if (inetAddress == null) {
            Logger.getLogger(GameServer.class.getName()).log(Level.WARNING, "Could not getLocalHostIPv4 address");
            return null;
        }
        String des = gameData != null ? ResourceManager.getObjectName(gameData) : "Unknow";
        MessSCStarted mess = new MessSCStarted(inetAddress.getHostAddress()
                , configService.getPort()
                , configService.getVersionName()
                , envService.getMachineName()
                , des
                , serverState);
        return mess;
    }
    
    // 创建“服务端停止”的通知消息
    private MessSCClosed createServerStopMess() {
        InetAddress inetAddress = envService.getLocalHostIPv4();
        if (inetAddress == null) {
            Logger.getLogger(GameServer.class.getName()).log(Level.WARNING, "Could not getLocalHostIPv4 address");
            return null;
        }
        String des = gameData != null ? ResourceManager.getObjectName(gameData) : "Unknow";
        return new MessSCClosed(inetAddress.getHostAddress()
                , configService.getPort()
                , configService.getVersionName()
                , envService.getMachineName()
                , des
                , serverState);
    }

    //    @Override
    //    public AbstractMess receive(String code, String content) throws Exception {
    //        int c = Integer.parseInt(code);
    //        // 客户端在查找服务端,如果主机在运行，则响应该信息，告诉客户端，当前服务器
    //        // 正在运行。
    //        if (c == Code.CODE_CS_FIND_SERVER) {
    //            //Logger.getLogger(GameServer.class.getName()).log(Level.INFO, "Receive message from client, code={0}", c);
    //            if (server.isRunning()) {
    //                MessSCStarted mess = createServerRunMess();
    //                mess.setState(serverState);
    //                return mess;
    //            }
    //        } else if (c == Code.CODE_CS_PING) {
    //            MessCSPing ping = new MessCSPing();
    //            ping.decodeContent(content);
    //        }
    //        return null;
    //    }
    
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
    public void connectionAdded(Server server, HostedConnection conn) {
        if (listener != null) {
            listener.clientAdded(this, conn);
        }
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        if (listener != null) {
            listener.clientRemoved(this, conn);
        }
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        if (listener != null) {
            listener.serverMessage(this, source, m);
        }
    }
    
    public void update(float tpf) {
        time += tpf;
        if (listener != null) {
            listener.update(tpf, this);
        }
    }
    
}
