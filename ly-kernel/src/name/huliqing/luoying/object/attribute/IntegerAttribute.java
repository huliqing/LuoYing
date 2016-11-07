/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;


/**
 * 以慗形int作为属性参数。
 * @author huliqing
 */
public class IntegerAttribute extends NumberAttribute {
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsInteger(ATTR_VALUE, 0);
        assert value instanceof Integer;
    }
    
    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (value.intValue() != newValue.intValue()) {
            value = newValue.intValue(); // 注意：这里必须，一定要转化为int类型。
            return true;
        }
        return false;
    }
    
    @Override
    public void add(final int other) {
        setValue(value.intValue() + other);
    }

    @Override
    public void add(final float other) {
        setValue(value.intValue() + other);
    }
    
}
