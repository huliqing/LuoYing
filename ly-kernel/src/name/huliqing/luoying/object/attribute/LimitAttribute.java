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
 * LimitAttribute主要用于定义一些可以限制值大小的属性类型。
 * @author huliqing
 * @param <T>
 */
public interface LimitAttribute<T extends Number> {
    
    /**
     * 获取最大限制值
     * @return 
     */
    float getMaxLimit();
    
    /**
     * 设置为最高值
     */
    void setMax();
    
    /**
     * 获取最小限制值
     * @return 
     */
    float getMinLimit();
    
    /**
     * 设置为最小值
     */
    void setMin();
    
    /**
     * 获得当前值
     * @return 
     */
    Number getValue();
}
