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

import com.jme3.math.ColorRGBA;
import name.huliqing.luoying.data.AttributeData;

/**
 * 颜色类型的属性
 * @author huliqing
 */
public class ColorAttribute extends AbstractAttribute<ColorRGBA> {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsColor(ATTR_VALUE);
        if (value == null) {
            value = new ColorRGBA();
        }
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute(ATTR_VALUE, value);
    }

    @Override
    protected boolean doSetValue(ColorRGBA newValue) {
        boolean changed = (Float.compare(value.r, newValue.r) != 0 
                || Float.compare(value.g, newValue.g) != 0
                || Float.compare(value.b, newValue.b) != 0 
                || Float.compare(value.a, newValue.a) != 0);
        value.set(newValue);
        return changed;
    }

}
