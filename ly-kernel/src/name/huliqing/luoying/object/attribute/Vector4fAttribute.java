/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import com.jme3.math.Vector4f;
import name.huliqing.luoying.data.AttributeData;

/**
 * 定义带有4个矢量的属性类型，x,y,z,w
 * @author huliqing
 */
public class Vector4fAttribute extends AbstractAttribute<Vector4f> {

    private final Vector4f oldValue = new Vector4f();
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        this.value = data.getAsVector4f(ATTR_VALUE);
        if (value == null) {
            value = new Vector4f();
        }
    }

    @Override
    public void setValue(Vector4f newValue) {
        oldValue.set(value);
        value.set(newValue);
        notifyValueChangeListeners(oldValue, value);
    }
    
    public void set(float x, float y, float z, float w) {
        oldValue.set(value);
        value.set(x, y, z, w);
        notifyValueChangeListeners(oldValue, value);
    }
}
