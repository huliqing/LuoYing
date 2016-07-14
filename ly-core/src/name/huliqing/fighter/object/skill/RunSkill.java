/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.object.skill.Walk;

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
