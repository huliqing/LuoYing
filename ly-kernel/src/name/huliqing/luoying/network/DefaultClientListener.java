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
    
    // bak201612xx
//    private final List<GameMess> messageQueue = new LinkedList<GameMess>();
//    private double offset = Double.MIN_VALUE;
//    private final double maxDelay = 0.50;
    
    @Override
    protected final void onReceiveGameMess(GameClient gameClient, GameMess m) {
        
        // bak201612xx
        // 统一把接收到的消息放到队列中
//        GameMess message = (GameMess) m;
//        if (offset == Double.MIN_VALUE) {
//            offset = gameClient.time - message.getTime();
//        }
//        double delayTime = (message.getTime() + offset) - gameClient.time;
//        if (delayTime > maxDelay) {
//            offset -= delayTime - maxDelay;
//        } else if (delayTime < 0) {
//            offset -= delayTime;
//        }
//        messageQueue.add(message);

        processGameMess(gameClient, m);
    }
    
    @Override
    public void update(float tpf, GameClient gameClient) {
        super.update(tpf, gameClient);
        
        // bak201612xx
//        for (Iterator<GameMess> it = messageQueue.iterator(); it.hasNext();) {
//            GameMess message = it.next();
//            if (message.getTime() >= gameClient.time + offset) {
//                processGameMess(gameClient, message);
//                it.remove();
//            }
//        }
        
    }

    /**
     * 当客户端接收到来自服务端的GameMess消息时该方法被自动调用，默认情况下，
     * 该方法将直接调用GameeMess的applyOnClient方法。
     * @param gameClient 客户端
     * @param gameMess 从服务端发来的消息
     */
    protected void processGameMess(GameClient gameClient, GameMess gameMess) {
        gameMess.applyOnClient(gameClient);
    }
   
}
