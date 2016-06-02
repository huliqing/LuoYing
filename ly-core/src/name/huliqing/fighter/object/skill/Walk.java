/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.skill;

import com.jme3.math.Vector3f;

/**
 * 步行和跑步接口,注:步行和跑步逻辑是一样的,不同的只是animation动画和步行速度
 * @author huliqing
 */
public interface Walk extends Skill {
    
    /**
     * 设置行动方向（非位置）
     * @param walkDirection 
     */
    void setWalkDirection(Vector3f walkDirection);
    
    /**
     * 设置视角方向(非位置)
     * @param viewDirection 
     */
    void setViewDirection(Vector3f viewDirection);
    
    /**
     * 设置行动的基本速度
     * @param baseSpeed 
     */
    void setBaseSpeed(float baseSpeed);
}
