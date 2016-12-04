/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import com.jme3.math.Vector2f;
import name.huliqing.luoying.data.AttributeData;

/**
 * 定义带有4个矢量的属性类型，x,y,z,w
 * @author huliqing
 */
public class Vector2fAttribute extends AbstractAttribute<Vector2f> {

    private final Vector2f temp = new Vector2f();
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        this.value = data.getAsVector2f(ATTR_VALUE);
        if (value == null) {
            value = new Vector2f();
        }
    }
    
    @Override
    protected boolean doSetValue(Vector2f newValue) {
        boolean changed = (Float.compare(value.x, newValue.x) != 0 || Float.compare(value.y, newValue.y) != 0);
        value.set(newValue);
        return changed;
    }
    
    /**
     * 设置Vector4f的值。
     * @param x
     * @param y
     */
    public void set(float x, float y) {
        temp.set(x, y);
        setValue(temp);
    }
    public void setX(float x) {
        boolean changed = Float.compare(value.x, x) != 0;
        value.setX(x);
        if (changed) {
            notifyValueChangeListeners();
        }
    }
    public void setY(float y) {
        boolean changed = Float.compare(value.y, y) != 0;
        value.setY(y);
        if (changed) {
            notifyValueChangeListeners();
        }
    }
}
