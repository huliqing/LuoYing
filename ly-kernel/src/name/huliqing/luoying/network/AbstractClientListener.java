/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import java.util.ArrayList;
import java.util.List;
import com.jme3.network.ClientStateListener.DisconnectInfo;
import com.jme3.network.Message;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.mess.network.GameDataMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.mess.network.ClientMess;
import name.huliqing.luoying.mess.network.GetServerStateMess;
import name.huliqing.luoying.mess.network.ServerStateMess;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.network.GameClient.ClientListener;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.mess.network.PingMess;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.network.RequestGameInitMess;
import name.huliqing.luoying.mess.network.RequestGameInitStartMess;
import name.huliqing.luoying.network.GameClient.ClientState;

/**
 * 客户端帧听器,用于监听来自服务端的消息。
 * @author huliqing
 */
public abstract class AbstractClientListener implements ClientListener { 
    private static final Logger LOG = Logger.getLogger(AbstractClientListener.class.getName());
    
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final SystemService systemService = Factory.get(SystemService.class);
    
    // 用于检查服务端状态
    private final Detector detector = new Detector();
    
    // 从服务端获得的所有客户端列表
    protected final List<ConnData> clients = new ArrayList<ConnData>();
    
    // 用于判断是否已经向服务端发送了请求初始化游戏场景的消息。
    protected boolean hasRequestGameInit;
    
    // ----- Ping测试
    // Ping测试的时间间隔,单位秒，这个数值不能太小，因为每次发送Ping测试时Ping的
    // 发起时间都会重置，因此如果Ping值大于这个值时将不会测到。一般不要小于2秒.
    private float pingTimeInterval = 3;
    private float pingTimeUsed;
    
    // Ping更新侦听器
    public interface PingListener {
        
        /**
         * 当Ping值更新时调用该方法
         * @param ping 
         */
        void onPingUpdate(long ping);
        
    }
    
    private final List<PingListener> pingListerners = new ArrayList<PingListener>(1);
    
    // 用于向服务端发送的Ping消息
    private final PingMess messPing = new PingMess();
    
    public AbstractClientListener() {}

    /**
     * 获取所有客户端,注：该列表只能用于只读操作,不要手动修改该列表
     * @return 
     */
    public List<ConnData> getClients() {
        return clients;
    }
    
    /**
     * 添加一个Ping值侦听器
     * @param pingListener 
     */
    public void addPingListener(PingListener pingListener) {
        if (!pingListerners.contains(pingListener)) {
            pingListerners.add(pingListener);
        }
    }
    
    /**
     * 移除一个Ping值侦听器
     * @param pingListener 
     */
    public void removePingListener(PingListener pingListener) {
        pingListerners.remove(pingListener);
    }
    
    /**
     * 设置Ping值测试的时间间隔,单位秒，一般不要小于3秒。否则影响性能。
     * @param pingTimeInterval 
     */
    public void setPingTimeInterval(float pingTimeInterval) {
        this.pingTimeInterval = pingTimeInterval;
    }
    
    @Override
    public final  void clientConnected(final GameClient gameClient) {
        onConnected(gameClient);
    }

    @Override
    public final void clientDisconnected(final GameClient gameClient,  final DisconnectInfo info) {
        onClientDisconnected(gameClient, info);
    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        // 判断客户端和服务端是否状态就绪，是否可以开始调用onReady(GameClient)方法。
        if (!hasRequestGameInit) {
            if (gameClient.getClientState() == ClientState.ready && gameClient.getServerState() == ServerState.running) {
                // 请求初始化游戏数据
                gameClient.send(new RequestGameInitMess());
                hasRequestGameInit = true;
                
            } else if (gameClient.getServerState() != ServerState.running) {
                detector.checkServerState(tpf, gameClient);
            }
        }
        
        // 每隔一段时间向服务端发送Ping消息
        if (pingListerners.size() <= 0) {
            return;
        }
        pingTimeUsed += tpf;
        if (pingTimeUsed > pingTimeInterval) {
            pingTimeUsed = 0;
            messPing.sendTime = System.currentTimeMillis();
            gameClient.send(messPing);
        }
    }
    
    @Override
    public final void clientMessage(final GameClient gameClient, final Message m) {
        receiveMessage(gameClient, m);
    }
    
    private void receiveMessage(GameClient gameClient, Message m) {
        
        // 注：只有在客户端状态进入running之后才接收GameMess, 这很重要，因为在客户端处理游戏消息之前需要确保
        // 客户端已经初始化完成，一切准备就绪才可以。如果在这之前处理游戏命令消息可能会导致客户端和服务端的状态不同步。
        if (m instanceof GameMess) {
            if (gameClient.getClientState() == ClientState.running) {
                onReceiveGameMess(gameClient, (GameMess) m);
            } else {
                LOG.log(Level.WARNING, "ClientListener is not ready to process \"GameMess\""
                        + ", because the GameClient is not in \"running\" state! ClientState={0}, message={1}"
                        , new Object[] {gameClient.getServerState(), m});
            }
            return;
        }
        
        // 游戏初始化消息。
        if (m instanceof GameDataMess) {
            onReceiveMessGameData(gameClient, (GameDataMess) m);
            
        } else if (m instanceof ServerStateMess) {
            onReceiveMessServerState(gameClient, (ServerStateMess) m);

        } else if (m instanceof ClientsMess) {
            onReceiveMessClients(gameClient,  (ClientsMess) m);

        }  else if (m instanceof RequestGameInitStartMess) {
            onReceiveGameInitStart(gameClient, (RequestGameInitStartMess) m);
            
        } else if (m instanceof PingMess) {
            onUpdatePing(gameClient, (PingMess) m);
            
        } else {
            
            LOG.log(Level.WARNING, "Unknow message type. message={0}", m.getClass().getName());
            
        }
    }
    
    /**
     * 从服务端获得游戏数据，在当客户端连接到服务端之后立即发生，这保证在执
     * 行其它命令之前就已经获得了来自服务端的游戏数据。
     * @param gameClient
     * @param mess 
     */
    protected void onReceiveMessGameData(GameClient gameClient, GameDataMess mess) {
        gameClient.setGameData(mess.getGameData());
    }
    
    /**
     * 当接收到来自服务端的状态消息时该方法被自动调用。
     * @param gameClient
     * @param mess 
     */
    protected void onReceiveMessServerState(GameClient gameClient, ServerStateMess mess) {
        gameClient.setServerState(mess.getServerState());
    }
    
    /**
     * 当接收到服务端发来的所有客户端列表时该方法被自动调用，一般这意味着服务端列表发生了变化。
     * 该方法主要处理的就是更新客户端列表信息。
     * @param gameClient
     * @param mess 
     */
    protected void onReceiveMessClients(GameClient gameClient, ClientsMess mess) {
        this.clients.clear();
        this.clients.addAll(mess.getClients());
    }
    
    /**
     * 当客户端连接到服务端时该方法被自动调用,默认情况下，该方法即立即向服务端发送客户端的基本标识。MessClient
     * @param gameClient
     */
    protected void onConnected(GameClient gameClient) {
        // 1.连接上服务端后立即发送客户端标识
        ClientMess mess = new ClientMess(configService.getClientId(), systemService.getMachineName());
        gameClient.send(mess);
    }
    
    // 用于检查服务端状态
    private class Detector {
        private final float interval = 0.25f;
        private float intervalUsed;
        public void checkServerState(float tpf, GameClient gameClient) {
            intervalUsed += tpf;
            if (intervalUsed > interval) {
                intervalUsed = 0;
                gameClient.send(new GetServerStateMess());
            }
        }
    }
    
    // Ping值测试
    protected void onUpdatePing(GameClient gameClient, PingMess mess) {
        if (pingListerners.size() > 0) {
            for (PingListener pl : pingListerners) {
                long ping = System.currentTimeMillis() - mess.sendTime;
                pl.onPingUpdate(ping);
            }
        }
    }
    
    /**
     * 当客户端接收到服务端对于初始化请求{@link RequestGameInitMess}的响应时该方法被调用，该方法会将客户端状态从
     * ready转变化running。<br>
     * 当客户端接收到这个消息后，接下来将立即接收到来自服务端的用于初始化场景的实体数据，
     * 连续接收到的实体数量由{@link RequestGameInitStartMess#getInitEntityCount() }中指定。
     * @param gameClient
     * @param mess 
     */
    protected void onReceiveGameInitStart(GameClient gameClient, RequestGameInitStartMess mess) {
        gameClient.setClientState(ClientState.running);
        onGameInitialize(mess.getInitEntityCount());
    }
    
    /**
     * 当客户端断开连接时，这个方法发生在客户端断开与服务端的连接后调用。
     * @param gameClient
     * @param info 
     */
    protected abstract void onClientDisconnected(GameClient gameClient, DisconnectInfo info);
    
    /**
     * 当客户端从ready转入Running状态时该方法被自动调用(一次)，这表明客户端已经完成与服务端的<b>状态初始化</b>,
     * 接下来会接收到来自服务端的初始化场景的实体，数量为initEntityTotal所指定的实体数,这些实体数据是连续发送的，
     * 用于初始化客户端场景，客户端只有在完成了场景实体的初始化之后才可以正式开始游戏。
     * @param initEntityTotal
     */
    protected abstract void onGameInitialize(int initEntityTotal);
    
    /**
     * 运行时处理来自服务端传来的消息, 注：这个方法只有在客户端状态进入running之后才会被调用。
     * @param gameClient
     * @param m 
     */
    protected abstract void onReceiveGameMess(GameClient gameClient, GameMess m);
    

}
