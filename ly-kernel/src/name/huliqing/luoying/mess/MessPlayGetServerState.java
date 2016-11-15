/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.network.GameServer;

/**
 * 向服务端获取当前的游戏状态
 * @author huliqing
 */
@Serializable
public class MessPlayGetServerState extends MessBase {

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        gameServer.send(source, new MessSCServerState(gameServer.getServerState()));
//        source.send(new MessSCServerState(gameServer.getServerState())); // remove0906以后统一使用gameServer发送消息
    }
    
}
