/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractSimpleAttribute<T> extends AbstractAttribute implements MatchAttribute {
    
    protected T value;
    protected List<ValueChangeListener> listeners;
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T newValue) {
        T oldValue = this.value;
        this.value = newValue;
        notifyValueChangeListeners(oldValue, this.value);
    }
    
    public void addListener(ValueChangeListener<T> listener) {
        if (listeners == null) {
            listeners = new ArrayList<ValueChangeListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public boolean removeListener(ValueChangeListener<T> listener) {
        return listeners != null && listeners.remove(listener);
    }
    
    /**
     * 通知提示值变侦听器
     * @param oldValue
     * @param newValue 
     */
    protected void notifyValueChangeListeners(T oldValue, T newValue) {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onValueChanged(this, oldValue, newValue);
            }
        }
    }
    
    
}
