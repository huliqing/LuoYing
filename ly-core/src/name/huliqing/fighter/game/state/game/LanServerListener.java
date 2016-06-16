/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.game;

import com.jme3.app.Application;
import name.huliqing.fighter.game.state.lan.GameServer;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.ResConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.DefaultServerListener;
import name.huliqing.fighter.game.mess.MessBase;
import name.huliqing.fighter.game.mess.MessMessage;
import name.huliqing.fighter.game.mess.MessActorTransform;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class LanServerListener extends DefaultServerListener<Actor> {
    private static final Logger LOG = Logger.getLogger(LanServerListener.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final List<Actor> syncObjects = new LinkedList<Actor>();
    private float syncTimer = 0;
    private final float syncFrequency = 1f/5f;
    
    private final MessActorTransform syncTempCache = new MessActorTransform();
    
    public LanServerListener(Application app) {
        super(app);
    }

    @Override
    public void update(float tpf, GameServer gameServer) {
        syncTimer += tpf;
        if (syncTimer >= syncFrequency) {
            syncTimer = 0;
            for (Actor actor : syncObjects) {
                syncTempCache.setActorId(actor.getData().getUniqueId());
                syncTempCache.setLocation(actor.getLocation());
                syncTempCache.setWalkDirection(actor.getWalkDirection());
                syncTempCache.setViewDirection(actor.getViewDirection());
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

        // 1.将客户端角色的所有宠物移除出场景
        List<Actor> actors = playService.findAllActor();
        if (actors != null && !actors.isEmpty()) {
            for (Actor actor : actors) {
                if (actor.getData().getOwnerId() == clientPlayer.getData().getUniqueId()) {
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
        if (Common.getPlayState() != null) {
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
