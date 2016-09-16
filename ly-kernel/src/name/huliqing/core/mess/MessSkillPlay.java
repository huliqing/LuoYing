/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ConnData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessSkillPlay extends MessBase {
    
    protected long actorId;
    protected String skillId;
    
     public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setSkillId(SkillData skillData) {
        if (skillData != null) {
            skillId = skillData.getId();
        }
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection source) {
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
         // 找不到指定的角色
        if (actor == null) {
            return;
        }
        // 角色必须是客户端所控制的。
        if (actor.getData().getUniqueId() == cd.getActorId()) {
            Factory.get(SkillNetwork.class).playSkill(actor, skillId, false);
        }
    }

    @Override
    public void applyOnClient() {
        Actor actor = Factory.get(PlayService.class).findActor(actorId);
        if (actor != null) {
            Factory.get(SkillService.class).playSkill(actor, skillId, true);
        }
    }
    
  
}
