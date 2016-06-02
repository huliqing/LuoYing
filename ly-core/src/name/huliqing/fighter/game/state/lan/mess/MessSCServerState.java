/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.game.state.lan.GameServer.ServerState;

/**
 * 服务端向客户端发送当前服务端的状态
 * @author huliqing
 */
@Serializable
public class MessSCServerState extends MessBase {
    
    private ServerState serverState;

    public MessSCServerState() {}
    
    public MessSCServerState(ServerState serverState) {
        this.serverState = serverState;
    }
    
    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }
}
