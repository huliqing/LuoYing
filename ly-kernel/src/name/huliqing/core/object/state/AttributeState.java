/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.state;

import com.jme3.app.Application;
import name.huliqing.core.Factory;
import name.huliqing.core.data.StateData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.utils.ConvertUtils;

/**
 * 可以改变角色属性数值的状态,可指定改变动态值或静态值，或者两者都
 * 改变，也可以在状态结束时恢复属性值。
 * @author huliqing
 */
public class AttributeState extends State {
    private final AttributeService attributeService = Factory.get(AttributeService.class);
    
    // 影响的属性ID
    private AttributeWrap[] attributes;
    
    private boolean dynamicApply;
    private boolean dynamicRestore;
    private boolean staticApply;
    private boolean staticRestore;
    
    // ---- 内部
    // 削弱作用
    private float resist;
    
    // 是否需要清理属性变更，注意：AttributeState状态只要修改了属性，就必须在最
    // 后进行clean恢复. 比如：initialize的时候对速度属性增加了
    // 0.2,那么在cleanup的时候就必须把速度降0.2，但必须防止多次调用cleanup产生的
    // 数据不对应，即要防止如调用了两次cleanup时速度被降了0.4.
    protected boolean needClean;

    @Override
    public void setData(StateData data) {
        super.setData(data); 
        // attributes
        String[] attributesArr = data.getAsArray("attributes");
        attributes = new AttributeWrap[attributesArr.length];
        for (int i = 0; i < attributesArr.length; i++) {
            String[] attr = attributesArr[i].split("\\|");
            AttributeWrap aw = new AttributeWrap(attr[0], ConvertUtils.toFloat(attr[1], 0));
            attributes[i] = aw;
        }
        
        dynamicApply = data.getAsBoolean("dynamicApply", false);
        dynamicRestore = data.getAsBoolean("dynamicRestore", false);
        staticApply = data.getAsBoolean("staticApply", false);
        staticRestore = data.getAsBoolean("staticRestore", false);
        
        // 抵抗率，[0.0~1.0)
        resist = data.getResist();
    }

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        
        float factor = 1 - resist;
        for (AttributeWrap aw : attributes) {
            if (staticApply) {
                attributeService.applyStaticValue(actor, aw.attribute, aw.amount * factor);
            }
            if (dynamicApply) {
                attributeService.applyDynamicValue(actor, aw.attribute, aw.amount * factor);
                attributeService.clampDynamicValue(actor, aw.attribute);
            }
        }
        
        needClean = true;
    }
    
    @Override
    public void cleanup() {
        if (needClean) {
            float factor = resist - 1;
            if (staticApply && staticRestore) {
                for (AttributeWrap aw : attributes) {
                    attributeService.applyStaticValue(actor, aw.attribute, aw.amount * factor);
                }
            }
            if (dynamicApply && dynamicRestore) {
                for (AttributeWrap aw : attributes) {
                    attributeService.applyDynamicValue(actor, aw.attribute, aw.amount * factor);
                    // 限制动态值超出范围
                    attributeService.clampDynamicValue(actor, aw.attribute);
                }
            }
            needClean = false;
        }
        super.cleanup();
    }
    
    private class AttributeWrap {
        String attribute;
        // 属性的添加量
        float amount;
        
        public AttributeWrap(String attribute, float amount) {
            this.attribute = attribute;
            this.amount = amount;
        }
    }
}
