/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.SkillModule;
import name.huliqing.luoying.object.skill.Skill;

/**
 *
 * @author huliqing
 */
@Serializable
public final class MessSkillPlay extends MessBase {
    
    private long actorId;
    // 要执行的技能id
    private String skillId;
    
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
        super.applyOnServer(gameServer, source);
        ConnData cd = source.getAttribute(ConnData.CONN_ATTRIBUTE_KEY);
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
         // 找不到指定的角色
        if (actor == null) {
            return;
        }
        // 角色必须是客户端所控制的。客户端角色不能强制执行技能，并且也不能自己指定wantNotInterruptSkills参数
        if (actor.getData().getUniqueId() == cd.getEntityId()) {
            SkillModule skillModule = actor.getModuleManager().getModule(SkillModule.class);
            Factory.get(SkillNetwork.class).playSkill(actor, skillModule.getSkill(skillId), false);
        }
    }
    
    @Override
    public void applyOnClient() {
        super.applyOnClient();
        Entity actor = Factory.get(PlayService.class).getEntity(actorId);
        if (actor != null) {
            SkillModule skillModule = actor.getModuleManager().getModule(SkillModule.class);
            Skill skill = skillModule.getSkill(skillId);
            skillModule.playSkill(skill, true);
        }
    }
  
}
