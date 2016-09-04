/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import name.huliqing.core.data.AttributeData;

/**
 *
 * @author huliqing
 */
public class FloatAttribute extends NumberAttribute<Float> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsFloat("value", 0);
    }

    @Override
    public void setValue(Number newValue) {
        super.setValue(newValue.floatValue());
    }
    
    @Override
    protected void notifyValueChangeListeners(Number oldValue, Number newValue) {
//        if (oldValue.compareTo(newValue) != 0) {
//            super.notifyValueChangeListeners(oldValue, newValue); 
//        }
        if (oldValue.floatValue() != newValue.floatValue()) {
            super.notifyValueChangeListeners(oldValue, newValue); 
        }
    }
    
    @Override
    public void add(final int other) {
        setValue(value.floatValue() + other);
    }

    @Override
    public void add(final float other) {
        setValue(value.floatValue() + other);
    }

    @Override
    public void subtract(final int other) {
        setValue(value.floatValue() - other);
    }

    @Override
    public void subtract(final float other) {
        setValue(value.floatValue() - other);
    }
}
