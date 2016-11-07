/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;


/**
 * @author huliqing
 */
public class LongAttribute extends NumberAttribute {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsLong(ATTR_VALUE, 0L); // 0L,确保无论如何返回的值都是Long类型
        assert value instanceof Long;
    }    

    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (value.longValue() != newValue.longValue()) {
            value = newValue.longValue(); // 确保转化为long类型
            return true;
        }
        return false;
    }
    
    @Override
    public final void add(final int other) {
        setValue(value.longValue() + other);
    }

    @Override
    public final void add(final float other) {
        setValue((long)(value.longValue() + other));
    }

}
