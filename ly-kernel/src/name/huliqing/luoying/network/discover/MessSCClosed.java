/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network.discover;

import name.huliqing.luoying.network.GameServer.ServerState;


/**
 * 服务端向客户端通知:服务器正在关闭
 * @author huliqing
 */
public class MessSCClosed extends MessServerState {
    
    public MessSCClosed() {
    }
    
    public MessSCClosed(String host, int port, String versionName, int versionCode, String hostName, String des, ServerState state) {
        super(host, port, versionName, versionCode, hostName, des, state);
    }
}
