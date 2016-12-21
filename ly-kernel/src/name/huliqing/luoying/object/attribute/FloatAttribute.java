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
        value = data.getAsFloat(ATTR_VALUE, 0f); // 注意："0f"确保返回的是float, 避免在使用默认值的时候变成int类型。
        assert value instanceof Float;
    }
    
    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (Float.compare(value.floatValue(), newValue.floatValue()) != 0) {
            value = newValue.floatValue(); // 一定要转化为float类型。
            return true;
        }
        return false;
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
