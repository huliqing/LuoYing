/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.play;

import com.jme3.app.Application;
import name.huliqing.fighter.game.state.lan.GameServer;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.DefaultServerListener;
import name.huliqing.fighter.game.state.lan.mess.MessBase;
import name.huliqing.fighter.game.state.lan.mess.MessMessage;
import name.huliqing.fighter.game.state.lan.mess.MessSCActorRemove;
import name.huliqing.fighter.game.state.lan.mess.MessActorTransform;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class LanServerListener extends DefaultServerListener<Actor> {
//    private static final Logger LOG = Logger.getLogger(LanServerListener.class.getName());
    
    private final PlayService playService = Factory.get(PlayService.class);
    private final List<Actor> syncObjects = new LinkedList<Actor>();
    private float syncTimer = 0;
    private float syncFrequency = 1f/5f;
    
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
        Long actorUniqueId = conn.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
        if (actorUniqueId != null) {
            Actor clientActor = playService.findActor(actorUniqueId);
            if (clientActor != null) {
                // 将客户端角色移出场景
                playService.removeObject(clientActor.getModel());
                MessSCActorRemove mess = new MessSCActorRemove();
                mess.setActorId(actorUniqueId);
                gameServer.broadcast(mess);
                
                // 广播，告诉所有客户端
                String message = ResourceManager.get("lan.leaveGame"
                        , new Object[] {clientActor.getData().getName()});
                MessageType type = MessageType.notice;
                MessMessage notice = new MessMessage();
                notice.setMessage(message);
                notice.setType(type);
                gameServer.broadcast(notice);
                
                // 主机获得通知
                if (Common.getPlayState() != null) {
                    playService.addMessage(message, type);
                }
            }
        }
    }

    @Override
    protected void processServerMessage(GameServer gameServer, HostedConnection source, Message m) {
        ((MessBase)m).applyOnServer(gameServer, source);
    }
}
