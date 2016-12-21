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
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.manager.RandomManager;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameServer;

/**
 * @author huliqing
 */
@Serializable
public class GameMess extends BaseMess {
    
    // 随机索引
    private byte randomIndex = -1;
    
    public GameMess() {}
    
    public GameMess(boolean reliable) {
        super(reliable);
    }
    
    /**
     * 设置随机数索引，以便与客户端进行同步。
     * @param randomIndex 
     */
    public final void setRandomIndex(byte randomIndex) {
        this.randomIndex = randomIndex;
    }
    
    /**
     * 当客户端接收到这个消息时，这个方法会在客户端被调用，以响应来自服务端的消息。
     * @param gameClient
     */
    public void applyOnClient(GameClient gameClient) {
        // 在客户端同步随机数的索引。
        if (randomIndex != -1) {
            RandomManager.setIndex(randomIndex);
        }
    }
    
    /**
     * 当服务端接收到这个消息时，这个方法会在服务端被调用,子类可以继承这个方法以处理来自客户端的消息。
     * @param gameServer
     * @param source 
     */
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        // 由子类覆盖
    }
}
