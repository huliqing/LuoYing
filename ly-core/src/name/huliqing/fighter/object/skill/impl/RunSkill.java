/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.object.skill.Walk;

/**
 *
 * @author huliqing
 */
public class RunSkill extends WalkSkill implements Walk{
    private final ConfigService configService = Factory.get(ConfigService.class);

    public RunSkill() {}

    public RunSkill(SkillData skillData) {
        super(skillData);
        baseSpeed = skillData.getAsFloat("baseSpeed", configService.getBaseRunSpeed());
    }
    
}
