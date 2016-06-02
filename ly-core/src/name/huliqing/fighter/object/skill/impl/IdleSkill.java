/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.data.SkillData;

/**
 *
 * @author huliqing
 */
public class IdleSkill extends SimpleSkill {
    
    public IdleSkill() {}
    
    public IdleSkill(SkillData skillData) {
       super(skillData);
    }

    @Override
    public void init() {
        super.init(); 
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {
        // ignore
    }

//    @Override
//    public boolean isInRange(Actor character) {
//        throw new UnsupportedOperationException("Idle skill Not supported isInRange!");
//    }

    
}
