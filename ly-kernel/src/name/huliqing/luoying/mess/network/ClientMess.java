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
 * 客户端首次连接到服务端时发送的第一个信息，主要用于向服务端交待当前连接的客户端的身份信息。
 * @author huliqing
 */
@Serializable
public class ClientMess extends BaseMess {
    
    // 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
    private String clientId;
    
    // 客户端名称标识,如PC名称，手机名称
    private String clientName;
    
    public ClientMess() {}
    
    public ClientMess(String clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    /**
     * 获取客户端的ID标识。
     * @return 
     */
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
