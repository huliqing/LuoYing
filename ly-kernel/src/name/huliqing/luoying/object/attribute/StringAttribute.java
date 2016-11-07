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
public class StringAttribute extends SimpleAttribute<String> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data); 
        value = data.getAsString(ATTR_VALUE, "");
    }
    
    /**
     * 设置字符串的值
     * @param newValue 不能为null.
     * @return 
     */
    @Override
    protected boolean doSetSimpleValue(String newValue) {
        if (newValue.equals(value)) {
            return false;
        }
        value = newValue;
        return true;
    }
    
}
