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
package name.huliqing.luoying.mess.network;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 客户端向服务端请求初始化游戏, 这个消息只有在客户端处于ready状态，同时服务端处于running状态时才可以向服务端发送，
 * 以请求服务端场景实体的初始化数据。<br>
 * 当服务端接收到这个消息之后应该向客户端发送MessRequestGameInitOk消息，并立即向客户端发送场景初始化实体数据.
 * @author huliqing
 */
@Serializable
public class RequestGameInitMess extends BaseMess {

    @Override
    public String toString() {
        return "RequestGameInitMess{" + '}';
    }
    
}
