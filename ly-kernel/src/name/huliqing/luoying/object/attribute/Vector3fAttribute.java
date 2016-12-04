/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.AttributeData;

/**
 * 定义带有4个矢量的属性类型，x,y,z,w
 * @author huliqing
 */
public class Vector3fAttribute extends AbstractAttribute<Vector3f> {

    private final Vector3f temp = new Vector3f();
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        this.value = data.getAsVector3f(ATTR_VALUE);
        if (value == null) {
            value = new Vector3f();
        }
    }
    
    @Override
    protected boolean doSetValue(Vector3f newValue) {
        boolean changed = (Float.compare(value.x, newValue.x) != 0 
                || Float.compare(value.y, newValue.y) != 0
                || Float.compare(value.z, newValue.z) != 0);
        value.set(newValue);
        return changed;
    }
    
    /**
     * 设置Vector4f的值。
     * @param x
     * @param y
     * @param z
     */
    public void set(float x, float y, float z) {
        temp.set(x, y, z);
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
    public void setZ(float z) {
        boolean changed = Float.compare(value.z, z) != 0;
        value.setZ(z);
        if (changed) {
            notifyValueChangeListeners();
        }
    }
}
