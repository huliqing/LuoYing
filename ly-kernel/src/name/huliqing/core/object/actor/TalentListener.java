/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.actor;

import name.huliqing.core.data.TalentData;

/**
 * 监听角色的天赋状态变化
 * @author huliqing
 */
public interface TalentListener {
    
    /**
     * 在角色添加了天赋后触发
     * @param actor
     * @param talentData 新添加的天赋的data
     */
    void onTalentAdded(Actor actor, TalentData talentData);
    
    /**
     * 在从角色身上移除一个天赋后触发。
     * @param actor
     * @param talentData 被移除的天赋的data
     */
    void onTalentRemoved(Actor actor, TalentData talentData);
    
    /**
     * 角色增加或减少了天赋点数后触发该方法。
     * @param actor
     * @param data 发生了点数变动的天赋的data.
     * @param pointsAmount 实际增加或减少的天赋点数
     */
    void onTalentPointsChange(Actor actor, TalentData data, int pointsAmount);
}
