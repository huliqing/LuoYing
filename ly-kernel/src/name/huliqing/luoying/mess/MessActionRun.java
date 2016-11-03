/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 向目标位置移动
 * @author huliqing
 */
@Serializable
public class MessActionRun extends MessBase {
    
    // 角色唯一id
    private long actorId;
    // 目标位置
    private Vector3f pos;

    public MessActionRun() {}

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        PlayService playService = Factory.get(PlayService.class);
        ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
        ActionService actionService = Factory.get(ActionService.class);
        
        Entity actor = (Actor) playService.getEntity(actorId);
        if (actor == null) 
            return;

        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Long uniqueActorId = cd != null ? cd.getEntityId() : null;
        
        if (uniqueActorId != actor.getData().getUniqueId()) {
            return; // 不允许控制别人的角色
        }
        
        // remove20151229服务端以后一直打开AI
//        actorService.setAutoAi(actor, false);
   
        // remove20161103
//        // 必须清除跟随
//        actorNetwork.setFollow(actor, -1);
        
        // 走向目标,action不需要广播
        actionService.playRun(actor, pos);
    }
    
}
