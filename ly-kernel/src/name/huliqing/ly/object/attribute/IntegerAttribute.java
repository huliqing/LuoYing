/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.attribute;

import name.huliqing.ly.data.AttributeData;

/**
 * 以慗形int作为属性参数。
 * @author huliqing
 */
public class IntegerAttribute extends NumberAttribute {

//    private static final Logger LOG = Logger.getLogger(IntegerAttribute.class.getName());
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsInteger("value", 0);
    }

    @Override
    protected void updateData() {
        // 这里一定要确保存的是int类型
        data.setAttribute("value", value.intValue()); 
    }

    @Override
    public void setValue(Number newValue) {
        // 转成int类型。
        super.setValue(newValue.intValue()); 
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
