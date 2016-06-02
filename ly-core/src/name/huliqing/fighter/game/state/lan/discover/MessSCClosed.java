/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.discover;

import name.huliqing.fighter.game.state.lan.GameServer.ServerState;


/**
 * 服务端向客户端通知:服务器正在关闭
 * @author huliqing
 */
public class MessSCClosed extends MessServerState {
    
    public MessSCClosed() {
    }
    
    public MessSCClosed(String host, int port, String version, String hostName, String des, ServerState state) {
        super(host, port, version, hostName, des, state);
    }
}
