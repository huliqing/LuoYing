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


/**
 * Number类型的属性。
 * @author huliqing
 */
public abstract class NumberAttribute extends SimpleAttribute<Number> {

    public byte byteValue() {
        return value.byteValue();
    }
    
    public short shortValue() {
        return value.shortValue();
    }
    
    public int intValue() {
        return value.intValue();
    }
    
    public float floatValue() {
        return value.floatValue();
    }
    
    public long longValue() {
        return value.longValue();
    }
    
    public double doubleValue() {
        return value.doubleValue();
    }
    
    public abstract void add(int other);
    public abstract void add(float other);
    


    
}
