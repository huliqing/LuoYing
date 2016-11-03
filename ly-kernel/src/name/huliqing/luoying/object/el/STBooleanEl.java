/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * STBooleanEl，这个EL的结果为一个Boolean类型的值，通过这个值来判断source是否可以作用于target
 * ,一般用于判断两个拥有属性功能（如Entity)的对象是否可以互相作用,当求值时需要设置两个基本值source, target<br>
 * 使用示例，比如当用于判断一个角色A的技能是否可以作用于另一个角色B时, <br>
 * 在xml中的表达式可以像这样：#{s.attributeGroup != t.attributeGroup}<br>
 * 这表示当角色A(source)与目标角色B(target)的派系（group属性值）不一样时，角色A的技能可以作用于角色B。<br>
 * 
 * 代码使用示例：
 * <code><pre>
 * HitCheckEl el = elService.createHitCheckEl(...);
 * el.setSource(source.getAttributeManager());
 * el.setTarget(target.getAttributeManager());
 * if (el.getValue()) {
 *      ...doSomething.
 * }
 * </pre></code>
 * @author huliqing
 */
public class STBooleanEl extends AbstractEl<Boolean> {

    private final AttributeElContext elContext = new AttributeElContext();
    
    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源属性管理器，比如某个Entity的属性管理器。
     * @param source 
     * @return  
     */
    public STBooleanEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标属性管理器，比如某个Entity的属性管理器
     * @param target 
     * @return  
     */
    public STBooleanEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }
    
}
