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
 * @author huliqing
 */
public class LongAttribute extends NumberAttribute {

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsLong(ATTR_VALUE, 0L); // 0L,确保无论如何返回的值都是Long类型
        assert value instanceof Long;
    }    

    @Override
    protected boolean doSetSimpleValue(Number newValue) {
        if (value.longValue() != newValue.longValue()) {
            value = newValue.longValue(); // 确保转化为long类型
            return true;
        }
        return false;
    }
    
    @Override
    public final void add(final int other) {
        setValue(value.longValue() + other);
    }

    @Override
    public final void add(final float other) {
        setValue((long)(value.longValue() + other));
    }

}
