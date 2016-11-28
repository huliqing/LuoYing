/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.mess.network.GameDataMess;
import name.huliqing.luoying.mess.network.ClientsMess;
import name.huliqing.luoying.mess.network.ClientMess;
import name.huliqing.luoying.mess.network.GetServerStateMess;
import name.huliqing.luoying.mess.network.ServerStateMess;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.network.GameServer.ServerListener;
import name.huliqing.luoying.mess.network.GetClientsMess;
import name.huliqing.luoying.mess.network.GetGameDataMess;
import name.huliqing.luoying.data.GameData;
import name.huliqing.luoying.mess.GameMess;
import name.huliqing.luoying.mess.network.PingMess;
import name.huliqing.luoying.mess.network.RequestGameInitMess;
import name.huliqing.luoying.mess.network.RequestGameInitOkMess;
import name.huliqing.luoying.network.GameServer.ServerState;

/**
 * 服务端监听器,用于监听来自客户端连接的消息。
 * @author huliqing
 */
public abstract class AbstractServerListener implements ServerListener {

    private static final Logger LOG = Logger.getLogger(AbstractServerListener.class.getName());

    private final Application app;
    
    public AbstractServerListener(Application app) {
        this.app = app;
    }
    
    @Override
    public final void clientAdded(final GameServer gameServer, final HostedConnection conn) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                onClientAdded(gameServer, conn);
                return null;
            }
        });
    }

    @Override
    public final void clientRemoved(final GameServer gameServer, final HostedConnection conn) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                onClientRemoved(gameServer, conn);
                return null;
            }
        });
    }

    @Override
    public final void messageReceived(final GameServer gameServer, final HostedConnection source, final Message m) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                processMessageFromClient(gameServer, source, m);
                return null;
            }
        });
    }
    
    private void processMessageFromClient(GameServer gameServer, HostedConnection source, final Message m) {
//        LOG.log(Level.INFO, "Receive from client, class={0}, message={1}", new Object[] {m.getClass().getName(), m.toString()});

        // 这是游戏运行时的消息
        if (m instanceof GameMess) {
            onReceiveGameMess(gameServer, source, (GameMess) m);
            return;
        }

        // 这些消息主要用于客户端和服务端状态初始化时处理, 当客户端和服务端正式进入游戏交互之后，
        // 这些消息的频率会非常少，甚至不需要.
        if (m instanceof ClientMess) {
            onReceiveMessClient(gameServer, source, (ClientMess) m);

        } else if (m instanceof GetServerStateMess) {
            onReceiveMessGetServerState(gameServer, source, (GetServerStateMess) m);
            
        } else if (m instanceof GetGameDataMess) {
            onReceiveMessGetGameData(gameServer, source, (GetGameDataMess) m);

        } else if (m instanceof GetClientsMess) {
            onReceiveMessGetClients(gameServer, source, (GetClientsMess) m);

        } else if (m instanceof PingMess) {
            onReceiveMessPing(gameServer, source, (PingMess) m);
            
        } else if (m instanceof RequestGameInitMess) {
            if (gameServer.getServerState() != ServerState.running) {
                LOG.log(Level.WARNING, "Server state is not ready to process game init request! "
                        + "clientAddress={0}, clientId={1}, message={2}", new Object[] {source.getAddress(), source.getId(), m});
                return;
            }
            onReceiveMessRequestGameInit(gameServer, source, (RequestGameInitMess) m);
        }
        
        
    }
    
    /**
     * 当前客户端获得标识时,这一步发生在 {@link #onClientAdd }之后, 主要更
     * 新客户端的标识，并刷新客户端列表。
     * @param gameServer
     * @param conn
     * @param m 
     */
    protected void onReceiveMessClient(GameServer gameServer, HostedConnection conn, ClientMess m) {
        // 登记、更新客户端资料
        ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        cd.setClientId(m.getClientId());
        cd.setClientName(m.getClientName());
        // 更新客户端列表
        onClientsUpdated(gameServer);
    }
    
    /**
     * 当接收到客户端发来的询问服务端的当前状态时。该方法主要向请求的客户端
     * 返回当前服务端的状态。
     * @param gameServer
     * @param conn
     * @param m 
     */
    protected void onReceiveMessGetServerState(GameServer gameServer, HostedConnection conn, GetServerStateMess m) {
        gameServer.send(conn, new ServerStateMess(gameServer.getServerState()));
    }
    
    /**
     * 当接收到客户端发送的请求游戏数据的消息时该方法被自动调用，这个方法主要实现向客户端返回游戏数据的消息：
     * {@link  GameData}, 默认情况下服务端仅向客户端发送游戏的基本数据。
     * 详情参考{@link #onSendGameData(GameServer, HostedConnection) }
     * @param gameServer
     * @param conn
     * @param m 
     * @see #onSendGameData(GameServer, HostedConnection) 
     */
    protected void onReceiveMessGetGameData(GameServer gameServer, HostedConnection conn, GetGameDataMess m) {
        onSendGameData(gameServer, conn);
    }
    
    /**
     * 当接收到客户端发送的请求所有的客户端列表时该方法被自动调用，这个方法主要实现向客户端返回当前连接的所有客户端
     * 列表的基本信息. {@link 
     * 信息。
     * @param gameServer
     * @param conn
     * @param mess 
     */
    protected void onReceiveMessGetClients(GameServer gameServer, HostedConnection conn, GetClientsMess mess) {
        gameServer.broadcast(new ClientsMess(gameServer.getClients()));
    }
    
    /**
     * 当服务端接收到来自客户端的Ping消息时，该方法被自动调用，默认情况下服务端接收到这个消息后将直接返回这个消息
     * 不作任何其它处理.
     * @param gameServer
     * @param conn
     * @param mess 
     */
    protected void onReceiveMessPing(GameServer gameServer, HostedConnection conn, PingMess mess) {
        gameServer.send(conn, mess);
    }
    
    /**
     * 服务端向客户端发送游戏数据，当服务端接收到客户端连接或者客户端向服务端请求游戏数据时，
     * 该方法会被自动调用，主要实现向客户端发送当前的游戏数据GameData。<br>
     * 注：默认情况下，服务端只向客户端发送游戏基本数据，不发送场景实体数据，也不发送游戏逻辑。
     * 因为客户端不应该执行来自服务端的游戏逻辑，另一个，游戏场景实体数据的不确定性，当场景实体非常多时，
     * 如果一次性发送可能导致问题，因此这些数据应该在客户端和服务端连接和初始化完成后再从服务端获取。
     * @param gameServer
     * @param coon 
     */
    protected void onSendGameData(GameServer gameServer, HostedConnection coon) {
        // 注1：这里向客户端发送的并不包含游戏逻辑数据及场景实体数据，
        // 这些数据是在客户端状态初始化后再从服务端获取并载入,当服务端和客户端状态就绪之后，服务端可以依次有序的向
        // 客户端一个一个发送所有实体数据。
        
        // 注2：gameData必须克隆后，再清除逻辑和场景实体，否则会影响服务端的游戏数据。
        GameData gameData = gameServer.getGameData();
        
        GameData clone = (GameData) gameServer.getGameData().clone(); 
        // 因为克隆的时候会导致唯一ID不同，这里特别同步一下，使用客户端和服务端使用相同的唯一ID。
        clone.setUniqueId(gameData.getUniqueId());
        
        if (clone.getSceneData() != null) {
            clone.getSceneData().setEntityDatas(null);
            clone.getSceneData().setUniqueId(gameData.getUniqueId());
        }
        
        if (clone.getGuiSceneData() != null) {
            clone.getGuiSceneData().setEntityDatas(null);
            clone.getGuiSceneData().setUniqueId(gameData.getGuiSceneData().getUniqueId());
        }
        
        // 清理掉逻辑
        clone.getGameLogicDatas().clear();
        
        gameServer.send(coon, new GameDataMess(clone));
    }
    
    /**
     * 当一个新的客户端连接到服务端时该方法被自动调用，默认情况下，
     * 该方法将立即向客户端发送游戏数据{@link GameData}作为响应.
     * 注：默认情况下，服务端只向客户端发送游戏的基本数据,不包含场景数据及游戏逻辑。
     * @param gameServer
     * @param conn 
     * @see #onSendGameData(GameServer, HostedConnection) 
     */
    protected void onClientAdded(GameServer gameServer, HostedConnection conn) {
        // 初始化一个用于存放数据的容器,选择在这里初始化以便后续使用的时候不再需要判断null
        ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        if (cd == null) {
            cd = new ConnData();
            cd.setConnId(conn.getId());
            cd.setAddress(conn.getAddress());
            conn.setAttribute(ConnData.CONN_ATTRIBUTE_KEY, cd);
        }
        // 当客户端连接时向客户端发送游戏数据
        onSendGameData(gameServer, conn);
        // 更新客户端列表
        onClientsUpdated(gameServer);
    }
    
    /**
     * 当一个客户端断开与服务端的连接时该方法被自动调用，默认情况下，该方法会向所有客户端广播更新客户端列表的信息。
     * 子类可以覆盖该方法来作进一步处理该事件，例如：将客户端控制的游戏角色移除场景，向客户端发送广播通知消息。
     * @param gameServer
     * @param conn 
     */
    protected void onClientRemoved(GameServer gameServer, HostedConnection conn) {
        // 更新客户端列表
        onClientsUpdated(gameServer);
    }
    
    /**
     * 当客户端列表发生变化时该方法被自动调用,其中包含如：新客户端添加，断开，客户端信息发生变化时该方法都会被调用。
     * 默认情况下，该方法会向所有客户端广播更新客户端列表的信息。
     * @param gameServer
     */
    protected void onClientsUpdated(GameServer gameServer) {
        gameServer.broadcast(new ClientsMess(gameServer.getClients()));
    }
    
    /**
     * 当服务端接收到来自客户端的初始化游戏的请求时，该方法被调用。子类实现这个方法需要<b>按顺序</b>做两件事：<br>
     * 1.向客户端发送{@link RequestGameInitMess}消息进行响应，并指明要向客户端初始化多少场景实体。
     * 2.接着步骤1，立即向客户端发送初始化场景的实体数据。
     * @param gameServer
     * @param conn
     * @param mess 
     * @see RequestGameInitMess
     * @see RequestGameInitOkMess
     */
    protected abstract void onReceiveMessRequestGameInit(GameServer gameServer, HostedConnection conn, 
            RequestGameInitMess mess);
    
    /**
     * 在服务端处理接收到的来自客户端的消息
     * @param gameServer
     * @param source
     * @param m 
     */
    protected abstract void onReceiveGameMess(GameServer gameServer, HostedConnection source, GameMess m);
}
