/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.skill.Skill;

/**
 * 监听角色技能的执行。
 * @author huliqing
 */
public interface SkillListener {
    
    /**
     * 当角色添加了一个新技能后该方法被调用。
     * @param actor
     * @param skill 新添加的技能
     */
    void onSkillAdded(Actor actor, Skill skill);
    
    /**
     * 当角色被移除了一个技能后该方法被调用。
     * @param actor
     * @param skill 被移除的技能
     */
    void onSkillRemoved(Actor actor, Skill skill);
    
}
