/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.control;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.TempVars;
import name.huliqing.core.data.ControlData;
import name.huliqing.core.object.actor.Actor;

/**
 * 角色的物理控制器
 * @author huliqing
 */
public class ActorPhysicsControl extends ActorControl<ControlData> {

    private BetterCharacterControlWrap innerControl;
    private float radius = 1;
    private float height = 1;
    private float mass = 1;

    @Override
    public void setData(ControlData data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.mass = data.getAsFloat("mass", mass);
    }
    
    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        if (innerControl == null) {
            innerControl = new BetterCharacterControlWrap(radius, height, mass);
        }
        spatial.addControl(innerControl);
    }

    @Override
    public void actorUpdate(float tpf) {}
    
    @Override
    public void actorRender(RenderManager rm, ViewPort vp) {}

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        innerControl.setEnabled(enabled);
    }

    @Override
    public void cleanup() {
        spatial.removeControl(innerControl);
        super.cleanup(); 
    }
    
    public Vector3f getLocation() {
        return spatial.getWorldTranslation();
    }
    
    public void setLocation(Vector3f location) {
        innerControl.warp(location);
    }
    
    public Vector3f getViewDirection() {
        return innerControl.getViewDirection();
    }
    
    public void setViewDirection(Vector3f viewDirection) {
        innerControl.setViewDirection(viewDirection);
    }
    
    public Vector3f getWalkDirection() {
        return innerControl.getWalkDirection();
    }
    
    public void setWalkDirection(Vector3f walkDirection) {
        innerControl.setWalkDirection(walkDirection);
    }
    
    public float getMass() {
        return innerControl.getMass();
    }
    
    public void setMass(float mass) {
        this.mass = mass;
        innerControl.setMass(mass);
    }
    
    public boolean isKinematic() {
        return innerControl.isKinematic();
    }
    
    public void setKinematic(boolean kinematic) {
        innerControl.setKinematic(kinematic);
    }
    
    /**
     * 让角色看向指定方向
     * @param position 
     */
    public void setLookAt(Vector3f position) {
        // 静态角色不能朝向
        if (mass <= 0) {
            return;
        }
        TempVars tv = TempVars.get();
        position.subtract(getLocation(), tv.vect1); 
        setViewDirection(tv.vect1.normalizeLocal());
        tv.release();
    }
    
    // --------------------------------------------------------------------------------------------------------------------------------
    
    private class BetterCharacterControlWrap extends BetterCharacterControl {

        public BetterCharacterControlWrap(float radius, float height, float mass) {
            super(radius, height, mass);
        }
        
        public float getMass() {
            return rigidBody.getMass();
        }
        
        public void setMass(float mass) {
            this.rigidBody.setMass(mass);
        }
        
        public boolean isKinematic() {
            return rigidBody.isKinematic();
        }

        public void setKinematic(boolean kinematic) {
            rigidBody.setKinematic(kinematic);
        }
    }
}
