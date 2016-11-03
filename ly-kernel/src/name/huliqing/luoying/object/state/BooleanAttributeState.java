/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.state;

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.object.attribute.BooleanAttribute;

/**
 * 可以改变角色的Boolean属性的状态
 * @author huliqing
 */
public class BooleanAttributeState extends AbstractState {    
    private String bindAttribute;
    private boolean value;
    private boolean restore;
    
    // ---- 保存原始状态以便恢复
    private BooleanAttribute attribute;
    private boolean originValue;
    
    @Override
    public void setData(StateData data) {
        super.setData(data);
        this.bindAttribute = data.getAsString("bindAttribute");
        this.value = data.getAsBoolean("value", false);
        this.restore = data.getAsBoolean("restore", restore);
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("value", value);
        data.setAttribute("originValue", originValue);
    }

    @Override
    public void initialize() {
        super.initialize();
        attribute = actor.getAttributeManager().getAttribute(bindAttribute, BooleanAttribute.class);
        if (attribute != null) {
            originValue = attribute.getValue();
            attribute.setValue(value);
        }
    }

    @Override
    public void cleanup() {
        if (restore && attribute != null) {
            attribute.setValue(originValue);
        }
        super.cleanup();
    }
    
}
