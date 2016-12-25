/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public interface SelectObj<T> {
   
    Spatial getSelectedSpatial();
    
    void setObject(T object);
    
    T getObject();
}
