/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.manager.ResManager;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.SimpleValueChangeListener;
import name.huliqing.luoying.object.attribute.StringAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.attribute.Vector3fAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityAttributeListener;
import name.huliqing.luoying.utils.FastStack;
/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T> implements SimpleValueChangeListener<Object>, ValueChangeListener {
    private static final Logger LOG = Logger.getLogger(ActorModule.class.getName());
    private final static String DATA_VIEW_DIRECTION = "viewDirection";
    private final static String DATA_WALK_DIRECTION = "walkDirection";
    
    private float radius = 0.4f;
    private float height = 3.2f;
    
    // 判断角色属性，这个属性用于判断角色是否“死亡”
    private String bindDeadAttribute;
    // 绑定角色的目标属性
    private String bindTargetAttribute;
    // 角色的质量
    private String bindMassAttribute;
    // 绑定角色属性，这个属性用来控制角色是否是可移动的，默认都是可以移动的。
    private String bindMovableAttribute;
    // 绑定角色属性，这个属性控制角色是否是可转动朝向的，默认都是可以转动的.
    private String bindRotatableAttribute;
    // 绑定角色的位置属性，可以用这个值来控制角色的位置。
    private String bindLocationAttribute;
    // 绑定角色的名称属性
    private String bindNameAttribute;
    
    // ---- inner
    private BetterCharacterControlWrap innerControl;
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    private BooleanAttribute deadAttribute;
    private NumberAttribute targetAttribute;
    private NumberAttribute massAttribute;
    private BooleanAttribute movableAttribute;
    private BooleanAttribute rotatableAttribute;
    private Vector3fAttribute locationAttribute;
    private StringAttribute nameAttribute;
    
    // 这个参数用于避免LocationAttribute位置变化时触发事件的循环调用，因为ActorModule自身会改变角色的位置值，
    // 这可能会导致内部循环触发LocationAttribute的变化事件。
    private boolean locationChangedEventEnabled = true;
    
    // 用于监听Entity被击中某个属性时的侦听器
    private final EntityAttributeListener actorEntityListener = new ActorEntityListener();
    
    @Override
    public void setData(T data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.bindDeadAttribute = data.getAsString("bindDeadAttribute");
        this.bindTargetAttribute = data.getAsString("bindTargetAttribute");
        this.bindMassAttribute = data.getAsString("bindMassAttribute");
        this.bindMovableAttribute = data.getAsString("bindMovableAttribute");
        this.bindRotatableAttribute = data.getAsString("bindRotatableAttribute");
        this.bindLocationAttribute = data.getAsString("bindLocationAttribute");
        this.bindNameAttribute = data.getAsString("bindNameAttribute");
    }

    @Override
    public void updateDatas() {
        if (initialized) {
            data.setAttribute(DATA_VIEW_DIRECTION, getViewDirection());
            if (locationAttribute != null) {
                locationChangedEventEnabled = false;
                locationAttribute.setValue(getLocation());
                locationChangedEventEnabled = true;
            }
            // 不保存这个状态，由skill去恢复走路就行。避免移动与动画不同步，
            // 这发生在当角色在走路过程中存档后，再恢复时会导致角色“不走路”但却在一直移动。
//            data.setAttribute(DATA_WALK_DIRECTION, getWalkDirection());
        }
    }
    
    private <T extends Attribute> T getAttribute(String attributeName, Class<T> type) {
        if (attributeName == null) {
            return null;
        }
        T attribute = entity.getAttributeManager().getAttribute(attributeName, type);
        if (attribute == null) {
            LOG.log(Level.WARNING, "Attribute not found by attributeName={0}, attributeType={1}, entity={2}"
                    , new Object[] {attributeName, type.getName(), entity.getData().getId()});
        }
        return attribute;
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        entity.addEntityAttributeListener(actorEntityListener);
        
        deadAttribute = getAttribute(bindDeadAttribute, BooleanAttribute.class);
        targetAttribute = getAttribute(bindTargetAttribute, NumberAttribute.class);
        massAttribute = getAttribute(bindMassAttribute, NumberAttribute.class);
        movableAttribute = getAttribute(bindMovableAttribute, BooleanAttribute.class);
        rotatableAttribute = getAttribute(bindRotatableAttribute, BooleanAttribute.class);
        locationAttribute = getAttribute(bindLocationAttribute, Vector3fAttribute.class);
        nameAttribute = getAttribute(bindNameAttribute, StringAttribute.class);
        
        // 监听角色健康值属性，当健康值等于或小于0于，角色要标记为死亡。
        deadAttribute.addSimpleValueChangeListener(this);
        targetAttribute.addSimpleValueChangeListener(this);
        if (massAttribute != null) {
            massAttribute.addSimpleValueChangeListener(this);
        }
        if (locationAttribute != null) {
            locationAttribute.addListener(this);
        }
        // 首次载入的角色可能没有指定名字，则从资源文件中获取角色名字。
        if (nameAttribute != null) {
            if (nameAttribute.getValue() == null || nameAttribute.getValue().equals("")) {
                nameAttribute.setValue(ResManager.get(entity.getData().getId() + ".name"));
            }
        }
        
        // 控制器
        this.innerControl = new BetterCharacterControlWrap(radius, height, massAttribute != null ? massAttribute.floatValue() : 60);
        Vector3f localForward = entity.getData().getAsVector3f("localForward");
        if (localForward != null) {
            this.innerControl.setLocalForward(localForward);
        }
        this.entity.getSpatial().addControl(innerControl);
        
        Vector3f viewDirection = data.getAsVector3f(DATA_VIEW_DIRECTION);
        Vector3f walkDirection = data.getAsVector3f(DATA_WALK_DIRECTION);
        if (viewDirection != null) {
            setViewDirection(viewDirection);
        }
        if (walkDirection != null) {
            setWalkDirection(walkDirection);
        }
    }
    
    @Override
    public void cleanup() {
        entity.removeEntityAttributeListener(actorEntityListener);
        if (innerControl != null) {
            // 注意要从PhysicsSpace中清理掉，否则角色看起来移除了，但是实际上物理物体还在场景中占资源。
            // 使用PhysicsSpace中的debug可以看到。
            PhysicsSpace ps = innerControl.getPhysicsSpace();
            if (ps != null) {
                ps.remove(entity.getSpatial());
            }
            entity.getSpatial().removeControl(innerControl);
        }
        super.cleanup(); 
    }

    @Override
    public void onSimpleValueChanged(Attribute attribute, Object oldValue) {
        if (attribute == targetAttribute) {
            // 通知旧目标被释放
            Entity oldTarget = entity.getScene().getEntity(((Number) oldValue).longValue());
            if (oldTarget != null) {
                ActorModule oldTargetAM = oldTarget.getModuleManager().getModule(ActorModule.class);
                if (oldTargetAM != null) {
                    oldTargetAM.notifyActorReleased(entity);
                }
            }
            // 通知新目标被锁定
            Entity newTarget = entity.getScene().getEntity(targetAttribute.getValue().longValue());
            if (newTarget != null) {
                ActorModule newTargetAM = newTarget.getModuleManager().getModule(ActorModule.class);
                if (newTargetAM != null) {
                    newTargetAM.notifyActorLocked(entity);
                }
            }
            return;
        }
        
        // 当角色质量发生变化
        if (attribute == massAttribute) {
            innerControl.setMass(massAttribute.getValue().floatValue());
        }
    }
    
    @Override
    public void onValueChanged(Attribute attribute) {
        // locationChangedEventEnabled这个参数用于避免LocationAttribute位置变化时触发事件的循环调用，
        // 因为ActorModule自身会改变角色的位置值，这可能会导致内部循环触发LocationAttribute的变化事件。
        if (locationChangedEventEnabled && attribute == locationAttribute) {
            setLocation(locationAttribute.getValue());
        }
    }
    
    /**
     * 通知侦听器，告知当前角色被某个对象（other)释放了锁定
     */
    private void notifyActorReleased(Entity other) {
        if (actorListeners != null) {
            for (ActorListener al : actorListeners) {
                al.onActorTargetReleased(entity, other);
            }
        }
    }
    
    /**
     * 通知侦听器，告知当前角色被某个对象（other)锁定了。
     */
    private void notifyActorLocked(Entity other) {
        if (actorListeners != null) {
            for (ActorListener al : actorListeners) {
                al.onActorTargetLocked(entity, other);
            }
        }
    }
    
    public Vector3f getLocation() {
        return entity.getSpatial().getLocalTranslation();
    }
    
    public void setLocation(Vector3f location) {
        entity.getSpatial().setLocalTranslation(location);
        innerControl.warp(location);
    }
    
    public Quaternion getRotation() {
        return entity.getSpatial().getLocalRotation();
    }
    
    public void setRotation(Quaternion rotation) {
        innerControl.setPhysicsRotation(rotation);
    }
    
    public void setScale(Vector3f scale) {
        entity.getSpatial().setLocalScale(scale);
    }
    
    public Vector3f getWalkDirection() {
        return innerControl.getWalkDirection();
    }
    
    public void setWalkDirection(Vector3f walkDirection) {
//        LOG.log(Level.INFO, "setWalkDirection, actor={0}, walkDirection={1}"
//                , new Object[] {actor.getData().getId(), walkDirection});
        if (movableAttribute != null && !movableAttribute.getValue()) {
            return;
        }
        innerControl.setWalkDirection(walkDirection);
    }
    
    public Vector3f getViewDirection() {
        return innerControl.getViewDirection();
    }
    
    public void setViewDirection(Vector3f viewDirection) {
        if (rotatableAttribute != null && !rotatableAttribute.getValue()) {
            return;
        }
        innerControl.setViewDirection(viewDirection);
    }
    
    /**
     * 让角色看向指定方向
     * @param position 
     */
    public void setLookAt(Vector3f position) {
        // 静态角色不能朝向
        if (innerControl.getMass() <= 0) {
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
        if (actorListener == null)
            return;
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
    
    // 这个侦听器用于侦听Entity属性被击中, 并将普通的属性击中转换为更高级一点事件响应：ActorListener, 
    private class ActorEntityListener implements EntityAttributeListener {
        
        // 这个状态用于记住被击中之前角色的死亡状态。
        // 这里必须用Stack方式，因为onHitAttributeBefore，onHitAttributeAfter随然是成对出现的，
        // 但是不能保证他们是连续调用的。
        private final FastStack<Boolean> deadStack = new FastStack<Boolean>(2);
        
        @Override
        public void onHitAttributeBefore(Attribute attribute, Object hitValue, Entity hitter) {
            deadStack.push(isDead());
        }

        @Override
        public void onHitAttributeAfter(Attribute attribute, Object hitValue, Entity hitter, Object oldValue) {
//            if (attribute != hitAttribute) {
//                throw new IllegalStateException("ActorEntityListener state error! You may call this Entity.hitAttribute(...) "
//                        + "from different threads. entityId=" + entity.getData().getId() 
//                        + ", actorModuleId=" + data.getId() 
//                        + ", hitAttributeId=" + attribute.getId() + ", hitAttributeName=" + attribute.getName() 
//                        + ", hitValue=" + hitValue + ", hitterId=" + (hitter != null ? hitter.getData().getId() : null));
//            }
            
            boolean oldDeadState = deadStack.pop();
            // 判断是否因这次击中而导致角色死亡
            boolean killed = !oldDeadState && isDead();
            
            // 通知当前entity, 已经被某一个目标击中
            notifyHitByTarget(hitter, attribute, hitValue, oldValue, killed);

            // 通知hitter：你已经击中了一个目标
            if (hitter != null) {
                ActorModule hitterAM = hitter.getModuleManager().getModule(ActorModule.class);
                if (hitterAM != null) {
                    hitterAM.notifyHitTarget(entity, attribute, hitValue, oldValue, killed);
                }
            }
        }
        
        private boolean isDead() {
            if (deadAttribute != null) {
                return deadAttribute.getValue();
            }
            return false;
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色Hit了另一个目标角色.<br>
     */
    private void notifyHitByTarget(Entity hitter, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed) {
        if (actorListeners != null) {
            for (ActorListener lis : actorListeners) {
                lis.onActorHitByTarget(entity, hitter, hitAttribute, hitValue, oldValue, killed);
            }
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色击中了一个目标角色。
     * @param targetWhichBeHit 被击中的另一个目标角色
     * @param hitAttribute 指定击中的是目标角色的哪一个属性
     * @param hitValue 击中后属性的值
     * @param oldValue 击中前属性的值
     */
    private void notifyHitTarget(Entity targetWhichBeHit, Attribute hitAttribute, Object hitValue, Object oldValue, boolean killed) {
        if (actorListeners != null) {
            for (ActorListener lis : actorListeners) {
                lis.onActorHitTarget(entity, targetWhichBeHit, hitAttribute, hitValue, oldValue, killed);
            }
        }
    }
    
    private class BetterCharacterControlWrap extends BetterCharacterControl {
        public BetterCharacterControlWrap(float radius, float height, float mass) {
            super(radius, height, mass);
        }

        @Override
        public void update(float tpf) {
            super.update(tpf);
            if (enabled && locationAttribute != null) {
                // 把位置更新回LocationAttribute属性
                // 注：locationChangedEventEnabled用于避免位置变化时引起的循环调用, 虽然这不太可能真的引起循环调用，
                // 由于第二次设置value时值是相同的，所以不会引起ValueChange事件，但是会多出一些比较和执行操作。
                // locationChangedEventEnabled可以减少这些多余的执行操作和避免可能存在的BUG隐患。
                locationChangedEventEnabled = false;
                locationAttribute.setValue(getLocation());
                locationChangedEventEnabled = true;
            }
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
        
        public void setLocalForward(Vector3f localForward) {
            if (localForward != null) {
                this.localForward.set(localForward);
            }
        }
        
        @Override
        public void setPhysicsRotation(Quaternion rotation) {
            super.setPhysicsRotation(rotation);
        }
    }
    
}
