/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el2;

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * HitCheckEL，这个EL的结果为一个Boolean类型的值，通过这个值来判断source是否可以作用于target,
 * 当求值时需要设置两个基本值source, target<br>
 * 使用示例，比如当用于判断一个角色A的技能是否可以作用于另一个角色B时, <br>
 * 在xml中的表达式可以像这样：#{source.attributeGroup != target.attributeGroup}<br>
 * 这表示当角色A(source)与目标角色B(target)的派系（group属性值）不一样时，角色A的技能可以作用于角色B。
 * @author huliqing
 */
public class HitCheckEL extends AbstractEL<Boolean> {

    private final AttributeELContext elContext = new AttributeELContext();
    
    public void setSource(AttributeManager sourceAM) {
        elContext.setAttributeManager("source", sourceAM);
    }
    
    public void setTarget(AttributeManager targetAM) {
        elContext.setAttributeManager("target", targetAM);
    }

    @Override
    public ELContext getELContext() {
        return elContext;
    }
    
}
