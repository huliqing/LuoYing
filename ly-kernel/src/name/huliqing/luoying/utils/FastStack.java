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
package name.huliqing.luoying.utils;

import java.util.Arrays;

/**
 * 快速高效的堆栈。
 * @author huliqing
 * @param <T>
 */
public final class FastStack<T> {
    
    private Object[] elements;
    private int index = -1;
    
    public FastStack(int initialCapacity) {
        elements = new Object[initialCapacity];
    }
    
    public void push(T o) {
        index++;
        if (index >= elements.length) {
            elements = Arrays.copyOf(elements, elements.length + 1);
        }
        elements[index] = o;
    }
    
    public T pop() {
        T object = (T) elements[index];
        elements[index] = null;
        index--;
        return object;
    }
    
    public T get(int index) {
        return (T) elements[index];
    }
    
    public int size() {
        return index + 1;
    }
}
