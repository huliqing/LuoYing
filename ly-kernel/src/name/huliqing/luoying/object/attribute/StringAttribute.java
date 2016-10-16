/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;

/**
 * 字符串类型的属性
 * @author huliqing
 */
public class StringAttribute extends AbstractSimpleAttribute<String> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsString("value");
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("value", value);
    }
    
    @Override
    protected void notifyValueChangeListeners(String oldValue, String newValue) {
        if (!oldValue.equals(newValue)) {
            updateDatas();
            super.notifyValueChangeListeners(oldValue, newValue); 
        }
    }
    
    @Override
    public boolean match(final Object other) {
        if (other instanceof Number) {
            return value.equals(other.toString());
        }
        return value.equals(other);
    }
    
    
}
