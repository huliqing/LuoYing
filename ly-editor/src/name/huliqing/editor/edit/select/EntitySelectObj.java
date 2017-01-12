/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.editor.edit.scene.SceneEditForm;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class EntitySelectObj<T extends Entity> implements SelectObj<T> {
     
    protected T entity;
    
    public EntitySelectObj() {}
    
    public EntitySelectObj(T entity) {
        this.entity = entity;
    }
    
    @Override
    public void setObject(T entity) {
        this.entity = entity;
    }

    @Override
    public T getObject() {
        return entity;
    }

    @Override
    public void setLocalTranslation(Vector3f location) {
        entity.getSpatial().setLocalTranslation(location);
    }

    @Override
    public void setLocalRotation(Quaternion rotation) {
        entity.getSpatial().setLocalRotation(rotation);
    }

    @Override
    public void setLocalScale(Vector3f scale) {
        entity.getSpatial().setLocalScale(scale);
    }

    @Override
    public Spatial getReadOnlySelectedSpatial() {
        return entity.getSpatial();
    }

    public abstract Float distanceOfPick(Ray ray);
    
    public abstract void initialize(SceneEditForm  form);
    
    public abstract void cleanup();
    
    
    
}
