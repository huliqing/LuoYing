/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.layer.network.TalentNetwork;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.module.TalentListener;

/**
 *
 * @author huliqing
 */
public interface TalentService extends TalentNetwork {
    
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
