/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.controls.entity;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.SimpleJmeEdit;
import name.huliqing.editor.edit.controls.ControlTile;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class EntityControlTile<T extends Entity> extends ControlTile<T, SimpleJmeEdit> {
     
    protected final List<EntityControlTileListener> listeners = new ArrayList<EntityControlTileListener>();
    
    @Override
    public T getTarget() {
        return target;
    }

    @Override
    public void setTarget(T target) {
        super.setTarget(target); 
    }

    @Override
    public void initialize(SimpleJmeEdit edit) {
        super.initialize(edit);
    }

    @Override
    public void cleanup() {
        super.cleanup(); 
    }
    
    /**
     * 重新载入实体
     * @param scene
     */
    public void reloadEntity(Scene scene) {
        // 清理
        if (target.isInitialized()) {
            target.cleanup();
        }
        // 重新载入实体,重新设置Data,必须的
        target.setData(target.getData());
        target.initialize();
        target.onInitScene(scene);
        updateState();
    }

    public synchronized void addListener(EntityControlTileListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public synchronized boolean removeListener(EntityControlTileListener listener) {
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
