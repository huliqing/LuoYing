/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;
import name.huliqing.luoying.network.GameServer.ServerState;

/**
 * 服务端向客户端发送当前服务端的状态
 * @author huliqing
 */
@Serializable
public class ServerStateMess extends BaseMess {
    
    private ServerState serverState;

    public ServerStateMess() {}
    
    public ServerStateMess(ServerState serverState) {
        this.serverState = serverState;
    }
    
    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public String toString() {
        return "MessSCServerState{" + "serverState=" + serverState + '}';
    }
    
    
}
