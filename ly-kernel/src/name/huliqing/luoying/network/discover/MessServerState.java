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
package name.huliqing.luoying.network.discover;

import name.huliqing.luoying.network.GameServer.ServerState;

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
    private String versionName;
    // 游戏版本号，如151
    private int versionCode;
    // 服务器名称，如PC名称，手机名称，或其它，不一定需要唯一
    private String hostName;
    // 描述信息
    private String des;
    // 服务器状态
    private ServerState state;
    
    public MessServerState() {}
    
    /**
     * @param host 服务端主机ip,如：192.168.1.x
     * @param port 服务端端口号
     * @param versionName 游戏版本,如：1.5.1
     * @param versionCode 游戏版本号
     * @param hostName 服务器名称,如PC名称，手机名称，或其它，不一定需要唯一
     * @param des 描述信息可为null
     * @param state 服务器状态:0:等待中；1：运行游戏中
     */
    public MessServerState(String host, int port, String versionName, int versionCode, String hostName, String des, ServerState state) {
        this.host = host;
        this.port = port;
        this.versionName = versionName;
        this.versionCode = versionCode;
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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
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
    public ServerState getState() {
        return state;
    }

    /**
     * 设置服务器端游戏状态=> 0:等待中；1：运行游戏中
     * @param state 
     */
    public void setState(ServerState state) {
        this.state = state;
    }
}
