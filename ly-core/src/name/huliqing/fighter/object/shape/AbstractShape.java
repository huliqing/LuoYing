/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.shape;

import com.jme3.scene.Geometry;
import name.huliqing.fighter.data.ShapeData;
import name.huliqing.fighter.utils.MatUtils;

/**
 *
 * @author huliqing
 */
public abstract class AbstractShape implements Shape {

    protected ShapeData data;
    protected Geometry geometry;
    
    public AbstractShape(ShapeData data) {
        this.data = data;
    }

    @Override
    public ShapeData getData() {
        return data;
    }

    @Override
    public Geometry getGeometry() {
        if (geometry == null) {
            geometry = new Geometry("AbstractShape", getMesh());
            geometry.setMaterial(MatUtils.createWireFrame());
        }
        return geometry;
    }
    
}
