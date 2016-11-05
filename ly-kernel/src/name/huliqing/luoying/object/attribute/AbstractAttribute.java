/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    /** return \"value\" */
    protected final static String ATTR_VALUE = "value";

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
    }
    
    @Override
    public void updateDatas() {
        data.setAttribute(ATTR_VALUE, value);
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return data.getName();
    }
    
    @Override
    public T getValue() {
        return value;
    }
    
    @Override
    public void setValue(T newValue) {
        T oldValue = this.value;
        this.value = newValue;
        notifyValueChangeListeners(oldValue, this.value);
    }
    
    /**
     * 添加值变侦听器
     * @param listener 
     */
    @Override
    public void addListener(ValueChangeListener<?> listener) {
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
    public boolean removeListener(ValueChangeListener<?> listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 通知提示值变侦听器
     * @param oldValue
     * @param newValue 
     */
    protected void notifyValueChangeListeners(T oldValue, T newValue) {
        if (listeners != null && oldValue != newValue) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onValueChanged(this, oldValue, newValue);
            }
        }
    }
    
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

}
