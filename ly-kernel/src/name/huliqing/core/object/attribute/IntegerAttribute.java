/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 * @author huliqing
 */
public class IntegerAttribute extends NumberAttribute<Integer> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsInteger("value", 0);
    }
    
    @Override
    public void setValue(Number newValue) {
        super.setValue(newValue.intValue()); 
    }
    
    @Override
    protected void notifyValueChangeListeners(Number oldValue, Number newValue) {
        if (oldValue.intValue() != newValue.intValue()) {
            super.notifyValueChangeListeners(oldValue, newValue); 
        }
    }
    
    @Override
    public void add(final int other) {
        setValue(value.intValue() + other);
    }

    @Override
    public void add(final float other) {
        setValue((int)(value.intValue() + other));
    }

    @Override
    public void subtract(final int other) {
        setValue(value.intValue() - other);
    }

    @Override
    public void subtract(final float other) {
        setValue((int)(value.intValue() - other));
    }
    
}
