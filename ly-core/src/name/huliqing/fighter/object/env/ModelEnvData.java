/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.env;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.queue.RenderQueue;
import name.huliqing.fighter.data.EnvData;

/**
 *
 * @author huliqing
 */
@Serializable
public class ModelEnvData extends EnvData{
    
    private String file;
    // 注意不要在这里使用final关键字，否则Serializable无法重建参数
    private Vector3f location = new Vector3f();
    private Quaternion rotation = new Quaternion();
    private Vector3f scale = new Vector3f(1,1,1);
    // 是否使用物体功能
    private boolean physics;
    // 质量
    private float mass;
    // 摩擦力
    private float friction;
    private RenderQueue.ShadowMode shadowMode;
    private boolean useUnshaded;
    // 是否作为地形的一部分，当作为地型的一部分时可进行点击，并让角色在上面行走。
    private boolean terrain;
    
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

    public RenderQueue.ShadowMode getShadowMode() {
        return shadowMode;
    }

    public void setShadowMode(RenderQueue.ShadowMode shadowMode) {
        this.shadowMode = shadowMode;
    }

    /**
     * 是否使用unshaded材质
     * @return 
     */
    public boolean isUseUnshaded() {
        return useUnshaded;
    }

    public void setUseUnshaded(boolean useUnshaded) {
        this.useUnshaded = useUnshaded;
    }

    /**
     * 是否作为地形的一部分，当作为地型的一部分时可进行点击，并让角色在上面行走。
     * @return 
     */
    public boolean isTerrain() {
        return terrain;
    }

    /**
     * 是否作为地形的一部分，当作为地型的一部分时可进行点击，并让角色在上面行走。
     * @param terrain 
     */
    public void setTerrain(boolean terrain) {
        this.terrain = terrain;
    }
    
}
