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
