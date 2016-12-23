/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.object.entity.Entity;

/**
 *
 * @author huliqing
 */
public class EntitySelectObj implements SelectObj<Entity> {
    
    private Entity entity;

    public EntitySelectObj(Entity entity) {
        this.entity = entity;
    }
    
    @Override
    public void setObject(Entity object) {
        this.entity = object;
    }

    @Override
    public Entity getObject() {
        return entity;
    }

    @Override
    public void setLocation(Vector3f location) {
        entity.getSpatial().setLocalTranslation(location);
    }

    @Override
    public void setRotation(Quaternion rotation) {
        entity.getSpatial().setLocalRotation(rotation);
    }

    @Override
    public void setScale(Vector3f scale) {
        entity.getSpatial().setLocalScale(scale);
    }
    
    @Override
    public Vector3f getLocation() {
        return entity.getSpatial().getWorldTranslation();
    }

    @Override
    public Quaternion getRotation() {
        return entity.getSpatial().getWorldRotation();
    }

    @Override
    public Vector3f getScale() {
        return entity.getSpatial().getWorldScale();
    }
}
