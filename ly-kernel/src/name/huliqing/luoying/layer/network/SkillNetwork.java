/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.network;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.skill.Skill;

/**
 * @author huliqing
 */
public interface SkillNetwork extends Inject {

//    /**
//     * 执行一个技能实例
//     * @param actor
//     * @param skill
//     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
//     * 任何限制，直接执行技能即，该方法将保证返回true。
//     * @return 
//     */
//    boolean playSkill(Entity actor, Skill skill, boolean force);
    
    /**
     * 执行“步行”技能，步行的速度等受角色属性的影响
     * @param actor
     * @param walkSkill
     * @param dir 目标
     * @param faceToDir
     * @param force 是否强制执行,注：如果强制执行则忽略<strong>所有</strong>
     * 任何限制，直接执行技能即，该方法将保证返回true。
     * @return s
     */
    boolean playWalk(Entity actor, Skill walkSkill, Vector3f dir, boolean faceToDir, boolean force);
    
}
