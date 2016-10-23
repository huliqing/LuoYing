/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.mess.MessActionRun;
import name.huliqing.luoying.network.Network;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.mess.MessActorSpeak;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkManager;

/**
 *
 * @author huliqing
 */
public class GameNetworkImpl implements GameNetwork {
    private GameService gameService;
    private ActorService actorService;
    private ActionService actionService;
    private ActorNetwork actorNetwork;
    private final static Network NETWORK = Network.getInstance();
    
    @Override
    public void inject() {
        gameService = Factory.get(GameService.class);
        actorService = Factory.get(ActorService.class);
        actionService = Factory.get(ActionService.class);
        
        actorNetwork = Factory.get(ActorNetwork.class);
    }

    @Override
    public void addMessage(String message, MessageType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncGameInitToClient(HostedConnection client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncObject(NetworkObject object, SyncData syncData, boolean reliable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void speak(Entity actor, String mess, float useTime) {
        if (NETWORK.isClient()) {
            // ignore
        } else {
            MessActorSpeak mas = new MessActorSpeak();
            mas.setActorId(actor.getData().getUniqueId());
            mas.setMess(mess);
            NETWORK.broadcast(mas);
            gameService.speak(actor, mess, useTime);
        }
    }
    
    @Override
    public void talk(Talk talk) {
        if (NETWORK.isClient()) {
            // ignore
        } else {
            talk.setNetwork(true);
            TalkManager.getInstance().startTalk(talk);
        }
    }
    
    @Override
    public void playRunToPos(Entity actor, Vector3f worldPos) {
        // 死亡后不能再移动
        if (actorService.isDead(actor)) {
            return;
        }
        
        if (NETWORK.isClient()) {
            MessActionRun runAction = new MessActionRun();
            runAction.setActorId(actor.getData().getUniqueId());
            runAction.setPos(worldPos);
            NETWORK.sendToServer(runAction);
        } else {
            
            // 不需要广播
            // ...

            // setFollow不使用Network也可以,因客户端无逻辑，但是这里尽量保持CS状态一致
            actorNetwork.setFollow(actor, -1);
            gameService.playRunToPos(actor, worldPos);

        }
    }

}
