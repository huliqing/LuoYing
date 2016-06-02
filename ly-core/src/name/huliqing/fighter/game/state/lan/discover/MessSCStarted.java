/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.discover;

import name.huliqing.fighter.game.state.lan.GameServer;


/**
 * 服务端向客户端通知服务器正在运行的消息
 * @author huliqing
 */
public class MessSCStarted extends MessServerState {

    public MessSCStarted() {
    }
    
    public MessSCStarted(String host, int port, String version, String hostName, String des, GameServer.ServerState state) {
        super(host, port, version, hostName, des, state);
    }
    
    // remove20160501
//    @Override
//    public String encodeContent() {
//        if (host == null) {
//            throw new IllegalArgumentException("Need to set a host address, like: 192.168.1.8");
//        }
//        if (port <= 0) {
//            throw new IllegalArgumentException("Need to set a port!");
//        }
//        return host 
//                + "|" + port 
//                + "|" + version 
//                + "|" + hostName
//                + "|" + (des != null ? des : "")
//                + "|" + state
//                
//                // 最后加“|”为了防止一个奇怪的现象:
//                // java.lang.NumberFormatException: For input string: "0
//                
//                + "|"; 
//    }
//
//    @Override
//    public void decodeContent(String content) {
//        String[] server = content.split("\\|");
//        host = server[0];
//        port = Integer.parseInt(server[1]);
//        version = server[2];
//        if (server.length > 3) {
//            hostName = server[3];
//        }
//        if (server.length > 4) {
//            des = server[4];
//        }
//        if (server.length > 5) {
//            state = ServerState.valueOf(server[5]);
//        }
//    }
    
}
