/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 * 字符串类型的属性
 * @author huliqing
 */
public class StringAttribute extends AbstractAttribute<String, AttributeData>{

    private String value;

    @Override
    public void setData(AttributeData data) {
        value = data.getAsString("value");
        super.setData(data); 
    }
    
    // 更新data值，以避免在外部使用data时获取不到实时的数据
    protected void updateData() {
        data.setAttribute("value", value);
    }
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        updateData();
        if (!oldValue.equals(this.value)) {
            notifyValueChangeListeners(oldValue, this.value);
        }
    }

    @Override
    public boolean match(Attribute other) {
        return value.equals(other.getValue().toString());
    }
    
    
}
