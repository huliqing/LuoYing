/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.el.HitCheckEl;
import name.huliqing.luoying.object.el.HitEl;
import name.huliqing.luoying.object.el.LevelEl;
import name.huliqing.luoying.object.el.CheckEl;

/**
 *
 * @author huliqing
 */
public interface ElService extends Inject{
    
    /**
     * 载入一条CheckEl，参数可能是elId或是一条CheckEl，如: #{!source.attributeDead}
     * @param idOrExpression
     * @return 
     */
    CheckEl createCheckEl(String idOrExpression);
    
    /**
     * 载入一条HitCheckEl, 参数可以是elId或是一条HitCheckEl表达式, 如：#{source.attributeGroup != target.attributeGroup}
     * @param idOrExpression
     * @return 
     */
    HitCheckEl createHitCheckEl(String idOrExpression);
    
    /**
     * 载入一条HitEl, 参数可以是elId或是一条HitEl表达式, 如：#{source.attributeAttack - target.attributeDefense}
     * @param idOrExpression
     * @return 
     */
    HitEl createHitEl(String idOrExpression);
    
    /**
     * 载入一条LevelEl, 参数可以是elId或是一条LevelEl表达式，如: #{level * 2}
     * @param idOrExpression
     * @return 
     */
    LevelEl createLevelEl(String idOrExpression);
    
}
