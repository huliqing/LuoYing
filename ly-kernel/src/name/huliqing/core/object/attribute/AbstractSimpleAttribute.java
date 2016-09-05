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
 * @param <V>
 */
public abstract class AbstractSimpleAttribute<V> extends AbstractAttribute implements MatchAttribute {
    
    protected V value;
    protected List<ValueChangeListener> listeners;
    
    /**
     * 更新属性值到data中。
     */
    protected void updateData() {
        if (value != null) {
            data.setAttribute("value", value);
        }
    }
    
    public V getValue() {
        return value;
    }
    
    public void setValue(V newValue) {
        V oldValue = this.value;
        this.value = newValue;
        updateData();
        notifyValueChangeListeners(oldValue, this.value);
    }
    
    public void addListener(ValueChangeListener<V> listener) {
        if (listeners == null) {
            listeners = new ArrayList<ValueChangeListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

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
