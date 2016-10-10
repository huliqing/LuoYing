/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.object.entity.Entity;

/**
 * 通知客户端给角色添加一个技能
 * @author huliqing
 */
@Serializable
public class MessActorAddSkill extends MessBase {
    
    // 要添加技能的角色
    private long actorId;
    // 被添加的技能ID,因为角色技能不可重复，所以这里不需要唯一ID
    private String skillId;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        SkillService skillService = Factory.get(SkillService.class);
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            skillService.addSkill(actor, skillId);
        }
    }
    
}
