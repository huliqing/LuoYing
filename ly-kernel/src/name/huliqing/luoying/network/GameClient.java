/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import com.jme3.app.Application;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.network.GameServer.ServerState;

/**
 * 客户端程序，注：不要直接通过new GameClient创建客户端，而是通过 {@link Network#createGameClient(java.lang.String, int) }
 * 方式来统一创建。
 * @author huliqing
 */
public class GameClient implements ClientStateListener, MessageListener<Client>{
    private final static Logger LOG = Logger.getLogger(GameClient.class.getName());
    
    public interface ClientListener {
    
        /**
         * 客户端连接到服务端时该方法被调用。
         * @param gameClient
         */
        void clientConnected(GameClient gameClient);

        /**
         * 当客户端断开连接时该方法被调用。
         * @param gameClient
         * @param info 
         */
        void clientDisconnected(GameClient gameClient, DisconnectInfo info);

        /**
         * 客户端处理从服务端传递过来的消息
         * @param gameClient
         * @param m 
         */
        void clientMessage(GameClient gameClient, Message m);
        
        /**
         * 更新逻辑
         * @param tpf
         * @param gameClient 
         */
        void update(float tpf, GameClient gameClient);
    }
    
    public enum ClientState {
        
        /**
         * 客户端处于等待状态, 这个状态表示客户端刚刚连接上服务端，未开始游戏，未准备就绪。这个状态下服务端向客户端
         * 发送游戏行为的命令都不会被客户端接受。
         */
        waitting,
        
        /**
         * 客户端处于准备就绪状态，这个状态表示客户端已经可以开始准备接受初始化游戏场景的命令。
         */
        ready,
        
        /**
         * 客户端处于正常的游戏运行状态.
         */
        running;
    }
    
    private Client client;
    
    // 客户端监听器，用于监听从服务端来的消息，并对消息进行处理。
    private ClientListener listener;
    
    // 服务端的状态
    private ServerState serverState = ServerState.waiting;
    
    // 客户端状态
    private ClientState clientState = ClientState.waitting;
    
    // 游戏数据
    private GameData gameData;
    
    // 客户端的已经运行时间，单位秒.
    public double time;
    
    // 游戏名称
    private final String gameName;
    
    // 游戏版本
    private final int version;
    // 主机地址
    private final String host;
    // 主机端口
    private final int port;
    
    GameClient(String gameName, int versionCode, String host, int port)
        throws Exception {
        this.gameName = gameName;
        this.version = versionCode;
        this.host = host;
        this.port = port;
    }
    
    public void start() throws IOException {
        if (client != null && client.isConnected()) {
            return;
        }
        client = Network.connectToServer(gameName, version, host, port);
        client.addClientStateListener(this);
        client.addMessageListener(this);
        client.start();
    }
    
    /**
     * 关闭、断开客户端连接，并清理和释放资源
     */
    public void cleanup() {
        if (client == null)
            return;
        
        if (client.isConnected()) {
            client.close();
        }
        client.removeClientStateListener(this);
        client.removeMessageListener(this);
        clientState = ClientState.waitting;
    }
    
    public void update(float tpf) {
        time += tpf;
        if (listener != null) {
            listener.update(tpf, this);
        }
    }
    
    /**
     * 判断当前客户端是否正连接着服务端。
     * @return 
     */
    public boolean isConnected() {
        return client.isConnected();
    }
    
    /**
     * 向服务端发送消息,如果客户端处于断开状态，则该方法什么也不会做。
     * @param message 
     */
    public void send(Message message) {
        // 发送消息之前必须判断是否还连接着，否则可能发生异常 
        if (!client.isConnected()) 
            return;
        client.send(message);
    }

    /**
     * 设置侦听器，如果已经存在侦听器，则旧的会被替换掉。
     * @param listener 
     */
    public void setGameClientListener(ClientListener listener) {
        this.listener = listener;
    }

    /**
     * 获取客户端状态信息
     * @return 
     */
    public ClientState getClientState() {
        return clientState;
    }

    /**
     * 设置客户端状态
     * @param clientState 
     */
    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
        if (Config.debug) {
            LOG.log(Level.INFO, "客户端状态发生变化,clientState={0}", this.clientState);
        }
    }

    /**
     * 获取当前服务器端的状态
     * @return 
     */
    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    /**
     * 获取游戏数据，当客户端连接到服务端后，客户端应该从服务端获得当前的游戏
     * 数据
     * @return 
     */
    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    
    @Override
    public void clientConnected(Client c) {
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                listener.clientConnected(GameClient.this);
                return null;
            }
        });
    }

    @Override
    public void clientDisconnected(final Client c, final DisconnectInfo info) {
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                listener.clientDisconnected(GameClient.this, info);
                return null;
            }
        });
    }
    
    @Override
    public void messageReceived(final Client source, final Message m) {
//        LOG.log(Level.INFO, "GameClient receive message={0}", m.getClass().getSimpleName());
        if (listener == null)
            return;
        
        LuoYing.getApp().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                listener.clientMessage(GameClient.this, m);
                return null;
            }
        });
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    
}
