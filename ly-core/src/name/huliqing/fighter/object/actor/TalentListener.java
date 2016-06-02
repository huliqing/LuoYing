/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.actor;

import name.huliqing.fighter.data.TalentData;

/**
 * 监听角色的天赋状态变化
 * @author huliqing
 */
public interface TalentListener {
    
    /**
     * 角色添加了天赋时触发
     * @param actor
     * @param talentData 
     */
    void onTalentAdded(Actor actor, TalentData talentData);
    
    /**
     * 角色增加或减少了天赋点数时触发
     * @param actor
     * @param talentId
     * @param pointsAmount 实际增加或减少的天赋点数 
     */
    void onTalentPointsChange(Actor actor, String talentId, int pointsAmount);
}
