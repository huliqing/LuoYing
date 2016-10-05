/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import name.huliqing.ly.data.TalentData;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.talent.Talent;

/**
 * 监听角色的天赋状态变化
 * @author huliqing
 */
public interface TalentListener {
    
    /**
     * 在角色添加了天赋后触发
     * @param actor
     * @param talent 新添加的天赋
     */
    void onTalentAdded(Actor actor, Talent talent);
    
    /**
     * 在从角色身上移除一个天赋后触发。
     * @param actor
     * @param talent 被移除的天赋
     */
    void onTalentRemoved(Actor actor, Talent talent);
    
    /**
     * 角色增加或减少了天赋点数后触发该方法。
     * @param actor
     * @param talent 发生了点数变动的天赋的data.
     * @param pointsAmount 实际增加或减少的天赋点数
     */
    void onTalentPointsChange(Actor actor, Talent talent, int pointsAmount);
}
