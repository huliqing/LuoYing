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
package name.huliqing.luoying.object.entity;

import name.huliqing.luoying.xml.ObjectData;

/**
 * EntityDataListener用于监听实体的数据变化，当数据流转入实体时，如添加、移除、使用数据时都会触发数据侦听器。
 * 外部应用可以通过给实体添加EntityDataListener来监听数据的变化。
 * @author huliqing
 */
public interface DataListener {
    
    /**
     * 当实体添加了数据时该方法被调用。
     * @param data
     * @param amount
     */
    void onDataAdded(ObjectData data, int amount);
    
    /**
     * 当实体移除了数据时该方法被调用。
     * @param data
     * @param amount 
     */
    void onDataRemoved(ObjectData data, int amount);
    
    /**
     * 当实体使用了一个数据时该方法被调用。
     * @param data 
     */
    void onDataUsed(ObjectData data);
}
