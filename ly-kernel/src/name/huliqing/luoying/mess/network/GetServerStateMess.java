/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 向服务端获取当前的游戏状态
 * @author huliqing
 */
@Serializable
public class GetServerStateMess extends BaseMess {

    // remove20161128
//    @Override
//    public void applyOnServer(GameServer gameServer, HostedConnection source) {
//        super.applyOnServer(gameServer, source);
//        
//        // remove0906以后统一使用gameServer发送消息
////        source.send(new MessSCServerState(gameServer.getServerState())); 
//
//        gameServer.send(source, new MessServerState(gameServer.getServerState()));
//        
//    }
    
}
