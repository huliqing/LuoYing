/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author huliqing
 */
public class InterpolateRotationAnim extends AbstractAnim<Spatial> {

    private final Quaternion start = new Quaternion();
    private final Quaternion end = new Quaternion();
    private final Quaternion temp = new Quaternion();
    
    public void setStart(Quaternion start) {
        this.start.set(start);
    }
    public void setStart(Vector3f direction, Vector3f up) {
        this.start.lookAt(direction, up);
    }
    
    public void setEnd(Quaternion end) {
        this.end.set(end);
    }
    
    public void setEnd(Vector3f direction, Vector3f up) {
        this.end.lookAt(direction, up);
    }
    
    @Override
    protected void doAnimInit() {
        target.setLocalRotation(start);
    }

    @Override
    protected void doAnimUpdate(float interpolation) {
        temp.slerp(start, end, interpolation);
        target.setLocalRotation(temp);
    }
    
}
