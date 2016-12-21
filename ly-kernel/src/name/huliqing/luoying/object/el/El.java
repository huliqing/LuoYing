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
package name.huliqing.luoying.object.el;

import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.luoying.xml.DataProcessor;

/**
 * @author huliqing
 * @param <T>
 */
public interface El<T> extends DataProcessor<ObjectData> {
    
    /**
     * 获取表达式字符串形式
     * @return 
     */
    String getExpression();
    
    /**
     * 设置表达式,示例如：#{x+y}
     * @param elExpression 
     */
    void setExpression(String elExpression);
    
    /**
     * 获取表达式的值。
     * @return 
     */
    T getValue();
}
