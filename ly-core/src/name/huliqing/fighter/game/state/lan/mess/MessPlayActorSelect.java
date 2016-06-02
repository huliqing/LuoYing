/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.ActorConstants;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.enums.SkillType;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.state.lan.GameServer;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;

/**
 * 告诉服务端，客户端选择了哪一个角色id
 * @author huliqing
 */
@Serializable
public class MessPlayActorSelect extends MessBase {

    private String actorId;
    // 玩家所使用的角色名称
    private String actorName;
    
    public MessPlayActorSelect() {
    }
    
    public MessPlayActorSelect(String actorId, String actorName) {
        this.actorId = actorId;
        this.actorName = actorName;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        MessSCActorSelectResult result = new MessSCActorSelectResult();
        if (actorId == null) {
            result.setSuccess(false);
            result.setError("No actorId selected!");
            gameServer.send(source, result);
            return;
        }

        ActorService actorService = Factory.get(ActorService.class);
        LogicService logicService = Factory.get(LogicService.class);
        SkillService skillService = Factory.get(SkillService.class);
        PlayService playService = Factory.get(PlayService.class);
        PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
        
        // 1.载入和初始化玩家类角色
        Actor playerActor = actorService.loadActor(actorId);
        playerActor.setPlayer(true);
        logicService.resetPlayerLogic(playerActor);
        actorService.setName(playerActor, actorName);
        // 暂时以1作为默认分组
        actorService.setTeam(playerActor, 1);
        skillService.playSkill(playerActor, skillService.getSkill(playerActor, SkillType.wait).getId(), false);
        
        // 2.角色要绑定客户端ID，同时客户端也要绑定记住角色ID。
        playerActor.getModel().setUserData(ActorConstants.USER_DATA_CLIENT_CONNECTION_ID, source.getId());
        source.setAttribute(GameServer.ATTR_ACTOR_UNIQUE_ID, playerActor.getData().getUniqueId());
        result.setActorUniqueId(playerActor.getData().getUniqueId());
        result.setSuccess(true);
        gameServer.send(source, result);
        
        // 3.将角色添加到场景,注：在将ID绑定到客户端之后再添加角色
        playNetwork.addActor(playerActor);
        
        // 4.通知主机和客户端,在HUD上显示角色进入游戏的消息
        String message = ResourceManager.get("lan.enterGame"
                , new Object[] {playerActor.getData().getName()});
        MessageType type = MessageType.item;
        MessMessage notice = new MessMessage();
        notice.setMessage(message);
        notice.setType(type);
        gameServer.broadcast(notice); // 通知所有客户端
        playService.addMessage(message, type);  // 通知主机
        
        // 5.当客户端选择了角色之后，客户端绑定了角色，同时也就有了一个角色名，
        // 所以这里向客户端更新列表信息。让客户端的客户端连接列表可以显示出角色名字
        gameServer.broadcast(new MessSCClientList(gameServer.getClients()));
    }
    
    
}
