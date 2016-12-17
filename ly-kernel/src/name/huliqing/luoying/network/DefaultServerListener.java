/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
