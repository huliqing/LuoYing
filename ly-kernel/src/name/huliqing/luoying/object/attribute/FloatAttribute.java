/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;


/**
 * 使用float类型作为属性参数
 * @author huliqing
 */
public class FloatAttribute extends NumberAttribute {
//    private static final Logger LOG = Logger.getLogger(FloatAttribute.class.getName());

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsFloat(ATTR_VALUE, 0f); // 注意："0f"确保返回的是float, 避免在使用默认值的时候变成int类型。
        assert value instanceof Float;
    }
    
    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (Float.compare(value.floatValue(), newValue.floatValue()) != 0) {
            value = newValue.floatValue(); // 一定要转化为float类型。
            return true;
        }
        return false;
    }
    
    @Override
    public void add(final int other) {
        setValue(value.floatValue() + other);
    }

    @Override
    public void add(final float other) {
        setValue(value.floatValue() + other);
    }

}
