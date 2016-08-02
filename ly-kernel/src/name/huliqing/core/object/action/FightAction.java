/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.action;

import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.actor.Actor;

/**
 * 角色的战斗PK行为
 * @author huliqing
 */
public interface FightAction extends Action {
    
    /**
     * 设置使用的技能, 注：该技能必须满足以下条件：
     * 1.与当前角色的武器类型匹配。
     * 2.必须是攻击技能,可为普通攻击，技能攻击，魔法攻击
     * @param skill 
     */
    public void setSkill(SkillData skill);
    
    /**
     * 设置目标敌人
     * @param actor 
     */
    public void setEnemy(Actor actor);
    
}
