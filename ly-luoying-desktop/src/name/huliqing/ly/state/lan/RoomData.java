/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.state.lan;

import name.huliqing.luoying.network.GameServer.ServerState;


/**
 * @author huliqing
 */
public class RoomData {
    
    // 主机IP(e.g.192.168.1.8),端口及描述
    private String host;
    // 主机端口
    private int port;
    // 游戏版本(app版本）
    private String version;
    // 主机名称
    private String hostName;
    // 描述说明
    private String des;
    // 房间状态: 0等待客户端连接； 1正在运行游戏
    private ServerState serverState;
    // 客户端到服务端的Ping值消息，单位毫秒,因为Ping值不会那么大，所以直接用int
    private int ping;
    
//    // 游戏房间中所选择的游戏相关信息
//    private GameData gameData;
    
    public RoomData(String host, int port, String version, String hostName, String des, ServerState serverState) {
        this.host = host;
        this.port = port;
        this.version = version;
        this.hostName = hostName;
        this.des = des;
        this.serverState = serverState;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    /**
     * 获取Ping值，单位毫秒,因为Ping值不会那么大，所以直接用int
     * @return 
     */
    public int getPing() {
        return ping;
    }

    /**
     * 设置Ping值,单位毫秒,因为Ping值不会那么大，所以直接用int
     * @param ping 
     */
    public void setPing(int ping) {
        this.ping = ping;
    }
    
//    public GameData getGameData() {
//        return gameData;
//    }
//
//    public void setGameData(GameData gameData) {
//        this.gameData = gameData;
//    }

    /**
     * 比较两个房间是否一样
     * @param other
     * @return 
     */
    public boolean compare(RoomData other) {
        if (other == null) return false;
        return host.equals(other.host) && port == other.port;
    }

    /**
     * 将目标房间信息更新到本地中
     * @param other 
     */
    public void updateFrom(RoomData other) {
        if (other == null) return;
        this.host = other.host;
        this.port = other.port;
        this.version = other.version;
        this.hostName = other.hostName;
        this.des = other.des;
        this.serverState = other.serverState;
        this.ping = other.ping;
    }
}
