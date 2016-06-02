/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.state.lan.GameServer.ServerListener;
import name.huliqing.fighter.game.state.lan.mess.MessActorTransformDirect;
import name.huliqing.fighter.object.IntervalLogic;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class Network extends IntervalLogic {
    private final static Logger LOG = Logger.getLogger(Network.class.getName());
    private final static Network ins = new Network();
    private final ConfigService configService = Factory.get(ConfigService.class);
    private GameServer gameServer;
    private GameClient gameClient;
    
    private Network() {
        super(0);
    }
    
    public static Network getInstance() {
        return ins;
    }
    
    @Override
    protected void doLogic(float tpf) {
        if (gameClient != null) {
            gameClient.update(tpf);
        } else if (gameServer != null) {
            gameServer.update(tpf);
        }
    }
    
    /**
     * 清理Network创建的server或client,释放资源和端口占用。
     */
    @Override
    public void cleanup() {
        if (gameServer != null) {
            gameServer.cleanup();
            gameServer = null;
        }
        if (gameClient != null) {
            gameClient.cleanup();
            gameClient = null;
        }
    }
    
    /**
     * 创建服务器端程序，注意：如果当前已经创建过客户端或服务端，则它们会被
     * 关闭和清理.游戏中只能存在一个服务端或者是客户端，否则可能引起端口冲突
     * 或资源浪费问题。
     * @param gameData
     * @return
     * @throws IOException 
     */
    public  GameServer createGameServer(GameData gameData) throws IOException {
        cleanup();
        gameServer = new GameServer(gameData);
        return gameServer;
    }
    
    /**
     * 创建客户端程序，注意：如果当前已经创建过客户端或服务端，则它们会被
     * 关闭和清理.游戏中只能存在一个服务端或者是客户端，否则可能引起端口冲突
     * 或资源浪费问题。
     * @param gameData
     * @param serverHost
     * @param serverPort
     * @return
     * @throws IOException 
     */
    public GameClient createGameClient(String serverHost, int serverPort) throws Exception {
        cleanup();
        gameClient = new GameClient(configService.getGameName()
                , configService.getVersionCode()
                , serverHost
                , serverPort);
        return gameClient;
    }
    
    /**
     * 判断当前运行的游戏是否为客户端,注：没有isServer这个方法，因为主机和
     * 单机（故事）模式都可以开启服务模式。
     * @return 
     */
    public boolean isClient() {
        return gameClient != null;
    }
    
    /**
     * 判断当前主机是否有客户端连接，必须是主机模式以及有客户端连接时该方法
     * 才返回true.
     * @return 
     */
    public boolean hasConnections() {
        return gameServer != null && gameServer.hasConnections();
    }
    
    /**
     * 发送消息给所有客户端,必须确保当前游戏为服务端，并且正在运行，否则该方
     * 法将什么也不做。
     * @param message 
     */
    public void broadcast(Message message) {
        if (gameServer != null && gameServer.isRunning()) {
            gameServer.broadcast(message);
            return;
        } 
        
        if (gameClient != null) {
            throw new IllegalStateException("GameClient could not broadcast message, message=" + message);
        }
    }
    
    /**
     * 发送消息给服务端，必须确保当前游戏为客户端，并且已经连接到服务器，否
     * 则该方法将什么也不做
     * @param message 
     */
    public void sendToServer(Message message) {
        if (gameClient != null && gameClient.isConnected()) {
            gameClient.send(message);
        }
    }
    
    /**
     * 发送信息到指定的客户端
     * @param client
     * @param message 
     */
    public void sendToClient(HostedConnection client, Message message) {
        if (gameServer != null && gameServer.isRunning()) {
            gameServer.send(client, message);
        }
    }
    
    /**
     * 发送消息到指定角色所在的客户端。
     * @param actor
     * @param message 
     */
    public void sendToClient(Actor actor, Message message) {
        if (gameServer != null && gameServer.isRunning()) {
            gameServer.send(actor, message);
        }
    }
    
    /**
     * 直接同步指定角色的变换消息到当前所有连接的客户端,该消息只处理一次。
     * @param actorId
     * @param location
     * @param viewDirection 
     * @deprecated 不要使用这样直接同步位置,很导致客户端严重抖动
     */
    public void syncTransformDirect(Actor actor) {
        if (hasConnections()) {
            MessActorTransformDirect mess = new MessActorTransformDirect();
            mess.setActorId(actor.getData().getUniqueId());
            mess.setLocation(actor.getModel().getWorldTranslation());
            mess.setWalkDirection(actor.getWalkDirection());
            mess.setViewDirection(actor.getViewDirection());
            broadcast(mess);
            if (Config.debug) {
                LOG.log(Level.INFO, "broadcastSync message={0}, class={1}"
                        , new Object[] {mess.toString(), mess.getClass()});
            }
        }
    }

    /**
     * 添加需要动态自动同步变换状态的角色，添加后该角色的变换将自动持续的同
     * 步变换状态到所有客户端，直到调用 {@link #removeAutoSyncTransform(name.huliqing.fighter.object.actor.Actor) }
     * 为止。
     * @param actor 
     */
    public void addAutoSyncTransform(Actor actor) {
        if (hasConnections()) {
            ServerListener sl = gameServer.getListener();
            if (sl != null) {
                sl.addSyncObject(actor);
            }
        }
    }
    
    public void removeAutoSyncTransform(Actor actor) {
        if (gameServer != null) {
            ServerListener sl = gameServer.getListener();
            if (sl != null) {
                sl.removeSyncObject(actor);
            }
        }
    }
}
