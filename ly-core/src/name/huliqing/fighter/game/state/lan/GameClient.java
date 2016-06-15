/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.state.lan.GameServer.ServerState;

/**
 * 客户端程序，注：不要直接通过new GameClient创建客户端，而是通过 {@link Network#createGameClient(java.lang.String, int) }
 * 方式来统一创建。
 * @author huliqing
 */
public class GameClient implements ClientStateListener, MessageListener<Client>{
    private final static Logger LOG = Logger.getLogger(GameClient.class.getName());
    
    public interface ClientListener {
    
        /**
         * 客户端连接时
         * @param gameClient
         * @param client 
         */
        void clientConnected(GameClient gameClient, Client client);

        /**
         * 客户端断开
         * @param gameClient
         * @param client
         * @param info 
         */
        void clientDisconnected(GameClient gameClient, Client client, DisconnectInfo info);

        /**
         * 客户端处理从服务端传递过来的消息
         * @param gameClient
         * @param client
         * @param m 
         */
        void clientMessage(GameClient gameClient, Client client, Message m);
        
        void update(float tpf, GameClient gameClient);
    }
    
    public enum ClientState {
        /**
         * 客户端处于等待状态
         */
        waitting,
        
        /**
         * 客户端正在载入初始场景
         */
        loading,
        
        /**
         * 客户端已经装备好
         */
        ready,
        
        /**
         * 正在等待初始化场景
         */
        waitting_init_game,
        
        /**
         * 客户端正在运行游戏
         */
        running;
    }
    
    private Client client;
    private ClientListener listener;
    private ServerState serverState = ServerState.waiting;
    // 客户端状态
    private ClientState clientState = ClientState.waitting;
    // 游戏数据
    private GameData gameData;
    // 客户端的已经运行时间，单位秒.
    public double time;
    
    private final String gameName;
    private final int version;
    private final String host;
    private final int hostPort;
    
    GameClient(String gameName, int version, String host, int hostPort)
        throws Exception {
        this.gameName = gameName;
        this.version = version;
        this.host = host;
        this.hostPort = hostPort;
        
        // remove20160206
//        client = Network.connectToServer(gameName, version, host, hostPort);
//        client.addClientStateListener(this);
//        client.addMessageListener(this);
    }
    
    public void start() throws IOException {
        if (client != null && client.isConnected()) {
            return;
        }
        
        client = Network.connectToServer(gameName, version, host, hostPort);
        client.addClientStateListener(this);
        client.addMessageListener(this);
        
        client.start();
    }
    
    /**
     * 清理客户端，清理后客户端将不再可用。
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
    
    public boolean isConnected() {
        return client.isConnected();
    }
    
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
        if (listener != null) {
            listener.clientConnected(this, c);
        }
    }

    @Override
    public void clientDisconnected(Client c, final DisconnectInfo info) {
        if (listener != null) {
            listener.clientDisconnected(this, c, info);
        }
    }

    @Override
    public void messageReceived(Client source, Message m) {
        if (listener != null) {
            listener.clientMessage(this, source, m);
        }
    }
    
    public void update(float tpf) {
        time += tpf;
        if (listener != null) {
            listener.update(tpf, this);
        }
    }
}
