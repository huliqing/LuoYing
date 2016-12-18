/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.anim;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import name.huliqing.luoying.data.AnimData;

/**
 *
 * @author huliqing
 */
public class InterpolateRotationAnim extends AbstractAnim<Spatial> {

    private final Quaternion start = new Quaternion();
    private final Quaternion end = new Quaternion();
    private final Quaternion temp = new Quaternion();

    private boolean changed;
    
    @Override
    public void setData(AnimData data) {
        super.setData(data);
        Quaternion tempStart = data.getAsQuaternion("start");
        Quaternion tempEnd = data.getAsQuaternion("end");
        if (tempStart != null) {
            start.set(tempStart);
        }
        if (tempEnd != null) {
            end.set(tempEnd);
        }
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        if (changed) {
            data.setAttribute("start", start);
            data.setAttribute("end", end);
        }
    }
    
    public void setStart(Quaternion start) {
        this.start.set(start);
        changed = true;
    }
    public void setStart(Vector3f direction, Vector3f up) {
        this.start.lookAt(direction, up);
        changed = true;
    }
    
    public void setEnd(Quaternion end) {
        this.end.set(end);
        changed = true;
    }
    
    public void setEnd(Vector3f direction, Vector3f up) {
        this.end.lookAt(direction, up);
        changed = true;
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
