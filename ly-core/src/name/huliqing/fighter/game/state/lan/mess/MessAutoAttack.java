/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 客户端向服务端发出自动攻击命令
 * @author huliqing
 */
@Serializable
public class MessAutoAttack extends MessBase {

    // 被攻击的角色id,-1表示没有攻击目标，但是让角色转入自动攻击状态
    private long targetId = -1;
    
    public MessAutoAttack() {}

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
        // remove20151216
//        PlayService playService = Factory.get(PlayService.class);
//        ActorService actorService = Factory.get(ActorService.class);
//        ActionNetwork actionNetwork = Factory.get(ActionNetwork.class);
//        
//        Long actorId = source.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
//        Actor actor = playService.findActor(actorId);
//        
//        // TODO：暂不做验证处理
//        actorService.setAutoAi(actor, true);
//        Actor enemy = playService.findActor(targetId);
//        if (enemy != null) {
//            actionNetwork.playFight(actor, enemy, null);
//        }
        
        
        PlayService playService = Factory.get(PlayService.class);
        PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
        Long actorId = source.getAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID);
        Actor actor = playService.findActor(actorId);
        Actor target = playService.findActor(targetId);
        playNetwork.attack(actor, target);
    }

}
