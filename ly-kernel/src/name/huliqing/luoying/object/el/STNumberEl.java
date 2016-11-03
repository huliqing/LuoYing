/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * STNumberEl,这个EL会计算并返回一个Number值，这个Number值定义了一个对象source对另一个对象target产生的作用值。
 * 可以用来表示计算技能的伤害值、BUFF增益值、
 * <br>表达式参数：
 * 1.s: 这个参数表示源对象。<br>
 * 2.t: 这个参数表示目标对象。<br>
 * 表达式示例：#{s.attributeAttack - t.attributeDefence}, 这可以用来表示s角色对t角色的技能伤害
 * 值。
 * @author huliqing
 */
public class STNumberEl extends AbstractEl<Number> {

    private final AttributeElContext elContext = new AttributeElContext();

    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源对象
     * @param source 
     * @return  
     */
    public STNumberEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标对象
     * @param target 
     * @return  
     */
    public STNumberEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }

    @Override
    public Number getValue() {
        return super.getValue(); 
    }
    
}
