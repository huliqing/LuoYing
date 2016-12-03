/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.el;

import javax.el.ELContext;
import name.huliqing.luoying.object.attribute.AttributeManager;

/**
 * 技能输出值计算公式, 这个公式用于为技能计算一个最终的输出值。例如，当角色A使用技能B攻击角色C时，
 * 这个公式将为这样的攻击输出计算出一个输出值，用于作用到受攻击者身上(目标C).
 * 表达式参数：<br>
 * 1.s: 这个参数表示源对象,即攻击者。<br>
 * 2.t: 这个参数表示目标对象,即受攻击者。<br>
 * 3.skillHitValue: 技能的自身输出值，这是一个数值，来自于技能自身。<br>
 * @author huliqing
 */
public class SkillHitNumberEl extends AbstractEl<Number>{
    
    private final AttributeElContext elContext = new AttributeElContext(this);

    @Override
    protected ELContext getELContext() {
        return elContext;
    }
    
     /**
     * 设置源对象
     * @param source 
     * @return  
     */
    public SkillHitNumberEl setSource(AttributeManager source) {
        elContext.setAttributeManager("s", source);
        return this;
    }
    
    /**
     * 设置目标对象
     * @param target 
     * @return  
     */
    public SkillHitNumberEl setTarget(AttributeManager target) {
        elContext.setAttributeManager("t", target);
        return this;
    }
    
    /**
     * 设置技能自身的输出值。
     * @param skillValue
     * @return 
     */
    public SkillHitNumberEl setSkillValue(Number skillValue) {
        elContext.setBaseValue("skillHitValue", skillValue);
        return this;
    }

    /**
     * 计算出技能最终的输出值。
     * @return 
     */
    @Override
    public Number getValue() {
        return super.getValue(); 
    }
    
}
