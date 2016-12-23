/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class SpatialSelectObj implements SelectObj<Spatial> {

    private Spatial spatial;
    
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
    public void setLocation(Vector3f location) {
        spatial.setLocalTranslation(location);
    }

    @Override
    public void setRotation(Quaternion rotation) {
        spatial.setLocalRotation(rotation);
    }

    @Override
    public void setScale(Vector3f scale) {
        spatial.setLocalScale(scale);
    }

    @Override
    public Vector3f getLocation() {
        return spatial.getWorldTranslation();
    }

    @Override
    public Quaternion getRotation() {
        return spatial.getWorldRotation();
    }

    @Override
    public Vector3f getScale() {
        return spatial.getWorldScale();
    }
    
}
