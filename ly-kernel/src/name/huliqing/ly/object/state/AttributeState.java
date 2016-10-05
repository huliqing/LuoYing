/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.state;

import name.huliqing.ly.Factory;
import name.huliqing.ly.data.StateData;
import name.huliqing.ly.layer.service.AttributeService;

/**
 * 可以改变角色属性数值的状态,可指定改变动态值或静态值，或者两者都
 * 改变，也可以在状态结束时恢复属性值。
 * @author huliqing
 */
public class AttributeState extends AbstractState {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    private String attributeName;
    private float value;
    // 是否在属性移除后恢复属性值
    private boolean restore;
    
    // 被修改的指定属性
    // 实际操作的属性值
    private float applyValue;
    // 标记属性是否已经作用到目标
    private boolean attributeApplied;

    @Override
    public void setData(StateData data) {
        super.setData(data); 
        this.attributeName = data.getAsString("attributeName");
        this.value = data.getAsFloat("value", value);
        this.restore = data.getAsBoolean("restore", restore);
        this.attributeApplied = data.getAsBoolean("attributeApplied", attributeApplied);
    }
    
    @Override
    protected void updateData() {
        super.updateData();
        data.setAttribute("attributeApplied", attributeApplied);
    }
    
    @Override
    public void initialize() {
        super.initialize();
        
        if (attributeApplied) {
            return;
        }
        
        // data.getResist()为抵抗率，取值 [0.0~1.0], 如果为1.0则说明完全抵抗. 
        applyValue = value * (1 - data.getResist());
        attributeService.addNumberAttributeValue(actor, attributeName, applyValue);
        attributeApplied = true;
        updateData();
    }
    
    @Override
    public void cleanup() {
        if (attributeApplied && restore) {
            attributeService.addNumberAttributeValue(actor, attributeName, -applyValue);
            attributeApplied = false;
            updateData();
        }
        super.cleanup();
    }

}
