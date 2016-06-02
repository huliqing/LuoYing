/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.shape;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import name.huliqing.fighter.data.ShapeData;

/**
 * @author huliqing
 */
public class BoxShape extends AbstractShape {

    private Vector3f extents;
    
    // ---- inner
    private Box box;
    
    public BoxShape(ShapeData data) {
        super(data);
        extents = data.getProto().getAsVector3f("extents");
    }
    
    @Override
    public Mesh getMesh() {
        if (box == null) {
            box = new Box(extents.x, extents.y, extents.z);
        }
        return box;
    }
    
}
