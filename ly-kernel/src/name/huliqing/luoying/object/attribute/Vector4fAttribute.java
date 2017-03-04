/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying.object.attribute;

import com.jme3.math.Vector4f;
import name.huliqing.luoying.data.AttributeData;

/**
 * 定义带有4个矢量的属性类型，x,y,z,w
 * @author huliqing
 */
public class Vector4fAttribute extends AbstractAttribute<Vector4f> {

    private final Vector4f temp = new Vector4f();
    
    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        this.value = data.getAsVector4f(ATTR_VALUE);
        if (value == null) {
            value = new Vector4f();
        }
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute(ATTR_VALUE, value);
    }

    @Override
    protected boolean doSetValue(Vector4f newValue) {
        boolean changed = (Float.compare(value.x, newValue.x) != 0 
                || Float.compare(value.y, newValue.y) != 0
                || Float.compare(value.z, newValue.z) != 0 
                || Float.compare(value.w, newValue.w) != 0);
        value.set(newValue);
        return changed;
    }
    
    /**
     * 设置Vector4f的值。
     * @param x
     * @param y
     * @param z
     * @param w 
     */
    public void set(float x, float y, float z, float w) {
        temp.set(x, y, z, w);
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
    public void setW(float w) {
        boolean changed = Float.compare(value.w, w) != 0;
        value.setW(w);
        if (changed) {
            notifyValueChangeListeners();
        }
    }
}
