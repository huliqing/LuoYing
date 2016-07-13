/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill.impl;

import name.huliqing.fighter.data.SkillData;
import name.huliqing.fighter.object.skill.AbstractSkill;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class SimpleSkill<T extends SkillData> extends AbstractSkill<T> {

    @Override
    protected void doUpdateLogic(float tpf) {}
    
}
