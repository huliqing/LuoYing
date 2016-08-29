/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.object.el.El;
import name.huliqing.core.object.el.HitEl;
import name.huliqing.core.object.el.AttributeEl;

/**
 *
 * @author huliqing
 */
public interface ElService extends Inject{
    
    /**
     * 获取指定ID的EL
     * @param elId
     * @return 
     */
    El getEl(String elId);
    
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
    AttributeEl getAttributeEl(String id);

    /**
     * 获取一个HitEl.
     * @param id
     * @return 
     */
    HitEl getHitEl(String id);
    
}
