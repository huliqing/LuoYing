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
        value = data.getAsFloat(ATTR_VALUE, 0);
    }
   
    // remove20161029
//    @Override
//    public void updateDatas() {
//        super.updateDatas();
//        // 这里一定要转成float类型,否则下次从存档载入的时候data.getAsFloat("value", 0)获得的就不一定是Float,
//        // 因为value是抽象的Number类型。
//        data.setAttribute("value", value.floatValue());
//    }
    
    @Override
    public void setValue(Number value) {
        // 注意：这里一定要转成float类型,以确保保存在Data的时候是Float类型。
        super.setValue(value.floatValue());
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
