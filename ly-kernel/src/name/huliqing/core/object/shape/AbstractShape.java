/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.shape;

import com.jme3.scene.Geometry;
import name.huliqing.core.data.ShapeData;
import name.huliqing.core.utils.MatUtils;

/**
 *
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractShape<T extends ShapeData> implements Shape<T> {

    protected T data;
    protected Geometry geometry;

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T getData() {
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
