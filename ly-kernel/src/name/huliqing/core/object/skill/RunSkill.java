/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.Factory;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.object.skill.Walk;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class RunSkill<T extends SkillData> extends WalkSkill<T> implements Walk{
    private final ConfigService configService = Factory.get(ConfigService.class);

    @Override
    public void setData(T data) {
        super.setData(data); 
        baseSpeed = data.getAsFloat("baseSpeed", configService.getBaseRunSpeed());
    }
    
}
