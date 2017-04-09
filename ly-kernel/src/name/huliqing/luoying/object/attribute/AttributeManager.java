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

import java.util.List;

/**
 * 属性管理器,管理属性的增、删、改、查
 * @author huliqing
 */
public interface AttributeManager {
    
    /**
     * 通过属性名称获取指定的属性，如果属性不存在则返回null<br>
     * 注：返回值为泛型(Attribute)，如果使用了特定的属性类型来接收返回值，
     * 则当返回类型不能转换为指定类型时会导致转换异常。
     * 要避免转换类型异常可使用{@link #getAttribute(java.lang.String, java.lang.Class) } 
     * @param <T>
     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
     * @return 
     * @see #getAttribute(java.lang.String, java.lang.Class) 
     */
    <T extends Attribute> T getAttribute(String attrName);
    
    /**
     * 查找指定的属性，如果找不到或者指定的属性类型不匹配则返回null.
     * @param <T>
     * @param attrName 属性名称，<b>不</b>是属性ID(<b>Not</b>id)
     * @param type 如果不为null, 则找到的属性必须符合这个类型，否则返回null.
     * @return 
     * @see #getAttribute(java.lang.String) 
     */
    <T extends Attribute> T getAttribute(String attrName, Class<T> type);
    
    /**
     * 获取角色当前的所有属性，注：返回的列表只能只读，否则报错。
     * @return 
     */
    List<Attribute>getAttributes();
    
    /**
     * 添加新的属性，注：如果已经存在相同id或名称的属性，则旧的属性会被替换掉。
     * @param attribute 
     */
    void addAttribute(Attribute attribute);
    
    /**
     * 移除指定的属性。
     * @param attribute
     * @return 
     */
    boolean removeAttribute(Attribute attribute);
    
    /**
     * 添加属性侦听器
     * @param attributeListener 
     */
    void addListener(AttributeListener attributeListener);
    
    /**
     * 移除指定的属性侦听器
     * @param attributeListener
     * @return 
     */
    boolean removeListener(AttributeListener attributeListener);
    
}
