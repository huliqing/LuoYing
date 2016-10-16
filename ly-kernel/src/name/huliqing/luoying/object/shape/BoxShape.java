/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.shape;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import name.huliqing.luoying.data.ShapeData;

/**
 * @author huliqing
 * @param <T>
 */
public class BoxShape<T extends ShapeData> extends AbstractShape<T> {

    private Vector3f extents;
    
    // ---- inner
    private Box box;

    @Override
    public void setData(T data) {
        super.setData(data);
        extents = data.getAsVector3f("extents");
    }
    
    @Override
    public Mesh getMesh() {
        if (box == null) {
            box = new Box(extents.x, extents.y, extents.z);
        }
        return box;
    }

    
}
