/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.fighter.data.SkillData;

/**
 *
 * @author huliqing
 */
@Serializable
public abstract class MessSkillAbstract extends MessBase {
    
    protected long actorId;
    // －1代表不传递特定的技能ID,因为一些角色可能没有相应的技能。比如部分角色可能没有
    // wait技能，这是传递－1，在客户端或服务端接到该情况时视情况处理，如执行playReset代替wait技能
    protected String skillId;
    protected boolean force;
    
    public MessSkillAbstract() {}

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

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
    
}
