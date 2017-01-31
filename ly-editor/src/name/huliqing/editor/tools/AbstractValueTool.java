/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 * @param <V>
 */
public abstract class AbstractValueTool<V> extends EditTool implements ValueTool<V> {

    protected String label;
    protected V value;
    
    protected List<ValueChangedListener> listeners;

    public AbstractValueTool(String name, String tips, String icon) {
        super(name, tips, icon);
    }

    @Override
    public <T extends ValueTool> T setValue(V newValue) {
        if (value == null && newValue == null) 
            return (T) this;
        boolean changed = false;
        if (newValue != null && !newValue.equals(value)) {
            changed = true;
        } else if (value != null && !value.equals(newValue)) {
            changed = true;
        }
        V old = value;
        value = newValue;
        if (changed && listeners != null) {
            listeners.forEach(t -> {
                t.onValueChanged(this, old, value);
            });
        }
        return (T) this;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void addListener(ValueChangedListener listener) {
        if (listeners == null) {
            listeners = new ArrayList();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(ValueChangedListener listener) {
        return listeners != null && listeners.remove(listener);
    }

    
}
