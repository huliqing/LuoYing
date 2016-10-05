/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.skill;

import com.jme3.math.Vector3f;

/**
 * 定义死亡技能
 * @author huliqing
 */
public interface Dead {
    
    /**
     * 指定一个力的方向和大小,当某些使用物理特性的死亡技能时比较有用,比如布娃娃系
     * 统, 这个力指定了死亡时的受攻击方向.
     * 比如角色A攻击B,则这个力可计算为 B.subtract(A).normalizeLocal().multLocal(力的大小).
     * 力的大小可以使用攻击力的大小作为参考
     * @param force 
     */
    void applyForce(Vector3f force);
    

}
