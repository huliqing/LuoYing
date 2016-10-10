/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import java.util.List;
import name.huliqing.ly.data.TalentData;
import name.huliqing.ly.layer.network.TalentNetwork;
import name.huliqing.ly.object.entity.Entity;
import name.huliqing.ly.object.module.TalentListener;
import name.huliqing.ly.object.talent.Talent;

/**
 *
 * @author huliqing
 */
public interface TalentService extends TalentNetwork {
    
    /**
     * 给角色添加一个天赋
     * @param actor
     * @param talentData 
     */
    void addTalent(Entity actor, TalentData talentData);
    
    /**
     * 添加天赋
     * @param actor
     * @param talent 
     */
    void addTalent(Entity actor, Talent talent);
    
    /**
     * 移除角色身上指定的天赋
     * @param actor
     * @param talentId 
     */
    void removeTalent(Entity actor, String talentId);
    
    /**
     * 获取角色身上所有的天赋,如果没有任何天赋则返回null.
     * @param actor
     * @return 
     */
    List<Talent> getTalents(Entity actor);
    
    /**
     * 获取角色当前可用的天赋点数
     * @param actor
     * @return 
     */
    int getTalentPoints(Entity actor);
    
    /**
     * 添加天赋侦听器
     * @param actor
     * @param talentListener 
     */
    void addTalentListener(Entity actor, TalentListener talentListener);
    
    /**
     * 移除天赋侦听器
     * @param actor
     * @param talentListener 
     */
    void removeTalentListener(Entity actor, TalentListener talentListener);
}
