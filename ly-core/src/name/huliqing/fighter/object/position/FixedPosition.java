/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import com.jme3.math.Vector3f;
import name.huliqing.fighter.data.PositionData;

/**
 * 固定点
 * @author huliqing
 * @param <T>
 */
public class FixedPosition<T extends PositionData> extends AbstractPosition<T> {

    private Vector3f pos;

    @Override
    public void setData(T data) {
        super.setData(data); 
        this.pos = data.getAsVector3f("point");
    }

    @Override
    public Vector3f getPoint(Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(pos);
        return store;
    }
    
}
