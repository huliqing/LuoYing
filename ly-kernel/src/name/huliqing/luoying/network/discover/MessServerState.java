/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.network.discover;

import name.huliqing.luoying.network.GameServer;

/**
 * 服务端状态消息
 * @author huliqing
 */
public abstract class MessServerState extends AbstractMess {
    
    // 服务端主机ip,如：192.168.1.x
    private String host;
    // 服务端端口号
    private int port;
    // 游戏版本,如：1.5.1
    private String version;
    // 服务器名称，如PC名称，手机名称，或其它，不一定需要唯一
    private String hostName;
    // 描述信息
    private String des;
    // 服务器状态
    private GameServer.ServerState state;
    
    public MessServerState() {}
    
    /**
     * @param host 服务端主机ip,如：192.168.1.x
     * @param port 服务端端口号
     * @param version 游戏版本,如：1.5.1
     * @param hostName 服务器名称,如PC名称，手机名称，或其它，不一定需要唯一
     * @param des 描述信息可为null
     * @param state 服务器状态:0:等待中；1：运行游戏中
     */
    public MessServerState(String host, int port, String version, String hostName, String des, GameServer.ServerState state) {
        this.host = host;
        this.port = port;
        this.version = version;
        this.hostName = hostName;
        this.des = des;
        this.state = state;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * 获取服务器状态: 服务器状态:0:等待中；1：运行游戏中
     * @return 
     */
    public GameServer.ServerState getState() {
        return state;
    }

    /**
     * 设置服务器端游戏状态=> 0:等待中；1：运行游戏中
     * @param state 
     */
    public void setState(GameServer.ServerState state) {
        this.state = state;
    }
}
