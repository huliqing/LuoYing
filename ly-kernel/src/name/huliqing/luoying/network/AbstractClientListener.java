/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import com.jme3.app.Application;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener.DisconnectInfo;
import com.jme3.network.Message;
import name.huliqing.luoying.mess.MessSCGameData;
import name.huliqing.luoying.mess.MessSCClientList;
import name.huliqing.luoying.mess.MessClient;
import name.huliqing.luoying.mess.MessPlayGetServerState;
import name.huliqing.luoying.mess.MessSCServerState;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.network.GameClient.ClientListener;
import name.huliqing.luoying.network.GameServer.ServerState;
import name.huliqing.luoying.mess.MessPing;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.layer.service.SystemService;

/**
 * 客户端帧听器,用于监听来自服务端的消息。
 * @author huliqing
 */
public abstract class AbstractClientListener implements ClientListener {
    private static final Logger LOG = Logger.getLogger(AbstractClientListener.class.getName());
    
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final SystemService systemService = Factory.get(SystemService.class);
    
    private final Application app; 
    // 从服务端获得的所有客户端列表
    protected final List<ConnData> clients = new ArrayList<ConnData>();
    
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
    private final MessPing messPing = new MessPing();
    
    public AbstractClientListener(Application app) {
        this.app = app;
    }

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
    public final  void clientConnected(final GameClient gameClient, final Client client) {
        // 当连接到服务端以后，如果服端器这时已经在玩游戏，则客户端直接开始游戏。
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                processClientConnected(gameClient, client);
                return null;
            }
        });
    }

    @Override
    public final void clientDisconnected(final GameClient gameClient,  final Client client, final DisconnectInfo info) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                processClientDisconnected(gameClient, client, info);
                return null;
            }
        });
    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        if (pingListerners.size() <= 0) {
            return;
        }
        // 每隔一段时间向服务端发送Ping消息
        pingTimeUsed += tpf;
        if (pingTimeUsed > pingTimeInterval) {
            pingTimeUsed = 0;
//            messPing.time = System.nanoTime();
            messPing.setTime(System.nanoTime());
            gameClient.send(messPing);
        }
    }

    @Override
    public final void clientMessage(final GameClient gameClient, final Client client, final Message m) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                if (m instanceof MessSCGameData) {
                    MessSCGameData mess = (MessSCGameData) m;
                    onGameDataLoaded(gameClient, mess.getGameData());
                    
                } else if (m instanceof MessSCServerState) {
                    MessSCServerState mess = (MessSCServerState) m;
                    onServerStateChange(gameClient, mess.getServerState());
                    
                } else if (m instanceof MessSCClientList) {
                    MessSCClientList mess = (MessSCClientList) m;
                    onClientsUpdated(gameClient, mess.getClients());
                    
                } else if (m instanceof MessPing) {
                    // 测试Ping值
                    MessPing mess = (MessPing) m;
                    onUpdatePing(gameClient, mess);
                }else {
                    processClientMessage(gameClient, client, m);
                }
                return null;
            }
        });
    }
    
    // Ping值测试
    protected void onUpdatePing(GameClient gameClient, MessPing mess) {
        if (pingListerners.size() > 0) {
            for (PingListener pl : pingListerners) {
                // 注意要把纳秒转换为毫秒
                long ping = (long) ((System.nanoTime() - (long) mess.getTime()) * (1.0f / 1000000L));
                pl.onPingUpdate(ping);
            }
        }
    }
    
    /**
     * 从服务端获得游戏数据，在当客户端连接到服务端之后立即发生，这保证在执
     * 行其它命令之前就已经获得了来自服务端的游戏数据。
     * @param gameClient
     * @param gameData 
     */
    protected void onGameDataLoaded(GameClient gameClient, GameData gameData) {
        gameClient.setGameData(gameData);
    }
    
    /**
     * 当服务端游戏状态发生变化时
     * @param gameClient
     * @param newState 
     */
    protected void onServerStateChange(GameClient gameClient, ServerState newState) {
        gameClient.setServerState(newState);
    }
    
    /**
     * 当客户端列表发生变更时,这包含新客户端连接，断开，客户端信息更新,如机器
     * 标识等。
     * @param gameClient
     * @param clients 
     */
    protected void onClientsUpdated(GameClient gameClient, List<ConnData> clients) {
        this.clients.clear();
        this.clients.addAll(clients);
    }
    
    /**
     * 当有一个新的客户端连接到服务端调用该方法。
     * @param gameClient
     * @param client 
     */
    protected void processClientConnected(GameClient gameClient, Client client) {
        // 1.连接上服务端后立即发送客户端标识
        MessClient mess = new MessClient(configService.getClientId(), systemService.getMachineName());
        client.send(mess);

        // 2.从服务端获得当前游戏状态
        client.send(new MessPlayGetServerState());
    }
    
    /**
     * 当有一个客户端断开连接时，这个方法发生在客户端断开后调用。
     * @param gameClient
     * @param client
     * @param info 
     */
    protected abstract void processClientDisconnected(GameClient gameClient, Client client, DisconnectInfo info);
    
    /**
     * 处理服务端传来的消息
     * @param gameClient
     * @param source
     * @param m 
     */
    protected abstract void processClientMessage(GameClient gameClient, Client source, Message m);
}
