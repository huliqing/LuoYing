/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.AttributeData;

/**
 *
 * @author huliqing
 * @param <V>
 * @param <T>
 */
public abstract class AbstractAttribute<V, T extends AttributeData> implements Attribute<V, T>{

    protected T data;
    protected boolean initialized;
    protected List<ValueChangeListener> listeners;
    
    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return data.getAsString("name");
    }
    
    @Override
    public void initialize(AttributeStore store) {
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
    public void addListener(ValueChangeListener<V> listener) {
        if (listeners == null) {
            listeners = new ArrayList<ValueChangeListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(ValueChangeListener<V> listener) {
        return listeners != null && listeners.remove(listener);
    }

    /**
     * 通知提示值变侦听器
     * @param oldValue
     * @param newValue 
     */
    protected void notifyValueChangeListeners(V oldValue, V newValue) {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onValueChanged(this, oldValue, newValue);
            }
        }
    }
}
