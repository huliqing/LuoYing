/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.skill.AbstractSkill;

/**
 *
 * @author huliqing
 * @param <T>
 */
public class IdleSkill<T extends SkillData> extends AbstractSkill<T> {
    
    @Override
    protected void doUpdateLogic(float tpf) {
        // ignore
    }

    
}
