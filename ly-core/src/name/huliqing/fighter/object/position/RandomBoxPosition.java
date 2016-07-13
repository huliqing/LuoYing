/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.PositionData;

/**
 * BOX内的随机点
 * @author huliqing
 * @param <T>
 */
public final class RandomBoxPosition<T extends PositionData> extends AbstractPosition<T> {

    private EmitterShape shape;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        shape = new EmitterBoxShape(data.getAsVector3f("min"), data.getAsVector3f("max"));
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        shape.getRandomPoint(store);
        return store;
    }
    
}
