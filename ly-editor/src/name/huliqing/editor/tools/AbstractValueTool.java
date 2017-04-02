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
package name.huliqing.editor.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huliqing
 * @param <V>
 */
public abstract class AbstractValueTool<V> extends AbstractTool implements ValueTool<V> {

    protected String label;
    protected V value; 
    
    protected List<ValueChangedListener<V>> listeners;

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
    public synchronized void addValueChangeListener(ValueChangedListener<V>listener) {
        if (listeners == null) {
            listeners = new ArrayList();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public synchronized boolean removeValueChangeListener(ValueChangedListener<V> listener) {
        return listeners != null && listeners.remove(listener);
    }

    
}
