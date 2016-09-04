/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import name.huliqing.core.data.AttributeData;

/**
 * @author huliqing
 */
public class StringListAttribute extends AbstractAttribute implements CollectionAttribute<String> {
    
    private List<String> values;
    private List<CollectionChangeListener> listeners;
    // 标记是否允许添加重复元素,默认不可以。
    private boolean duplication;

    @Override
    public void setData(AttributeData data) {
        super.setData(data);
        values = data.getAsStringList("values");
        if (values == null) {
            values = new ArrayList<String>();
        }
        duplication = data.getAsBoolean("duplication", duplication);
    }
    
    protected void updateData() {
        data.setAttribute("values", values);
    }
    
    @Override
    public final int size() {
        return values.size();
    }
    
    @Override
    public final boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public void add(String e) {
        if (!duplication && values.contains(e)) {
            return;
        }
        values.add(e);
        updateData();
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onAdded(e);
            }
        }
    }

    @Override
    public boolean remove(String e) {
        if (values.remove(e)) {
            updateData();
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onRemoved(e);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取整个集合，注意返回集合不能直接修改，只能只读。
     * @return 
     */
    @Override
    public Collection values() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public boolean contains(String object) {
        return values.contains(object);
    }

    @Override
    public void addListener(CollectionChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<CollectionChangeListener>(2);
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(CollectionChangeListener listener) {
        return listeners != null && listeners.remove(listener);
    }
    
}
