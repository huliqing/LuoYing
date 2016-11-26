/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import com.jme3.app.Application;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.mess.MessBase;
import name.huliqing.luoying.network.AbstractClientListener;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.layer.service.GameService;

/**
 * 默认的局域网客户端监听来自服务端的消息的侦听器
 * @author huliqing 
 */
public abstract class LanClientListener extends AbstractClientListener {
    private static final Logger LOG = Logger.getLogger(LanClientListener.class.getName());
    private final GameService gameService = Factory.get(GameService.class);
    
    private final List<MessBase> messageQueue = new LinkedList<MessBase>();
    private double offset = Double.MIN_VALUE;
    private final double maxDelay = 0.50;
    
    public LanClientListener(Application app) {
        super(app);
    }
    
    @Override
    protected void onClientDisconnected(GameClient gameClient, ClientStateListener.DisconnectInfo info) {
        // 断开、踢出、服务器关闭等提示
        String message = ResManager.get("lan.disconnected");
        if (info != null) {
            message += "(" + info.reason + ")";
        }
        gameService.addMessage(message, MessageType.notice);
    }

    // remove
//    @Override
//    protected void onReady(GameClient gameClient) {
//        gameClient.send(new MessRequestInitGame());
//    }
    
    @Override
    protected final void onClientGameRunning(GameClient gameClient, Message m) {
        
        // remove20161126
//        // 客户端只有在接到InitGameOK消息后才允许处理游戏命令消息.
//        // 只有在客户端接收到InitGameOK后客户端才开始进入running状态
//        if (m instanceof MessSCInitGameOK) {
//            gameClient.setClientState(ClientState.running);
//            if (Config.debug) {
//                LOG.log(Level.INFO, "客户端初始化OK,可开始接收消息, ClientState={0}"
//                        , gameClient.getClientState());
//            }
//            return;
//        }
//        
//        // 客户端在状态变更为running这前不会处理游戏命令
//        if (gameClient.getClientState() != ClientState.running) {
//            if (Config.debug) {
//                LOG.log(Level.INFO, "客户端未准备好,ClientState={0}", gameClient.getClientState());
//            }
//            return;
//        }
        
        // 统一把接收到的消息放到队列中
        if (m instanceof MessBase) {
            MessBase message = (MessBase) m;
            if (offset == Double.MIN_VALUE) {
                offset = gameClient.time - message.getTime();
            }
            double delayTime = (message.getTime() + offset) - gameClient.time;
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
            if (message.getTime() >= gameClient.time + offset) {
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
