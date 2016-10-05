/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.attribute;

import java.util.logging.Logger;
import name.huliqing.ly.data.AttributeData;

/**
 * 使用float类型作为属性参数
 * @author huliqing
 */
public class FloatAttribute extends NumberAttribute {
    private static final Logger LOG = Logger.getLogger(FloatAttribute.class.getName());

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsFloat("value", 0);
    }

    @Override
    protected void updateData() {
        // 这里一定要转成float类型,否则下次从存档载入的时候data.getAsFloat("value", 0)获得的就不一定是Float,
        // 因为value是抽象的Number类型。
        data.setAttribute("value", value.floatValue());
    }
    
    @Override
    public void setValue(Number value) {
        // 转成float类型。
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
