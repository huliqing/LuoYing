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
package name.huliqing.editor.tools;

/**
 * 输入值的工具
 * @author huliqing
 * @param <V>
 */
public interface ValueTool<V> extends Tool {
    
    /**
     * 设置值，并返回当前实例
     * @param <T>
     * @param value
     * @return 
     */
    <T extends ValueTool> T setValue(V value);
    
    V getValue();
    
    void addValueChangeListener(ValueChangedListener<V> listener);
    
    boolean removeValueChangeListener(ValueChangedListener<V> listener);
}
