/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.message;

import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class EntitySkillUseMessage extends EntityMessage {
    
    private final SkillData skillData;
    
    public EntitySkillUseMessage(int stateCode, String message, Entity entity, SkillData skillData) {
        super(stateCode, message, entity);
        this.skillData = skillData;
    }

    /**
     * 获取被使用的技能
     * @return 
     */
    public SkillData getSkillData() {
        return skillData;
    }
    
}
