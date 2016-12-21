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
package name.huliqing.luoying.network;

import name.huliqing.luoying.data.ConnData;
import java.util.List;

/**
 * 局域网游戏接口
 * @author huliqing
 */
public interface  LanGame  {
    
    /**
     * 获取当前所有连接的客户端信息
     * @return 
     */
    List<ConnData> getClients();
    
    /**
     * 当客户端列表更新时该方法将被调用，客户端列表更新包含以下几个可能的情况：
     * 1.有新客户端连接<br >
     * 2.有客户端断开连接等。<br >
     * 3.客户端角色有资料更新，如角色名称或其它。<br >
     */
    void onClientListUpdated();
    
    /**
     * 踢出一个客户端
     * @param connId 
     */
    void kickClient(int connId);
    
    /**
     * 判断是否为服务端,如果是则返回true,否则返回false
     * @return 
     */
    boolean isServer();
}
