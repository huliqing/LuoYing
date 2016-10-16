/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.position;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.PositionData;

/**
 * 圆球内的随机点
 * @author huliqing
 * @param <T>
 */
public final class RandomSpherePosition<T extends PositionData> extends AbstractPosition<T> {

    private EmitterShape shape;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        shape = new EmitterSphereShape(
                  data.getAsVector3f("center")
                , data.getAsFloat("radius", 1));
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        shape.getRandomPoint(store);
        return store;
    }
    
}
