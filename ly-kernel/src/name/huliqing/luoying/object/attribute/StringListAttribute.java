/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.data.AttributeData;

/**
 * @author huliqing
 */
public class StringListAttribute extends AbstractAttribute<List<String>> implements CollectionAttribute<String> {
    
    private List<CollectionChangeListener> changeListeners;
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
    
    @Override
    public final int size() {
        return value.size();
    }
    
    @Override
    public final boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public void add(String e) {
        if (!duplication && value.contains(e)) {
            return;
        }
        value.add(e);
        if (changeListeners != null) {
            for (int i = 0; i < changeListeners.size(); i++) {
                changeListeners.get(i).onAdded(e);
            }
        }
    }

    @Override
    public boolean remove(String e) {
        if (value.remove(e)) {
            if (changeListeners != null) {
                for (int i = 0; i < changeListeners.size(); i++) {
                    changeListeners.get(i).onRemoved(e);
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
        return Collections.unmodifiableList(value);
    }

    @Override
    public boolean contains(String element) {
        return value.contains(element);
    }

    @Override
    public void addListener(CollectionChangeListener listener) {
        if (changeListeners == null) {
            changeListeners = new ArrayList<CollectionChangeListener>(2);
        }
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }

    @Override
    public boolean removeListener(CollectionChangeListener listener) {
        return changeListeners != null && changeListeners.remove(listener);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String v : value) {
            sb.append(v);
        }
        return sb.toString();
    }


    
}
