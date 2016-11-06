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
   
    // remove20161029
//    @Override
//    public void updateDatas() {
//        super.updateDatas();
//        // 这里一定要确保存的是int类型
//        data.setAttribute("value", value.intValue()); 
//    }

    @Override
    public void setValue(Number newValue) {
        // 注意：这里一定要转成int类型,以确保保存在Data的时候是int类型。
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
