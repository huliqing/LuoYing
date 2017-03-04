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
import name.huliqing.luoying.data.AttributeData;

/**
 * @author huliqing 
 * @param <T> 
 */
public abstract class AbstractAttribute<T> implements Attribute<T> {
//    private static final Logger LOG = Logger.getLogger(SimpleAttribute.class.getName());
    
    /** return "value" */
    protected final static String ATTR_VALUE = "value";
    
    protected String name;
    protected AttributeData data;
    protected boolean initialized;
    protected T value;
    protected List<ValueChangeListener> listeners;
    
    @Override
    public AttributeData getData() {
        return data;
    }
    
    @Override
    public void setData(AttributeData data) {
        this.data = data;
        name = data.getAsString("name");
    }
    
//    @Override
//    public void updateDatas() {
//        data.setAttribute(ATTR_VALUE, value);
//    }

    @Override
    public void initialize(AttributeManager attributeManager) {
        if (initialized) {
            throw new IllegalStateException("Attribute already initialized! attributeId=" + this.getId());
        }
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
    
    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public T getValue() {
        return value;
    }
    
    @Override
    public final void setValue(T newValue) {
//        if (Config.debug) {
//            LOG.log(Level.INFO, "setValue:  attributeId={0}, attributeName={1}, setNewValue={2}"
//                    , new Object[] {getId(), getName(), newValue});
//        }
        // 调用子类设置属性值，属性值是否变化由子类自己确定,
        // 这里是无法确定值是否发生变化的，不同的子类实现会有不同的情况，特别是属性值为复合类型的属性。
        if (doSetValue(newValue)) {
            notifyValueChangeListeners();
        }
    }
    
    /**
     * 添加值变侦听器
     * @param listener 
     */
    @Override
    public void addListener(ValueChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<ValueChangeListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * 移除值变侦听器
     * @param listener
     * @return 
     */
    @Override
    public boolean removeListener(ValueChangeListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 通知属性值发生变化，这个方法会在setValue的时候根据属性值的变化而自动调用。
     * 子类也可以根据实际情况自行调用这个方法来提示属性值发生变动。例如：
     * 属性值为集合类型的属性中，如果某个元素值发生变化，也可以直接调用这个方法来通知侦听器。
     */
    protected final void notifyValueChangeListeners() {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onValueChanged(this);
            }
        }
    }
    
    /**
     * 设置当前属性的新值，这个方法由子类处理实现，如果设置新值后属性值<b>发生变化</b>则该方法应该返回true, 
     * 否则返回false。返回true是为了让属性知道发生了变化以便触发侦听器。对于属性值发生变化的情况可以包含如下：
     * 1.字符串值发生变化<br>
     * 2.数值发生变化<br>
     * 3.集合中元素的增、删<br>
     * 4.复合型数据中的元素变化，如: 属性值为Vector类型的元素中任何元素改变都应该认为是属性发生了变化。
     * 5.除此之外所有你觉得属性值发生了变化, 并且需要向外部告知的情况，该方法都应该返回true。
     * @param newValue
     * @return 
     */
    protected abstract boolean doSetValue(T newValue);
    
}
