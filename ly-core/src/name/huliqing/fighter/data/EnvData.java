/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.data;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

/**
 *
 * @author huliqing
 */
@Serializable
public class EnvData extends ProtoData {

    private String file;
    private Vector3f location = new Vector3f();
    private Quaternion rotation = new Quaternion();
    private Vector3f scale = new Vector3f(1,1,1);
    private boolean physics;
    // 质量
    private float mass;
    // 摩擦力
    private float friction;
    private ShadowMode shadowMode;
    private boolean useUnshaded;
    
    public EnvData() {}
    
    public EnvData(String id) {
        super(id);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location.set(location);
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }

    public boolean isPhysics() {
        return physics;
    }

    public void setPhysics(boolean physics) {
        this.physics = physics;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public ShadowMode getShadowMode() {
        return shadowMode;
    }

    public void setShadowMode(ShadowMode shadowMode) {
        this.shadowMode = shadowMode;
    }

    public boolean isUseUnshaded() {
        return useUnshaded;
    }

    public void setUseUnshaded(boolean useUnshaded) {
        this.useUnshaded = useUnshaded;
    }
    
}
