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
 * DataHandler是用来作为处理Entity数据的勾子, 当向Entity添加、删除、使用数据时会通过这些DataHandler的处理。
 * 实体模块（Entity Module）可以通过实现这个接口来处理流转到实体的数据。
 * 每一个不同的实体模块应该尽量明确、并且只处理自己需要的一种类型的数据。
 * @author huliqing
 * @param <T>
 */
public interface DataHandler<T extends ObjectData> {
    
    /**
     * 指定一个要处理的数据类型，当数据流转到实体内时只有属于这个类型的数据才会触发这个DataHandler来对数据进行处理。
     * 每个DataHandler都应该尽量明确只处理自己需要的类型的数据。
     * @return 
     */
    Class<T> getHandleType();
    
    /**
     * 处理Entity数据的添加，当外部向Entity添加数据时这个方法会被调用，
     * 实体模块（Entity Module）通过实现这个方法来处理外部进入的数据。
     * @param data
     * @param amount 
     * @return  返回true,如果成功添加
     */
    boolean handleDataAdd(T data, int amount);
    
    /**
     * 处理Entity数据的移除，当外部从Entity移除数据时这个方法会被调用，
     * 实体模块（Entity Module）通过实现这个方法来处理数据的移除。
     * @param data
     * @param amount 
     * @return 返回true ,如果移除成功。
     */
    boolean handleDataRemove(T data, int amount);
    
    /**
     * 处理Entity数据的使用，当Entity使用数据时这个方法会被调用。
     * 实体模块（Entity Module）通过实现这个方法来确定要如何使用指定的数据。
     * @param data 
     * @return 返回true,如果使用了物品。
     */
    boolean handleDataUse(T data);
}
