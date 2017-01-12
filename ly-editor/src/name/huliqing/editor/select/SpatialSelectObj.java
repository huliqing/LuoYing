/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class SpatialSelectObj implements SelectObj<Spatial> {

    private Spatial spatial;
    
    public SpatialSelectObj() {}
    
    public SpatialSelectObj(Spatial object) {
        this.spatial = object;
    }
    
    @Override
    public void setObject(Spatial object) {
        this.spatial = object;
    }

    @Override
    public Spatial getObject() {
        return spatial;
    }

    @Override
    public void setLocalTranslation(Vector3f location) {
        spatial.setLocalTranslation(location);
        RigidBodyControl control = spatial.getControl(RigidBodyControl.class);
        if (control != null) {
            control.setPhysicsLocation(spatial.getWorldTranslation());
        }
        CharacterControl character = spatial.getControl(CharacterControl.class);
        if (character != null) {
            character.setPhysicsLocation(spatial.getWorldTranslation());
        }
    }

    @Override
    public void setLocalRotation(Quaternion rotation) {
        spatial.setLocalRotation(rotation);
    }

    @Override
    public void setLocalScale(Vector3f scale) {
        spatial.setLocalScale(scale);
    }

    @Override
    public Spatial getReadOnlySelectedSpatial() {
        return spatial;
    }

    
}
