/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.network;

import name.huliqing.core.data.ConnData;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mess.MessBase;
import name.huliqing.core.mess.MessMessage;
import name.huliqing.core.mess.MessActorTransform;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.mvc.service.ActorService;

/**
 * 默认的服务端监听器,用于监听来自客户端连接的消息。
 * @author huliqing
 */
public class DefaultServerListener extends AbstractServerListener<Actor> {
    private static final Logger LOG = Logger.getLogger(DefaultServerListener.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final List<Actor> syncObjects = new LinkedList<Actor>();
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
            for (Actor actor : syncObjects) {
                syncTempCache.setActorId(actor.getData().getUniqueId());
                syncTempCache.setLocation(actor.getSpatial().getWorldTranslation());
                syncTempCache.setWalkDirection(actorService.getWalkDirection(actor));
                syncTempCache.setViewDirection(actorService.getViewDirection(actor));
                gameServer.broadcast(syncTempCache);
            }
        }
    }

    @Override
    public void addSyncObject(Actor syncObject) {
        if (syncObject != null && !syncObjects.contains(syncObject)) {
            syncObjects.add(syncObject);
        }
    }

    @Override
    public boolean removeSyncObject(Actor syncObject) {
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
        
        Actor clientPlayer = playService.findActor(cd.getActorId());
        if (clientPlayer == null)
            return;

        // 1.将客户端角色的所有宠物移除出场景,注意是宠物，不要把非生命的（如防御塔）也一起移除
        List<Actor> actors = playService.findAllActor();
        if (actors != null && !actors.isEmpty()) {
            for (Actor actor : actors) {
                if (actor.getData().getOwnerId() == clientPlayer.getData().getUniqueId() &&  actor.getData().isLiving()) {
                    playNetwork.removeObject(actor);
                }
            }
        }

        // 2.将客户端角色移除出场景
        playNetwork.removeObject(clientPlayer);

        // 3.通知所有客户端（不含主机）
        String message = ResourceManager.get(ResConstants.LAN_CLIENT_EXISTS, new Object[] {clientPlayer.getData().getName()});
        MessMessage notice = new MessMessage();
        notice.setMessage(message);
        notice.setType(MessageType.notice);
        gameServer.broadcast(notice);

        // 4.通知主机
        if (LY.getPlayState() != null) {
            playService.addMessage(message, MessageType.notice);
        }
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
