/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network.discover;

import name.huliqing.luoying.network.GameServer.ServerState;


/**
 * 服务端向客户端通知服务器正在运行的消息
 * @author huliqing
 */
public class MessSCStarted extends MessServerState {

    public MessSCStarted() {}
    
    public MessSCStarted(String host, int port, String versionName, int versionCode
            , String hostName, String des, ServerState state) {
        super(host, port, versionName, versionCode, hostName, des, state);
    }
    
}
