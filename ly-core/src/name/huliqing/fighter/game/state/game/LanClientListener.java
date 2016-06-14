/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import com.jme3.app.Application;
import name.huliqing.fighter.game.state.lan.GameClient;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.DefaultClientListener;
import name.huliqing.fighter.game.state.lan.mess.MessPlayInitGame;
import name.huliqing.fighter.game.state.lan.GameClient.ClientState;
import name.huliqing.fighter.game.state.lan.GameServer.ServerState;
import name.huliqing.fighter.game.state.lan.mess.MessPlayGetServerState;
import name.huliqing.fighter.game.state.lan.mess.MessSCInitGameOK;
import name.huliqing.fighter.game.state.lan.mess.MessBase;
import name.huliqing.fighter.game.state.lan.mess.MessPlayGetClients;
import name.huliqing.fighter.manager.ResourceManager;

/**
 *
 * @author huliqing 
 */
public class LanClientListener extends DefaultClientListener {
    private static final Logger LOG = Logger.getLogger(LanClientListener.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    
    private List<MessBase> messageQueue = new LinkedList<MessBase>();
    private double offset = Double.MIN_VALUE;
    private double maxDelay = 0.50;

    // 客户端所控制的角色的唯一ID
    public static long playerActorUniqueId = -1;
    
    public LanClientListener(Application app) {
        super(app);
    }
    
    // remove20160613
//    /**
//     * 开始向服务端发起初始化游戏数据的请求。注：该方法会检查客户端和服务端
//     * 的状态，只有在确认客户端处于ClientState.ready和服务端处于ServerState.running
//     * 时才发起请求，在发起请求后，客户端将转入waiting_init_game状态，以避免重复发送请
//     * 求。
//     * @param gameClient 
//     */
//    public void checkToStartClientInit(GameClient gameClient) {
//        if (gameClient.getClientState() == ClientState.ready && gameClient.getServerState() == ServerState.running) {
//            // 获取初始场景信息,注：确保这个命令只发送一次。
//            gameClient.setClientState(ClientState.waitting_init_game);
//            gameClient.send(new MessPlayInitGame());
//            
//            // 从服务端重新获取所有客户端联接信息，因为gameClient重新设置了listener,
//            // 并且clientsWin需要初始化这部分信息，否则客户端进入后打开看不到列表，除非有新客户端连接。
//            // 所以这里应该主动获取一次，进行初始化
//            gameClient.send(new MessPlayGetClients());
//            
//            // 显示角色选择面板
//            playService.showSelectPanel(gameClient.getGameData().getAvailableActors());
//        } else {
//            // 请求服务端状态
//            gameClient.send(new MessPlayGetServerState());
//        }
//    }

    @Override
    protected void processClientDisconnected(GameClient gameClient, Client client, ClientStateListener.DisconnectInfo info) {
        // 断开、踢出、服务器关闭等提示
        String message = ResourceManager.get("lan.disconnected");
        if (info != null) {
            message += "(" + info.reason + ")";
        }
        // 注：如果不在游戏中，可能获取不到playState,这时是不能调用playService的
        if (Common.getPlayState() != null) {
            playService.addMessage(message, MessageType.notice);
        }
    }

    @Override
    protected void onServerStateChange(GameClient gameClient, ServerState newState) {
        super.onServerStateChange(gameClient, newState);
    }
    
    @Override
    protected void processClientMessage(GameClient gameClient, Client source, Message m) {
        // 客户端只有在接到InitGameOK消息后才允许处理游戏命令消息.
        // 只有在客户端接收到InitGameOK后客户端才开始进入running状态
        if (m instanceof MessSCInitGameOK) {
            gameClient.setClientState(ClientState.running);
            if (Config.debug) {
                LOG.log(Level.INFO, "客户端初始化OK,可开始接收消息, ClientState={0}"
                        , gameClient.getClientState());
            }
            return;
        }
        
        // 客户端在状态变更为running这前不会处理游戏命令
        if (gameClient.getClientState() != ClientState.running) {
            if (Config.debug) {
                LOG.log(Level.INFO, "客户端未准备好,ClientState={0}", gameClient.getClientState());
            }
            return;
        }
        
        if (m instanceof MessBase) {
            MessBase message = (MessBase) m;
            if (offset == Double.MIN_VALUE) {
                offset = gameClient.time - message.time;
//                if (Config.debug) {
//                    Logger.getLogger(LanClientListener.class.getName()).log(Level.INFO, "Initial offset {0}", offset);
//                }
            }
            double delayTime = (message.time + offset) - gameClient.time;
            if (delayTime > maxDelay) {
                offset -= delayTime - maxDelay;
//                if (Config.debug) {
//                    Logger.getLogger(LanClientListener.class.getName()).log(Level.INFO, "Decrease offset due to high delaytime ({0})", delayTime);
//                }
            } else if (delayTime < 0) {
                offset -= delayTime;
//                if (Config.debug) {
//                    Logger.getLogger(LanClientListener.class.getName()).log(Level.INFO, "Increase offset due to low delaytime ({0})", delayTime);
//                }
            }
            messageQueue.add(message);
//            if (Config.debug) {
//                LOG.log(Level.INFO, "客户端添加处理命令到列表-->message={0}, QueueSize={1}"
//                        , new Object[] {message.getClass().getSimpleName(), messageQueue.size()});
//            }
        }
    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        super.update(tpf, gameClient);
        for (Iterator<MessBase> it = messageQueue.iterator(); it.hasNext();) {
            MessBase message = it.next();
            if (message.time >= gameClient.time + offset) {
                doMessage(tpf, gameClient, message);
                it.remove();
            }
        }
        
//        if (Config.debug && messageQueue.size() > 0) {
//            LOG.log(Level.INFO, "客户端QueueSize={0}, gameClient.time={1}, offset={2}, message.time={3}"
//                    , new Object[] {messageQueue.size(), gameClient.time, offset, messageQueue.get(messageQueue.size() - 1).time});
//        }
    }

    protected void doMessage(float tpf, GameClient gameClient, MessBase m) {
        ((MessBase)m).applyOnClient();
    }
   
}
