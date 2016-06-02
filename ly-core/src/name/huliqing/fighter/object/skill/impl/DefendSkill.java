/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.skill.AbstractSkill;

/**
 * 防守技能
 * @author huliqing
 */
public class DefendSkill extends AbstractSkill {

    public DefendSkill(SkillData data) {
       super(data);
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {
    }

//    @Override
//    public boolean isInRange(Actor actor) {
//        return false;
//    }
    
}
