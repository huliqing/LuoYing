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
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameClient;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 让Entity使用一件指定id（唯一id）的物品)
 * @author huliqing
 */
@Serializable
public class EntityUseDataByIdMess extends GameMess {
    
    private long entityId;
    private long objectUniqueId;

    public long getEntityId() {
        return entityId;
    }

    /**
     * 设置EntityId
     * @param entityId 
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getObjectUniqueId() {
        return objectUniqueId;
    }

    /**
     * 设置要使用的物品的id
     * @param objectUniqueId 
     */
    public void setObjectUniqueId(long objectUniqueId) {
        this.objectUniqueId = objectUniqueId;
    }
   
    @Override
    public void applyOnClient(GameClient gameClient) {
        super.applyOnClient(gameClient);
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityService.class).useObjectData(entity, objectUniqueId);
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        Entity entity = Factory.get(PlayService.class).getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityNetwork.class).useObjectData(entity, objectUniqueId);
        }
    }
    
}
