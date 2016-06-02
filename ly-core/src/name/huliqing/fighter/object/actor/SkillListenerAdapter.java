/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actor;

import name.huliqing.fighter.object.skill.Skill;

/**
 * 技能侦听适配器
 * @author huliqing
 */
public class SkillListenerAdapter implements SkillListener {

    @Override
    public boolean onSkillHookCheck(Actor source, Skill skill) {
        return true;
    }

    @Override
    public void onSkillStart(Actor source, Skill skill) {
        // ignore
    }

    @Override
    public void onSkillEnd(Actor source, Skill skill) {
        // ignore
    }
    
}
