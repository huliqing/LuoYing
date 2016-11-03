/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.layer.service;

import name.huliqing.luoying.Inject;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.el.STNumberEl;
import name.huliqing.luoying.object.el.LNumberEl;
import name.huliqing.luoying.object.el.SBooleanEl;

/**
 *
 * @author huliqing
 */
public interface ElService extends Inject{
    
    /**
     * 载入一条SBooleanEl，参数可能是elId或是一条SBoolean表达式，如: #{!source.attributeDead}
     * @param idOrExpression
     * @return 
     */
    SBooleanEl createSBooleanEl(String idOrExpression);
    
    /**
     * 载入一条STBooleanEl, 参数可以是elId或是一条STBoolean表达式, 如：#{s.attributeGroup != t.attributeGroup}
     * @param idOrExpression
     * @return 
     */
    STBooleanEl createSTBooleanEl(String idOrExpression);
    
    /**
     * 载入一条STNumberEl, 参数可以是elId或是一条STNumber表达式, 如：#{s.attributeAttack - t.attributeDefense}
     * @param idOrExpression
     * @return 
     */
    STNumberEl createSTNumberEl(String idOrExpression);
    
    /**
     * 载入一条LNumberEl, 参数可以是elId或是一条LNumber表达式，如: #{l * 2}
     * @param idOrExpression
     * @return 
     */
    LNumberEl createLNumberEl(String idOrExpression);
    
}
