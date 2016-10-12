/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModelEntityData extends EntityData {
    
    public Vector3f getLocation() {
        return getAsVector3f("location", Vector3f.ZERO);
    }
    
    public void setLocation(Vector3f location) {
        setAttribute("location", location);
    }
    
    public Quaternion getRotation() {
        return getAsQuaternion("rotation", Quaternion.IDENTITY);
    }
    
    public void setRotation(Quaternion rotation) {
        setAttribute("rotation", rotation);
    }
    
    public Vector3f getScale() {
        return getAsVector3f("scale", Vector3f.UNIT_XYZ);
    }
    
    public void setScale(Vector3f scale) {
        setAttribute("scale", scale);
    }
    
}
