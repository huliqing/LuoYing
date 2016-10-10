/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.network;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.entity.Entity;

/**
 * @author huliqing
 */
public interface TalentNetwork extends Inject {
    
    /**
     * 给角色添加一个天赋
     * @param actor
     * @param talentId 
     */
    void addTalent(Entity actor, String talentId);
    
    /**
     * 增加角色某个天赋的点数,注：角色必须拥有足够的可用天赋点数才能增加。
     * 否则该方法将什么也不处理，当天赋的点数增加后，角色的可用天赋将会减少。
     * @param actor 指定的角色
     * @param talentId 天赋ID
     * @param points 增加的点数，必须是正数
     */
    void addTalentPoints(Entity actor, String talentId, int points);
    
}
