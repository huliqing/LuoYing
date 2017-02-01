/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class EntitySelectObj<T extends Entity> extends ControlTile<T> {
     
    protected final List<EntitySelectObjListener> listeners = new ArrayList<EntitySelectObjListener>();
    
    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public void setTarget(T target) {
        super.setTarget(target); 
    }

    public synchronized void addListener(EntitySelectObjListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public synchronized boolean removeListener(EntitySelectObjListener listener) {
        return listeners.remove(listener);
    }
    
    /**
     * 通过属性值的改变, 子类可以直接调用这个方法来通知所有侦听器.
     * @param property
     * @param newValue 
     */
    protected void notifyPropertyChanged(String property, Object newValue) {
        listeners.forEach(t -> {
            t.onPropertyChanged(target.getData(), property, newValue);
        });
    }

    @Override
    protected void onChildUpdated(ControlTile childUpdated, Type type) {
        // ignore
    }
    
}
