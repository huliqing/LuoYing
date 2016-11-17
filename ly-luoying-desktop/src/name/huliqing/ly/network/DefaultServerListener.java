/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.network;

import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.mess.MessBase;
import name.huliqing.luoying.mess.MessActorTransform;
import name.huliqing.luoying.layer.network.PlayNetwork;
import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.network.AbstractServerListener;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.mess.MessMessage;

/**
 * 默认的服务端监听器,用于监听来自客户端连接的消息。
 * @author huliqing
 */
public class DefaultServerListener extends AbstractServerListener<Entity> {
    private static final Logger LOG = Logger.getLogger(DefaultServerListener.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final GameService gameService = Factory.get(GameService.class);
//    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final List<Entity> syncObjects = new LinkedList<Entity>();
    private float syncTimer = 0;
    private final float syncFrequency = 1f/5f;
    
    private final MessActorTransform syncTempCache = new MessActorTransform();
    
    public DefaultServerListener(Application app) {
        super(app);
    }

    @Override
    public void update(float tpf, GameServer gameServer) {
        syncTimer += tpf;
        if (syncTimer >= syncFrequency) {
            syncTimer = 0;
            for (Entity actor : syncObjects) {
                syncTempCache.setActorId(actor.getData().getUniqueId());
                syncTempCache.setLocation(actor.getSpatial().getWorldTranslation());
                syncTempCache.setWalkDirection(actorService.getWalkDirection(actor));
                syncTempCache.setViewDirection(actorService.getViewDirection(actor));
                gameServer.broadcast(syncTempCache);
            }
        }
    }

    @Override
    public void addSyncObject(Entity syncObject) {
        if (syncObject != null && !syncObjects.contains(syncObject)) {
            syncObjects.add(syncObject);
        }
    }

    @Override
    public boolean removeSyncObject(Entity syncObject) {
        return syncObjects.remove(syncObject);
    }

    @Override
    public void cleanup() {
        syncObjects.clear();
    }

    @Override
    protected void onClientRemoved(GameServer gameServer, HostedConnection conn) {
        ConnData cd = conn.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        if (cd == null)
            return;
        
        Entity clientPlayer = playService.getEntity(cd.getEntityId());
        if (clientPlayer == null)
            return;

        // 1.将客户端角色的所有宠物移除出场景,注意是宠物，不要把非生命的（如防御塔）也一起移除
        List<Entity> actors = playService.getEntities(Entity.class, null);
        if (actors != null && !actors.isEmpty()) {
            for (Entity actor : actors) {
                if (gameService.getOwner(actor) == clientPlayer.getData().getUniqueId() && gameService.isBiology(actor)) {
                    playNetwork.removeEntity(actor);
                }
            }
        }

        // 2.将客户端角色移除出场景
        playNetwork.removeEntity(clientPlayer);

        // 3.通知所有客户端（不含主机）
        String message = ResManager.get(ResConstants.LAN_CLIENT_EXISTS, new Object[] {clientPlayer.getData().getName()});
        MessMessage notice = new MessMessage();
        notice.setMessage(message);
        notice.setType(MessageType.notice);
        gameServer.broadcast(notice);

//        // 4.通知主机
//        if (Ly.getPlayState() != null) {
//            playService.addMessage(message, MessageType.notice);
//        }
    }

    @Override
    protected void processServerMessage(GameServer gameServer, HostedConnection source, Message m) {
        if (m instanceof MessBase) {
            ((MessBase)m).applyOnServer(gameServer, source);
        } else {
            ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
            LOG.log(Level.WARNING, "Unknow message type from client, clientId={0}, clientName={1}, message={2}"
                    , new Object[] {cd.getClientId(), cd.getClientName(), m.getClass().getName()});
        }
    }
}
