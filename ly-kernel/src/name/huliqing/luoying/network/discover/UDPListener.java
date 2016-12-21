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

import java.net.DatagramPacket;

/**
 * UDP消息接收侦听器
 * @author huliqing
 */
public interface UDPListener {
  
    // remove,20160501,不再使用这种方式
//    /**
//     * 处理接收到的消息
//     * @param code 接收到的编码
//     * @param content 接收到的内容
//     * @param discover 本地UDP监听器
//     * @param packet 接收到的消息包
//     */
//    void receive(String code, String content, UDPDiscover discover, DatagramPacket packet) throws Exception;
    
    /**
     * 接收到的Object信息
     * @param object
     * @param discover
     * @param packet 
     * @throws java.lang.Exception 
     */
    void receive(Object object, UDPDiscover discover, DatagramPacket packet) throws Exception;
}
