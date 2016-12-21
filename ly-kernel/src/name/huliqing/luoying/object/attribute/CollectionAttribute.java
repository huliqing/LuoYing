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

import java.util.Collection;

/**
 * @author huliqing
 * @param <E>
 */
public interface CollectionAttribute<E> {
    
    /**
     * 判断当前元素数量
     * @return 
     */
    int size();
    
    /**
     * 判断集合是否为空
     * @return 
     */
    boolean isEmpty();
    
    /**
     * 添加一个元素
     * @param e 
     */
    void add(E e);
    
    /**
     * 移除一个元素
     * @param e
     * @return 
     */
    boolean remove(E e);
    
    /**
     * 获取数据列表
     * @return 
     */
    Collection<E> values();
    
    boolean contains(E object);
    
    /**
     * 添加一个侦听器,用于监听元素的添加或删除
     * @param listener 
     */
    void addCollectionChangeListener(CollectionChangeListener listener);
    
    /**
     * 移除一个侦听器。
     * @param listener
     * @return 
     */
    boolean removeCollectionChangeListener(CollectionChangeListener listener);
    

    
}
