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
package name.huliqing.luoying.data;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ConnData extends AbstractMessage {
    
    /**
     * HostedConnection的attribute的key
     */
    public transient final static String CONN_ATTRIBUTE_KEY = "_CONN_ATTRIBUTE_";
    
    // 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
    private String clientId;
    
    // 客户端名称标识,如PC名称，手机名称
    private String clientName;
    
    // 客户端的连接ID，该ID对于每个客户端连接都是唯一的
    private int connId; 
    
    // 连接地址，即客户端的IP信息
    private String address;
    
    // 客户端所控制的角色的唯一ID
    private long entityId;

     // 角色名称
    private String entityName;
    
    /**
     * 获取客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
     * @return 
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 客户端唯一标识，这个标识对于所有客户端或主机来说是唯一的。
     * @param clientId 
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 客户端名称标识,如PC名称，手机名称
     * @return 
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 客户端名称标识,如PC名称，手机名称
     * @param clientName 
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 客户端所控制的角色的唯一ID,如果为
     * @return 
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * 设置客户端所控制的角色的唯一ID
     * @param actorId 
     */
    public void setEntityId(long actorId) {
        this.entityId = actorId;
    }

    public int getConnId() {
        return connId;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String actorName) {
        this.entityName = actorName;
    }
    
    
}
