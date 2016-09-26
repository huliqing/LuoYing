/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.skill;

import name.huliqing.core.data.SkillData;

/**
 *
 * @author huliqing
 */
public class RunSkill extends WalkSkill implements Walk{

    @Override
    public void setData(SkillData data) {
        super.setData(data); 
        baseSpeed = data.getAsFloat("baseSpeed", 6);
    }
    
}
