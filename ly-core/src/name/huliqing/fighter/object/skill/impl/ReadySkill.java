/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.object.skill.AbstractSkill;

/**
 * @author huliqing
 */
public class ReadySkill extends AbstractSkill {

    public ReadySkill(SkillData data) {
       super(data);
    }

    @Override
    protected void doUpdateLogic(float tpf) {}

//    @Override
//    public boolean isInRange(Actor target) {
//        return false;
//    }
    
}
