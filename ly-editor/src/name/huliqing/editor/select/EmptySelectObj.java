/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.select;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * 用于默认的没有选择任何物体时的情形, 避免NullPoint
 * @author huliqing
 */
public class EmptySelectObj implements SelectObj {
    
    private final Vector3f location = new Vector3f();
    private final Quaternion rotation = new Quaternion();
    private final Vector3f scale = new Vector3f(1,1,1);

    /**
     * 设置物体对方法不会有任何作用
     * @param object 
     */
    @Override
    public void setObject(Object object) {
        // ignore
    }

    /**
     * 物方法将始终返回null.
     * @return 
     */
    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public void setLocation(Vector3f location) {
        this.location.set(location);
    }

    @Override
    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
    }

    @Override
    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }

    @Override
    public Vector3f getLocation() {
        return location;
    }

    @Override
    public Quaternion getRotation() {
        return rotation;
    }

    @Override
    public Vector3f getScale() {
        return scale;
    }
    
}
