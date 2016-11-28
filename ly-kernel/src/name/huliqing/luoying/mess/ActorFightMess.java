/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 客户端向服务端发出攻击命令
 * @author huliqing
 */
@Serializable
public class ActorFightMess extends GameMess {

    // 被攻击的角色id,-1表示没有攻击目标，但是让角色转入自动攻击状态
    private long targetId = -1;
    
    public ActorFightMess() {}

    /**
     * 获取攻击目标
     * @return 
     */
    public long getTargetId() {
        return targetId;
    }

    /**
     * 设置被攻击的角色id,如果设置为-1，表示没有攻击目标，但是让角色转入自动攻击状态
     * @param targetId 
     */
    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        super.applyOnServer(gameServer, source);
        PlayService playService = Factory.get(PlayService.class);
        PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
        
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Long actorId = cd != null ? cd.getEntityId() : null;
        
        Entity actor = playService.getEntity(actorId);
        Entity target = playService.getEntity(targetId);
        playNetwork.attack(actor, target);
    }

}
