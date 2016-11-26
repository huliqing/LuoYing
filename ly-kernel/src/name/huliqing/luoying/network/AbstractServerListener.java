/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import name.huliqing.luoying.mess.MessSCGameData;
import name.huliqing.luoying.mess.MessSCClientList;
import name.huliqing.luoying.mess.MessClient;
import name.huliqing.luoying.mess.MessPlayGetServerState;
import name.huliqing.luoying.mess.MessSCServerState;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.network.GameServer.ServerListener;
import name.huliqing.luoying.mess.MessPlayGetClients;
import name.huliqing.luoying.mess.MessPlayGetGameData;
import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.GameData;

/**
 * 服务端监听器,用于监听来自客户端连接的消息。
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractServerListener<T> implements ServerListener<T> {

    private static final Logger LOG = Logger.getLogger(AbstractServerListener.class.getName());

    private final Application app;
    
    public AbstractServerListener(Application app) {
        this.app = app;
    }
    
    @Override
    public void clientAdded(final GameServer gameServer, final HostedConnection conn) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                onClientAdded(gameServer, conn);
                return null;
            }
        });
    }

    @Override
    public void clientRemoved(final GameServer gameServer, final HostedConnection conn) {
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                onClientRemoved(gameServer, conn);
                onClientsUpdated(gameServer);
                return null;
            }
        });
    }

    @Override
    public void serverMessage(final GameServer gameServer, final HostedConnection source, final Message m) {
        LOG.log(Level.INFO, "Receive from client, class={0}, message={1}", new Object[] {m.getClass().getName(), m.toString()});
        
        app.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                if (m instanceof MessClient) {
                   onReceiveClientId(gameServer, source, (MessClient) m);
                   onClientsUpdated(gameServer);
                } else if (m instanceof MessPlayGetServerState) {
                    onReceiveGetServerState(gameServer, source, (MessPlayGetServerState) m);
                } else if (m instanceof MessPlayGetGameData) {
                    onReceiveGetGameData(gameServer, source, (MessPlayGetGameData) m);
                } else if (m instanceof MessPlayGetClients) {
                    onClientsUpdated(gameServer);
                } else {
                    // other message
                    processServerMessage(gameServer, source, m);
                }
                return null;
            }
        });
    }
    
    /**
     * 当有一个客户端连接进来时该方法被立即调用，这个方法将立即向连接进来的客户端发送当前服务端的游戏基本信息：
     * GameData, 其中<b>不包含</b>游戏逻辑信息及场景实体数据等信息，需要注意：<br>
     * 1.客户端不会执行来自服务端的游戏逻辑。
     * 2.游戏场景中的物体信息将由服务端和客户端都同时处于准备就绪的情况下，由客户端向服务端请求获得。
     * @param gameServer
     * @param conn 
     */
    protected void onClientAdded(GameServer gameServer, HostedConnection conn) {
        // 告诉客户端当前玩的游戏信息,gameData必须立即发送
        // 注：这里向客户端发送的并不包含游戏逻辑数据及场景实体数据，这些数据是在客户端状态初始化后再从服务端获取并载入
        // 当服务端和客户端状态就绪之后，
        GameData gameData = gameServer.getGameData();
        gameData.getGameLogicDatas().clear();
        gameData.getSceneData().setEntityDatas(null);
        gameData.getGuiSceneData().setEntityDatas(null);
        gameServer.send(conn, new MessSCGameData(gameData));
    }
    
    /**
     * 当前客户端获得标识时,这一步发生在 {@link #onClientAdd }之后, 主要更
     * 新客户端的标识，并刷新客户端列表。
     * @param gameServer
     * @param conn
     * @param m 
     */
    protected void onReceiveClientId(GameServer gameServer, HostedConnection conn, MessClient m) {
        ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        cd.setClientId(m.getClientId());
        cd.setClientName(m.getClientName());
    }
    
    /**
     * 当客户端列表更新时,其中更新包含如：新客户端添加，断开，客户端标识变化
     * 等
     * @param gameServer 
     */
    protected void onClientsUpdated(GameServer gameServer) {
        // 2.刷新客户端
        gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
    }
    
    /**
     * 当接收到客户端发来的询问服务端的当前状态时。该方法主要向请求的客户端
     * 返回当前服务端的状态。
     * @param gameServer
     * @param conn
     * @param m 
     */
    protected void onReceiveGetServerState(GameServer gameServer, HostedConnection conn, MessPlayGetServerState m) {
        MessSCServerState mess = new MessSCServerState();
        mess.setServerState(gameServer.getServerState());
        gameServer.send(conn, mess);
    }
    
    /**
     * 当服务端接收到客户端发出的请求游戏数据的命令时。
     * @param gameServer
     * @param conn
     * @param m 
     */
    protected void onReceiveGetGameData(GameServer gameServer, HostedConnection conn, MessPlayGetGameData m) {
        gameServer.send(conn, new MessSCGameData(gameServer.getGameData()));
    }
    
    /**
     * 当有一个客户端从服务端断开时。
     * @param gameServer
     * @param conn 
     */
    protected abstract void onClientRemoved(GameServer gameServer, HostedConnection conn);
    
    /**
     * 在服务端处理接收到的来自客户端的消息
     * @param gameServer
     * @param source
     * @param m 
     */
    protected abstract void processServerMessage(GameServer gameServer, HostedConnection source, Message m);
}
