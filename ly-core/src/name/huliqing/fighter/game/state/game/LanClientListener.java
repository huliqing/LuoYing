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
import name.huliqing.fighter.game.state.lan.GameClient.ClientState;
import name.huliqing.fighter.game.state.lan.mess.MessSCInitGameOK;
import name.huliqing.fighter.game.state.lan.mess.MessBase;
import name.huliqing.fighter.manager.ResourceManager;

/**
 * 默认的局域网客户端监听来自服务端的消息的侦听器
 * @author huliqing 
 */
public class LanClientListener extends DefaultClientListener {
    private static final Logger LOG = Logger.getLogger(LanClientListener.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    
    private final List<MessBase> messageQueue = new LinkedList<MessBase>();
    private double offset = Double.MIN_VALUE;
    private final double maxDelay = 0.50;

    // remove20160616
//    // 客户端所控制的角色的唯一ID
//    public static long playerActorUniqueId = -1;
    
    public LanClientListener(Application app) {
        super(app);
    }
    
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
    protected final void processClientMessage(GameClient gameClient, Client source, Message m) {
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
        
        // 统一把接收到的消息放到队列中
        if (m instanceof MessBase) {
            MessBase message = (MessBase) m;
            if (offset == Double.MIN_VALUE) {
                offset = gameClient.time - message.time;
            }
            double delayTime = (message.time + offset) - gameClient.time;
            if (delayTime > maxDelay) {
                offset -= delayTime - maxDelay;
            } else if (delayTime < 0) {
                offset -= delayTime;
            }
            messageQueue.add(message);
        }
    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        super.update(tpf, gameClient);
        for (Iterator<MessBase> it = messageQueue.iterator(); it.hasNext();) {
            MessBase message = it.next();
            if (message.time >= gameClient.time + offset) {
                applyMessage(gameClient, message);
                it.remove();
            }
        }
        
//        if (Config.debug && messageQueue.size() > 0) {
//            LOG.log(Level.INFO, "客户端QueueSize={0}, gameClient.time={1}, offset={2}, message.time={3}"
//                    , new Object[] {messageQueue.size(), gameClient.time, offset, messageQueue.get(messageQueue.size() - 1).time});
//        }
    }

    /**
     * 立即执行来自服务端的消息。
     * @param gameClient
     * @param m 从服务端发来的消息
     */
    protected void applyMessage(GameClient gameClient, MessBase m) {
        ((MessBase)m).applyOnClient();
    }
   
}