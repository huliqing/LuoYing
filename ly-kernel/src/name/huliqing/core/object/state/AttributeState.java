/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.attribute.NumberAttribute;

/**
 * 可以改变角色属性数值的状态,可指定改变动态值或静态值，或者两者都
 * 改变，也可以在状态结束时恢复属性值。
 * @author huliqing
 */
public class AttributeState extends State {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    private String attributeName;
    private float value;
    // 是否在属性移除后恢复属性值
    private boolean restore;
    
    // 被修改的指定属性
    private NumberAttribute attribute;
    // 实际操作的属性值
    private float applyValue;

    @Override
    public void setData(StateData data) {
        super.setData(data); 
        this.attributeName = data.getAsString("attributeName");
        this.value = data.getAsFloat("value", value);
        this.restore = data.getAsBoolean("restore", restore);
    }

    @Override
    public void initialize() {
        super.initialize();
        
        // data.getResist()为抵抗率，取值 [0.0~1.0], 如果为1.0则说明完全抵抗. 
        applyValue = value * (1 - data.getResist());
        attribute = attributeService.getAttributeByName(actor, attributeName);
        if (attribute != null) {
            attribute.add(applyValue);
        }
    }
    
    @Override
    public void cleanup() {
        if (attribute != null && restore) {
            attribute.subtract(applyValue);
            // 注意：这里恢复attribute的值之后要清理引用，以避免重复调用cleanup的时候又调用了subtract(applyValue)
            attribute = null;
        }
        super.cleanup();
    }

}
