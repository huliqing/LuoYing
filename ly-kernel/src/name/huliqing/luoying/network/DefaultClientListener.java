/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.jme3.app.Application;
import com.jme3.network.Message;
import name.huliqing.luoying.mess.GameMess;

/**
 * 默认的局域网客户端监听来自服务端的消息的侦听器
 * @author huliqing 
 */
public abstract class DefaultClientListener extends AbstractClientListener {
//    private static final Logger LOG = Logger.getLogger(DefaultClientListener.class.getName());
    
    private final List<GameMess> messageQueue = new LinkedList<GameMess>();
    private double offset = Double.MIN_VALUE;
    private final double maxDelay = 0.50;
    
    public DefaultClientListener(Application app) {
        super(app);
    }
    
//    @Override
//    protected void onClientDisconnected(GameClient gameClient, ClientStateListener.DisconnectInfo info) {
//        // 断开、踢出、服务器关闭等提示
//        String message = ResManager.get("lan.disconnected");
//        if (info != null) {
//            message += "(" + info.reason + ")";
//        }
//        gameService.addMessage(message, MessageType.notice);
//    }
    
    @Override
    protected final void onReceiveGameMess(GameClient gameClient, Message m) {
        
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
        if (m instanceof GameMess) {
            GameMess message = (GameMess) m;
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
        for (Iterator<GameMess> it = messageQueue.iterator(); it.hasNext();) {
            GameMess message = it.next();
            if (message.getTime() >= gameClient.time + offset) {
                onReceiveGameMess(gameClient, message);
                it.remove();
            }
        }
        
//        if (Config.debug && messageQueue.size() > 0) {
//            LOG.log(Level.INFO, "客户端QueueSize={0}, gameClient.time={1}, offset={2}, message.time={3}"
//                    , new Object[] {messageQueue.size(), gameClient.time, offset, messageQueue.get(messageQueue.size() - 1).time});
//        }
    }

    /**
     * 当客户端接收到来自服务端的GameMess消息时该方法被自动调用，默认情况下，
     * 该方法将直接调用GameeMess的applyOnClient方法。
     * @param gameClient 客户端
     * @param gameMess 从服务端发来的消息
     */
    protected void onReceiveGameMess(GameClient gameClient, GameMess gameMess) {
        gameMess.applyOnClient(gameClient);
    }
   
}
