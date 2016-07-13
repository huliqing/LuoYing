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
public class SkinSkill<T extends SkillData> extends AbstractSkill<T> {

    // 武器挂起或取出时的动画时间点，这个时间点取值[0.0~1.0],也即武器出现在手上
    // 或在挂靠点上（如背上，腿侧）的时间点。这个时间点是相对于取武器技能时间而定的。
    private float hangTimePoint;
    
    @Override
    public void setData(T data) {
        super.setData(data); 
       this.hangTimePoint = data.getAsFloat("hangTimePoint", 0.5f);
    }
    
    @Override
    protected void doUpdateLogic(float tpf) {
    }
    
    /**
     * 武器挂起或取出时的动画时间点，这个时间点取值[0.0~1.0],也即武器出
     * 现在手上或在挂靠点上（如背上，腿侧）的时间点。这个时间点是相对于取武器技能时间而定的。
     * @return 
     */
    public float getHangTimePoint() {
        return hangTimePoint;
    }
}
