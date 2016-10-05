/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.network.discover;

import name.huliqing.ly.network.GameServer.ServerState;


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
