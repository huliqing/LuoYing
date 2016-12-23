/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author huliqing
 */
public interface SelectObj<T> {
    
    void setObject(T object);
    
    T getObject();
    
    Vector3f getLocation();
    
    void setLocation(Vector3f location);
    
    Quaternion getRotation();
    
    void setRotation(Quaternion rotation);
    
    Vector3f getScale();
    
    void setScale(Vector3f scale);
}
