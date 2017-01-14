/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.editor.edit.scene.SceneEdit;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class EntitySelectObj<T extends Entity> implements SelectObj<T> {
     
    protected T entity;
    
    protected final List<EntitySelectObjListener> listeners = new ArrayList<EntitySelectObjListener>();
    
    public EntitySelectObj() {}
    
    public EntitySelectObj(T entity) {
        this.entity = entity;
    }
    
    @Override
    public void setObject(T entity) {
        if (this.entity != null) {
            throw new IllegalStateException("Entity already set, could not change!, EntitySelectObj=" 
                    + this + ", entity=" + entity);
        }
        this.entity = entity;
    }

    @Override
    public T getObject() {
        return entity;
    }

    @Override
    public void setLocalTranslation(Vector3f location) {
        entity.getSpatial().setLocalTranslation(location);
        RigidBodyControl control = entity.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsLocation(location);
        }
        BetterCharacterControl character = entity.getSpatial().getControl(BetterCharacterControl.class);
        if (character != null) {
            character.warp(location);
        }
        entity.updateDatas();
        notifyPropertyChanged("location", entity.getSpatial().getLocalTranslation());
    }

    @Override
    public void setLocalRotation(Quaternion rotation) {
        entity.getSpatial().setLocalRotation(rotation);
        RigidBodyControl control = entity.getSpatial().getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsRotation(entity.getSpatial().getWorldRotation());
        }
        entity.updateDatas();
        notifyPropertyChanged("rotation", entity.getSpatial().getLocalRotation());
    }

    @Override
    public void setLocalScale(Vector3f scale) {
        entity.getSpatial().setLocalScale(scale);
        entity.updateDatas();
        notifyPropertyChanged("scale", entity.getSpatial().getLocalScale());
    }

    @Override
    public Spatial getReadOnlySelectedSpatial() {
        return entity.getSpatial();
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
            t.onPropertyChanged(entity.getData(), property, newValue);
        });
    }
    
    public void initialize(SceneEdit  form) {
        // for child
    }
    
    public void cleanup() {
        // for child
    }
    
    public abstract Float distanceOfPick(Ray ray);
}
