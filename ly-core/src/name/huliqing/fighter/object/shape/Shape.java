/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.shape;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import name.huliqing.fighter.data.ShapeData;

/**
 *
 * @author huliqing
 */
public interface Shape {
    
    ShapeData getData();
    
    Mesh getMesh();
    
    Geometry getGeometry();
}
