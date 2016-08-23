/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.module;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.object.actor.Actor;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T>{

    private Actor actor;
    private BetterCharacterControlWrap innerControl;
    private float radius = 0.4f;
    private float height = 3.2f;
    
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
    }

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor); 
        this.innerControl = new BetterCharacterControlWrap(radius, height, actor.getData().getMass());
        this.actor = actor;
        this.actor.getSpatial().addControl(innerControl);
    }
    
    @Override
    public void cleanup() {
        if (innerControl != null) {
            actor.getSpatial().removeControl(innerControl);
        }
        super.cleanup(); 
    }
    
    public Vector3f getLocation() {
        return actor.getSpatial().getWorldTranslation();
    }
    
    public void setLocation(Vector3f location) {
        actor.getSpatial().setLocalTranslation(location);
        innerControl.warp(location);
    }
    
    public Vector3f getWalkDirection() {
        return innerControl.getWalkDirection();
    }
    
    public void setWalkDirection(Vector3f walkDirection) {
        innerControl.setWalkDirection(walkDirection);
    }
    
    public Vector3f getViewDirection() {
        return innerControl.getViewDirection();
    }
    
    public void setViewDirection(Vector3f viewDirection) {
        innerControl.setViewDirection(viewDirection);
    }
    
    /**
     * 让角色看向指定方向
     * @param position 
     */
    public void setLookAt(Vector3f position) {
        // 静态角色不能朝向
        if (actor.getData().getMass() <= 0) {
            return;
        }
        TempVars tv = TempVars.get();
        position.subtract(getLocation(), tv.vect1); 
        setViewDirection(tv.vect1.normalizeLocal());
        tv.release();
    }
    
    public void setEnabled(boolean enabled) {
        innerControl.setEnabled(enabled);
    }
    
    public boolean isEnabled() {
        return innerControl.isEnabled();
    }
    
    public boolean isKinematic() {
        return innerControl.isKinematic();
    }
    
    public void setKinematic(boolean kinematic) {
        innerControl.setKinematic(kinematic);
    }
    
     /**
     * 添加物品侦听器
     * @param actorListener 
     */
    public void addActorListener(ActorListener actorListener) {
        if (actorListeners == null) {
            actorListeners = new ArrayList<ActorListener>();
        }
        if (!actorListeners.contains(actorListener)) {
            actorListeners.add(actorListener);
        }
    }
    
    /**
     * 删除物品侦听器
     * @param actorListener
     * @return 
     */
    public boolean removeActorListener(ActorListener actorListener) {
        return actorListeners != null && actorListeners.remove(actorListener);
    }
    
    /**
     * 获取角色的物品帧听器
     * @return 
     */
    public List<ActorListener> getActorListeners() {
        return actorListeners;
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
