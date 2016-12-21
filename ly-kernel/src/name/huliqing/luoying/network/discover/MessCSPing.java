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

/**
 * 这个消息用于客户端向服务端发送ping消息，用于查询客户端与服务端的ping值。
 * 服务端在获取这个消息后直接返回这个消息。
 * 在消息发送和接收过程中会维持一个pingId,这个pingId用于识别每一个不同的ping
 * 消息。
 * @author huliqing
 */
public class MessCSPing extends AbstractMess {

    // 为每一个Ping消息生成pingId
    private transient static short globalId = Short.MIN_VALUE;
    
    // 为当前消息定义一个pingId，这个pingId会在short范围内循环。达到最大值后重新
    // 回到最小值。
    private short pingId;
    
    public MessCSPing() {
        pingId = globalId++;
        
        if (globalId >= Short.MAX_VALUE) {
            globalId = Short.MIN_VALUE;
        }
    }
    
    public short getPingId() {
        return pingId;
    }
    
}
