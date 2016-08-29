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
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.data.module.ModuleData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.NumberAttribute;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T>{

    private static final Logger LOG = Logger.getLogger(ActorModule.class.getName());
    
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    private Actor actor;
    private BetterCharacterControlWrap innerControl;
    private float radius = 0.4f;
    private float height = 3.2f;
    
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    // 生命值属性
    private String lifeAttributeName;
    private NumberAttribute lifeAttribute;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.lifeAttributeName = data.getAsString("lifeAttributeName");
    }

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.innerControl = new BetterCharacterControlWrap(radius, height, actor.getData().getMass());
        this.actor = actor;
        this.actor.getSpatial().addControl(innerControl);
        
        lifeAttribute = attributeService.getAttributeByName(actor, lifeAttributeName);
        if (lifeAttribute == null) {
            LOG.log(Level.WARNING, "Life attribute not found, by lifeAttributeName={0}, actorId={1}"
                    , new Object[] {lifeAttributeName, actor.getData().getId()});
        }
    }
    
    @Override
    public void cleanup() {
        if (innerControl != null) {
            actor.getSpatial().removeControl(innerControl);
        }
        lifeAttribute = null;
        super.cleanup(); 
    }
    
    /**
     * 杀死角色
     */
    public void kill() {
        if (lifeAttribute != null) {
            lifeAttribute.setValue(0);
        }
    }
    
    /**
     * 让角色复活
     */
    public void resurrect() {
        if (lifeAttribute != null) {
            lifeAttribute.setValue(1);
        }
    }
    
    /**
     * 判断角色是否已经死亡
     * @return 
     */
    public boolean isDead() {
        if (lifeAttribute != null) {
            return lifeAttribute.intValue() > 0;
        }
        return false;
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
