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
 */
public class FixedPosition extends AbstractPosition {

    private Vector3f pos;
    
    public FixedPosition(PositionData data) {
        super(data);
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
