/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.skill.Skill;

/**
 * 技能侦听适配器
 * @author huliqing
 */
public class SkillListenerAdapter implements SkillListener {

    @Override
    public boolean onSkillHookCheck(Skill skill) {
        return true;
    }

    @Override
    public void onSkillStart(Skill skill) {
    }

    @Override
    public void onSkillEnd(Skill skill) {
    }
    
}
