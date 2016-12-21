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

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.mess.ActorTransformMess;
import java.util.LinkedList;
import java.util.List;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 默认的服务端监听器,用于监听来自客户端连接的消息。
 * @author huliqing
 */
public abstract class DefaultServerListener extends AbstractServerListener {
    private final ActorService actorService = Factory.get(ActorService.class);
    private final List<Entity> syncObjects = new LinkedList<Entity>();
    private float syncTimer = 0;
    private final float syncFrequency = 1f/4f;
    
    private final ActorTransformMess syncTempCache = new ActorTransformMess();

    @Override
    public void update(float tpf, GameServer gameServer) {
        syncTimer += tpf;
        if (syncTimer >= syncFrequency) {
            syncTimer = 0;
            syncEntities(gameServer);
        }
    }
    
    @Override
    public void cleanup() {
        syncObjects.clear();
    }

    @Override
    public void addSyncEntity(Entity syncEntity) {
        if (syncEntity != null && !syncObjects.contains(syncEntity)) {
            syncObjects.add(syncEntity);
        }
    }

    @Override
    public boolean removeSyncEntity(Entity syncEntity) {
        return syncObjects.remove(syncEntity);
    }
    
    /**
     * 将服务端上的实体状态同步到客户端，这个方法会在服务端运行时持续循环运行。
     * @param gameServer
     */
    protected void syncEntities(GameServer gameServer) {
        for (Entity actor : syncObjects) {
            syncTempCache.setActorId(actor.getData().getUniqueId());
            syncTempCache.setLocation(actor.getSpatial().getWorldTranslation());
            syncTempCache.setWalkDirection(actorService.getWalkDirection(actor));
            syncTempCache.setViewDirection(actorService.getViewDirection(actor));
            gameServer.broadcast(syncTempCache);
        }
    }

}
