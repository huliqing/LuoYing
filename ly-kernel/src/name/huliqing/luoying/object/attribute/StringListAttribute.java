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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.data.AttributeData;

/**
 * 字符串列表类型的属性.
 * @author huliqing
 */
public class StringListAttribute extends AbstractAttribute<List<String>> implements CollectionAttribute<String> {
    
    private List<CollectionChangeListener> collectionChangeListeners;
    // 标记是否允许添加重复元素,默认不可以。
    private boolean duplication;

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        value = data.getAsStringList(ATTR_VALUE);
        if (value == null) {
            value = new ArrayList<String>();
        }
        duplication = data.getAsBoolean("duplication", duplication);
    }
    
    /**
     * 设置集合，注意：新设置的集合的值将直接作为属性的新值，直接引用，并且不管集合中的元素是否发生变化。
     * 每次调用这个方法都将直接触发值变侦听器。
     * @param newValue
     * @return 
     */
    @Override
    protected boolean doSetValue(List<String> newValue) {
        this.value = newValue;
        // 对于集合来说，很难去确定集合中的元素是否变动，所以一律返回true,
        // 只要调用setValue, 都认为属性发生了变化。
        return true;
    }
    
    @Override
    public final int size() {
        return value.size();
    }
    
    @Override
    public final boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * 向集合中添加元素，如果指定的元素已经存在于集合中，则该方法将什么也不会做。否则将元素添加到集合中，
     * 并触发值变侦听器及集合变化侦听器。
     * @param e 
     */
    @Override
    public void add(String e) {
        if (!duplication && value.contains(e)) {
            return;
        }
        value.add(e);
        // 集合元素变化侦听
        notifyCollectionItemAddedListeners(e);
        // 通知值变侦听器
        notifyValueChangeListeners();
    }

    /**
     * 从集合中移除元素，如果指定的元素不存在于集合中，则该方法将什么也不会做。否则将元素从集合中移除，
     * 并触发值变侦听器及集合变化侦听器。
     * @param e
     * @return 
     */
    @Override
    public boolean remove(String e) {
        if (value.remove(e)) {
            // 集合元素变化侦听
            notifyCollectionItemRemoveListeners(e);
            // 通知值变侦听器
            notifyValueChangeListeners();
            return true;
        }
        return false;
    }

    /**
     * 获取整个集合，注意返回集合不能直接修改，只能作为只读使用，偿试直接修改集合中的元素将会报错。
     * @return 
     * @see #add(java.lang.String) 
     * @see #remove(java.lang.String) 
     */
    @Override
    public Collection values() {
        return Collections.unmodifiableList(value);
    }

    @Override
    public boolean contains(String element) {
        return value.contains(element);
    }

    @Override
    public void addCollectionChangeListener(CollectionChangeListener listener) {
        if (collectionChangeListeners == null) {
            collectionChangeListeners = new ArrayList<CollectionChangeListener>(2);
        }
        if (!collectionChangeListeners.contains(listener)) {
            collectionChangeListeners.add(listener);
        }
    }

    @Override
    public boolean removeCollectionChangeListener(CollectionChangeListener listener) {
        return collectionChangeListeners != null && collectionChangeListeners.remove(listener);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String v : value) {
            sb.append(v);
        }
        return sb.toString();
    }
    
    private void notifyCollectionItemAddedListeners(String itemAdded) {
        if (collectionChangeListeners != null) {
            for (int i = 0; i < collectionChangeListeners.size(); i++) {
                collectionChangeListeners.get(i).onAdded(itemAdded);
            }
        }
    }

    private void notifyCollectionItemRemoveListeners(String itemRemoved) {
        if (collectionChangeListeners != null) {
            for (int i = 0; i < collectionChangeListeners.size(); i++) {
                collectionChangeListeners.get(i).onRemoved(itemRemoved);
            }
        }
    }
    
}
