/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 可以改变角色属性数值的状态.
 * @author huliqing
 */
public class AttributeState extends AbstractState {
    private final EntityService entityService = Factory.get(EntityService.class);
    
    private String bindNumberAttribute;
    private float addValue;
    // 是否在属性移除后恢复属性值
    private boolean restore;
    
    // ---- inner
    // 被修改的指定属性
    // 实际操作的属性值
    private float actualAddValue;
    // 标记属性是否已经作用到目标
    private boolean attributeApplied;
    private Entity sourceActor;
    
    @Override
    public void setData(StateData data) {
        super.setData(data); 
        this.bindNumberAttribute = data.getAsString("bindNumberAttribute");
        this.addValue = data.getAsFloat("addValue", addValue);
        this.restore = data.getAsBoolean("restore", restore);
        this.attributeApplied = data.getAsBoolean("_attributeApplied", attributeApplied);
        this.actualAddValue = data.getAsFloat("_actualAddValue", 0);
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("_attributeApplied", attributeApplied);
        data.setAttribute("_actualAddValue", actualAddValue); 
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (attributeApplied) {
            return;
        }
        
        NumberAttribute attr = entity.getAttributeManager().getAttribute(bindNumberAttribute, NumberAttribute.class);
        if (attr == null) {
            return;
        }
        
        sourceActor = getSourceActor();
        
        // data.getResist()为抵抗率，取值 [0.0~1.0], 如果为1.0则说明完全抵抗. 
        float expectAddValue = addValue * (1 - data.getResist());
        
        // 记住旧值
        float oldValue = attr.floatValue();
        // HIT开始，expectAddValue为期望增加到属性上的值，但是最终能增加多少值是与属性类型有关系的，最终增加的值不
        // 一定与期望的一致，例如一些属性值可能会限制最大值或最小值, 所以hit后要看一下属性的当前值，计算出实际上增加了
        // 多少，这个实际增加的值（actualAddValue）很重要，因为可能需要在状态结束时还原这个值。
        entityService.hitNumberAttribute(entity, bindNumberAttribute, expectAddValue, sourceActor);
        // Hit后计算出实际增加到属性上的值,
        actualAddValue = attr.floatValue() - oldValue;
        
        attributeApplied = true;
    }
    
    @Override
    public void cleanup() {
        if (attributeApplied && restore) {
            entityService.hitNumberAttribute(entity, bindNumberAttribute, -actualAddValue, null); // 这里不再需要设置sourceActor
            attributeApplied = false;
        }
        super.cleanup();
    }

}
