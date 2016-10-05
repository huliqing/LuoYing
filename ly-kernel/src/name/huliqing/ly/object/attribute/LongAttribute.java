/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.attribute;

import name.huliqing.ly.data.AttributeData;

/**
 * @author huliqing
 */
public class LongAttribute extends NumberAttribute {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsLong("value", 0);
    }    
    
    @Override
    protected void updateData() {
        // 这里一定要确保存的是long类型
        data.setAttribute("value", value.longValue());
    }

    @Override
    public void setValue(Number value) {
        // 转成long类型。
        super.setValue(value.longValue());
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
