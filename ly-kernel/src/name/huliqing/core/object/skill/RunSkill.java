/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.service.ConfigService;

/**
 *
 * @author huliqing
 */
public class RunSkill extends WalkSkill implements Walk{
    private final ConfigService configService = Factory.get(ConfigService.class);

    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        baseSpeed = data.getAsFloat("baseSpeed", configService.getBaseRunSpeed());
    }
    
}
