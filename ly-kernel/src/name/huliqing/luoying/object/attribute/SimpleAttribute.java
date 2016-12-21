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
import java.util.List;

/**
 * 普通属性类型的基类，这种类型是相对于使用复合类型为属性值的属性而言的，SimpleAttribute主要使用于Number,String
 * 作为属性值类型的属性，这种属性可以方便通过添加基本值的侦听器(SimpleValueChangeListener).
 * 来监听属性值的前后变化情况，以便知道属性在变化之前的值是怎么样的。
 * @author huliqing
 * @param <T>
 */
public abstract class SimpleAttribute<T> extends AbstractAttribute<T>{

    /** 基本类型的值变侦听器 */
    protected List<SimpleValueChangeListener> simpleListeners; 

    @Override
    protected final boolean doSetValue(T newValue) {
        T oldValue = value;
        if (doSetSimpleValue(newValue)) {
            notifySimpleValueChangeListeners(oldValue);
            return true;
        }
        return false;
    }
    
     /**
     * 添加值变侦听器
     * @param listener 
     */
    public void addSimpleValueChangeListener(SimpleValueChangeListener<?> listener) {
        if (simpleListeners == null) {
            simpleListeners = new ArrayList<SimpleValueChangeListener>();
        }
        if (!simpleListeners.contains(listener)) {
            simpleListeners.add(listener);
        }
    }
    
    /**
     * 移除值变侦听器
     * @param listener
     * @return 
     */
    public boolean removeSimpleValueChangeListener(SimpleValueChangeListener<?> listener) {
        return simpleListeners != null && simpleListeners.remove(listener);
    }
    
    /**
     * 通知所有侦听器，基本类型属性的值发生变化, 这个方法可以由子类直接调用。
     * 当子类在自定义的属性内发生值变化时可以调用这个方法来确发侦听器。
     * @param oldValue 
     */
    protected final void notifySimpleValueChangeListeners(T oldValue) {
        if (simpleListeners != null) {
            for (int i = 0; i < simpleListeners.size(); i++) {
                simpleListeners.get(i).onSimpleValueChanged(this, oldValue);
            }
        }
    }
    
    /**
     * 设置当前属性的新值，这个方法由子类处理实现，如果设置新值后属性值<b>发生变化</b>则该方法应该返回true, 
     * 否则返回false。返回true是为了让属性知道发生了变化以便触发侦听器。对于属性值发生变化的情况可以包含如下：<br>
     * 1.数值的变化.<br>
     * 2.字符串值的变化。<br>
     * @param newValue
     * @return 
     */
    protected abstract boolean doSetSimpleValue(T newValue);
}
