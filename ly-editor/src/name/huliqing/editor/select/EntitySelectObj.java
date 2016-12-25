/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.scene.Spatial;
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
    public Spatial getSelectedSpatial() {
        return entity.getSpatial();
    }

}
