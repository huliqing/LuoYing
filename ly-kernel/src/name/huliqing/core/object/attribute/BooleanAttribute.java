/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;
import name.huliqing.core.object.module.AttributeModule;

/**
 *
 * @author huliqing
 */
public class BooleanAttribute extends AbstractAttribute<Boolean, AttributeData>{

    private boolean value;

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsBoolean("value", value);
    }
    
    // 更新data值，以避免在外部使用data时获取不到实时的数据
    protected void updateData() {
        data.setAttribute("value", value);
    }
    
    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean newValue) {
        boolean old = this.value;
        this.value = newValue;
        updateData();
        if (old != this.value) {
            notifyValueChangeListeners(old, this.value);
        }
    }
    
    public boolean booleanValue() {
        return this.value;
    }

    /**
     * 判断与另一个属性是否匹配，首先：other必须是BooleanAttribute类型，否则这个方法始终返回false.<br>
     * 其次，如果两个BooleanAttribute的值都为true或false,则被认为相同的，只有一个为true，
     * 另一个为false时这个方法才会返回false.
     * @param other
     * @return 
     */
    @Override
    public boolean match(Object other) {
        if (other instanceof Boolean) {
            return this.booleanValue() == ((Boolean) other);
        }
        if (other instanceof Number) {
            return this.booleanValue() == convertNumber((Number) other);
        }
        if (other instanceof String) {
            return this.booleanValue() == convertString((String) other);
        }
        return false;
    }
    
    private boolean convertNumber(Number other) {
        return other.doubleValue() == 1;
    }
    
    private boolean convertString(String other) {
        return "1".equals(other) || "true".equals(other);
    }

}
