/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import name.huliqing.luoying.data.AttributeData;

/**
 * 字符串类型的属性
 * @author huliqing
 */
public class StringAttribute extends AbstractAttribute<String> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsString("value");
    }
   
    @Override
    public void setValue(String newValue) {
        // 同对象，同null, 必要的，当值相同时避免触发值变侦听器
        if (value == newValue) {
            return;
        }
        // 值相同
        if (value != null && value.equals(newValue)) {
            return;
        }
        super.setValue(newValue);
    }
    
//    @Override
//    public boolean match(final Object other) {
//        if (other instanceof Number) {
//            return value.equals(other.toString());
//        }
//        return value.equals(other);
//    }


    
    
}
