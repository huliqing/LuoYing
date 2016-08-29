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
public class FloatAttribute extends NumberAttribute<Float, AttributeData> {

    private float value;

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsFloat("value", value);
    }
    
    // 更新data值，以避免在外部使用data时获取不到实时的数据
    protected void updateData() {
        data.setAttribute("value", value);
    }

    @Override
    public final int intValue() {
        return (int) value;
    }

    @Override
    public final float floatValue() {
        return value;
    }

    @Override
    public final long longValue() {
        return (long) value;
    }

    @Override
    public final double doubleValue() {
        return value;
    }
    
    @Override
    public final Float getValue() {
        return value;
    }

    @Override
    public final void setValue(final Float value) {
        setAndNotify(value);
    }
    
    @Override
    public final void add(final int other) {
        setAndNotify(value + other);
    }

    @Override
    public final void add(final float other) {
        setAndNotify(value + other);
    }

    @Override
    public final void subtract(final int other) {
        setAndNotify(value - other);
    }

    @Override
    public final void subtract(final float other) {
        setAndNotify(value - other);
    }

    @Override
    public final boolean isEqualTo(final int other) {
        return value == other;
    }

    @Override
    public final boolean isEqualTo(final float other) {
        return Float.compare(value, other) == 0;
    }

    @Override
    public final boolean greaterThan(final int other) {
        return value > other;
    }

    @Override
    public final boolean greaterThan(final float other) {
        return value > other;
    }

    @Override
    public final boolean lessThan(final int other) {
        return value < other;
    }

    @Override
    public final boolean lessThan(final float other) {
        return value < other;
    }

    @Override
    public final boolean match(final Attribute other) {
        if (other instanceof NumberAttribute) {
            return NumberCompare.isEqualTo(value, (NumberAttribute) other);
        }
        return false;
    }
    
    /**
     * 设置当前属性的值，如果设置后属性值发生改变，则通知监听器(只有在值发生改变才通知监听器).
     * @param value 
     */
    protected void setAndNotify(float value) {
        float oldValue = this.value;
        this.value = value;
        updateData();
        if (Float.compare(oldValue, this.value) != 0) {
            notifyValueChangeListeners(oldValue, this.value);
        }
    }

}
