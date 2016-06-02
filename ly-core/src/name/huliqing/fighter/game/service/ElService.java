/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.object.el.HitEl;
import name.huliqing.fighter.object.el.XpDropEl;

/**
 *
 * @author huliqing
 */
public interface ElService extends Inject{
    
    /**
     * 给定一个levelEl的id及等级来获取等级值。注：levelElId就跟名字一样，必
     * 须是一个elLevel类型的等级公式。
     * @param levelElId
     * @param level
     * @return 
     */
    float getLevelEl(String levelElId, int level);
    
    /**
     * 获取一个经验掉落公式
     * @param id
     * @return 
     */
    XpDropEl getXpDropEl(String id);

    /**
     * 获取一个HitEl.
     * @param id
     * @return 
     */
    HitEl getHitEl(String id);
    
}
