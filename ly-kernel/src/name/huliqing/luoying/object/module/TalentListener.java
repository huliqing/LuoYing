/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.talent.Talent;

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
    void onTalentAdded(Entity actor, Talent talent);
    
    /**
     * 在从角色身上移除一个天赋后触发。
     * @param actor
     * @param talent 被移除的天赋
     */
    void onTalentRemoved(Entity actor, Talent talent);
    
    /**
     * 角色增加或减少了天赋点数后触发该方法。
     * @param actor
     * @param talent 发生了点数变动的天赋的data.
     * @param pointsAmount 实际增加或减少的天赋点数
     */
    void onTalentPointsChange(Entity actor, Talent talent, int pointsAmount);
}
