/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.PositionData;

/**
 * 圆球内的随机点
 * @author huliqing
 */
public final class RandomSpherePosition extends AbstractPosition {

    private EmitterShape shape;
    
    public RandomSpherePosition(PositionData data) {
        super(data);
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
