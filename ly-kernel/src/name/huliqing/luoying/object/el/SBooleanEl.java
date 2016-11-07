/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * SBooleanEl, 这个EL返回一个Boolean值，如果值为true，则说明可以使用一个目标对象,否则不能。
 * 这个EL可以用于判断一个Entity是否可以使用一件装备、一件武器、一个技能或一件物品...等。
 * 比如, 当给一件装备配置如下表达式时：<br>
 * CheckEL="#{!s.attributeDead && s.attributeLevel >= 5}"<br>
 * 这可以表示为：只有活着、并且等级大于或等于5的角色才可以使用目标装备.
 * 
 *  * 代码使用示例：
 * <code><pre>
 * CheckEl el = elService.createCheckEl(...);
 * el.setSource(source.getAttributeManager());
 * if (el.getValue()) {
 *      ...doSomething.
 * }
 * </pre></code>
 * @author huliqing
 */
public class SBooleanEl extends AbstractEl<Boolean>{

    private final AttributeElContext elContext = new AttributeElContext(this);

    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
    /**
     * 设置源对象的属性管理器, 使用时表达式格式如：#{source.attributeXXX}
     * @param source 
     * @return  
     */
    public SBooleanEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
}
