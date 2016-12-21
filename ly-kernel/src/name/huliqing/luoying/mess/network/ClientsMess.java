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
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.mess.BaseMess;

/**
 * 服务端向客户端发送当前所有已经连接到服务器的客户端列表信息.
 * @author huliqing
 */
@Serializable
public class ClientsMess extends BaseMess {
    
    // remove注意：在Serializable对象中不要使用final字段，这会造成无法序列化，除非该对象始终不变（特别是LIST,MAP）
    // 。这在JME中没有报错，但是在重新生成对象的时候该列表会空值。
//    private final List<ConnData> clients = new ArrayList<ConnData>();
    
    private List<ConnData> clients = new ArrayList<ConnData>();
    
    public ClientsMess() {}
    
    public ClientsMess(List<ConnData> clients) {
        this.clients.addAll(clients);
    }
    
    public void addClient(ConnData client) {
        clients.add(client);
    }
    
    public List<ConnData> getClients() {
        return clients;
    }
    
}
