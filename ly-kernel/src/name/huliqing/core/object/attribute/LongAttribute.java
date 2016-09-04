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
public class LongAttribute extends NumberAttribute<Long> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsLong("value", 0);
    }
    
    @Override
    protected void notifyValueChangeListeners(Number oldValue, Number newValue) {
        if (oldValue.longValue() != newValue.longValue()) {
            super.notifyValueChangeListeners(oldValue, newValue); 
        }
    }
    
    @Override
    public final void add(final int other) {
        setValue(value.longValue() + other);
    }

    @Override
    public final void add(final float other) {
        setValue((long)(value.longValue() + other));
    }

    @Override
    public final void subtract(final int other) {
        setValue(value.longValue() - other);
    }

    @Override
    public final void subtract(final float other) {
        setValue((long)(value.longValue() - other));
    }
    
}
