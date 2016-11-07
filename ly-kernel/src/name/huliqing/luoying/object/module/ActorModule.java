/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.BooleanAttribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.SimpleValueChangeListener;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityListener;
//import name.huliqing.luoying.object.skill.Skill;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T> implements SimpleValueChangeListener<Object> {
//    private static final Logger LOG = Logger.getLogger(ActorModule.class.getName());
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
    // 绑定一个角色属性，这个属性控制角色是否是可转动朝向的，默认都是可以转动的.
    private String bindRotatableAttribute;
    
    // ---- inner
    private BetterCharacterControlWrap innerControl;
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    private BooleanAttribute deadAttribute;
    private NumberAttribute targetAttribute;
    private NumberAttribute massAttribute;
    private BooleanAttribute movableAttribute;
    private BooleanAttribute rotatableAttribute;
    
    // 用于监听Entity被击中某个属性时的侦听器
    private final EntityListener actorEntityListener = new ActorEntityListener();
    
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
    }

    @Override
    public void updateDatas() {
        if (initialized) {
            data.setAttribute(DATA_VIEW_DIRECTION, getViewDirection());
            data.setAttribute(DATA_WALK_DIRECTION, getWalkDirection());
        }
    }
    
    @Override
    public void initialize(Entity entity) {
        super.initialize(entity);
        entity.addListener(actorEntityListener);
        
        deadAttribute = entity.getAttributeManager().getAttribute(bindDeadAttribute, BooleanAttribute.class);
        targetAttribute = entity.getAttributeManager().getAttribute(bindTargetAttribute, NumberAttribute.class);
        massAttribute = entity.getAttributeManager().getAttribute(bindMassAttribute, NumberAttribute.class);
        movableAttribute = entity.getAttributeManager().getAttribute(bindMovableAttribute, BooleanAttribute.class);
        rotatableAttribute = entity.getAttributeManager().getAttribute(bindRotatableAttribute, BooleanAttribute.class);
        
         // 监听角色健康值属性，当健康值等于或小于0于，角色要标记为死亡。
        deadAttribute.addSimpleValueChangeListener(this);
        targetAttribute.addSimpleValueChangeListener(this);
        massAttribute.addSimpleValueChangeListener(this);
        
        // 控制器
        this.innerControl = new BetterCharacterControlWrap(radius, height, massAttribute != null ? massAttribute.floatValue() : 60);
        Vector3f localForward = entity.getData().getAsVector3f("localForward");
        if (localForward != null) {
            this.innerControl.setLocalForward(localForward);
        }
        this.entity.getSpatial().addControl(innerControl);
        
        // 
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
        entity.removeListener(actorEntityListener);
        if (innerControl != null) {
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
        
        if (attribute == massAttribute) {
            innerControl.setMass(massAttribute.getValue().floatValue());
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
    private class ActorEntityListener implements EntityListener {
        // 这个状态用于记住被击中之前角色的死亡状态。
        private boolean deadStateBeforeHit;
        
        @Override
        public void onHitAttributeBefore(Attribute attribute, Object hitValue, Entity hitter) {
            deadStateBeforeHit = isDead();
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
            
            // 判断是否因这次击中而导致角色死亡
            boolean killed = !deadStateBeforeHit && isDead();
            
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
    
      // remove20161104 -----------------------------------------------------------------------------------------------------------
//    /**
//     * 应用属性Hit, 让当前角色的指定属性“被击中”， 指定的属性必须存在，否则什么也不会做。
//     * @param attribute 被击中的属性
//     * @param value 属性值，这个值会应用到指定的属性,作为属性的新值。
//     * @param hitter 攻击源，表示谁发起了攻击，注意这个参数可能为null(当攻击源不存在，或者非实体时)
//     */
//    public void hitAttribute(String attribute, Object value, Entity hitter) {
//        Attribute attr = entity.getAttributeManager().getAttribute(attribute);
//        if (attr == null) {
//            return;
//        }
//        boolean oldDead = isDead();
//        Object oldValue = attr.getValue();
//        attr.setValue(value);
//        // 判断是否因属性被击中后而死亡
//        boolean killed = !oldDead && isDead();
//        // 注意：newValue必须重新获取，因为一些属性类型可能会限制value的值，所以以重新获得的newValue为准.
//        Object newValue = attr.getValue();
//        
//        // 通知当前entity, 已经被击中
//        notifyHitByTarget(hitter, attribute, newValue, oldValue, killed);
//        
//        // 通知hitter，已经击中了一个目标
//        if (hitter != null) {
//            ActorModule hitterAM = hitter.getModuleManager().getModule(ActorModule.class);
//            if (hitterAM != null) {
//                hitterAM.notifyHitTarget(entity, attribute, newValue, oldValue, killed);
//            }
//        }
//    }
    
//    /**
//     * 执行“让当前角色被另一个角色击中”的逻辑，注：属性必须是NumberAttribute类型，否则什么也不做
//     * @param hitter 发起hit的角色, hitter可以为null
//     * @param hitAttribute 属性名称
//     * @param hitValue apply到指定属性的值，可正可负
//     */
//    public void applyHitNumberAttribute(Entity hitter, String hitAttribute, float hitValue) {
//        NumberAttribute attr = entity.getAttributeManager().getAttribute(hitAttribute, NumberAttribute.class);
//        if (attr == null) {
//            return;
//        }
//        boolean deadBefore = isDead();
//        attr.add(hitValue);
//        boolean deadAfter = isDead();
//        // 这个killed用来判断角色是否由于这次攻击而死亡。
//        boolean killed = !deadBefore && deadAfter;
//        
//        // 通过侦听器：当前角色被另一个家伙击中
//        notifyHitByTarget(hitter, hitAttribute, hitValue, killed);
//        
//        // 通知攻击者，告诉攻击者：你已经击中一个目标。
//        if (hitter != null) {
//            ActorModule hitterActorModule = hitter.getModuleManager().getModule(ActorModule.class);
//            if (hitterActorModule != null) {
//                hitterActorModule.notifyHitTarget(entity, hitAttribute, hitValue, killed);
//            }
//        }
//    }
//    
//    /**
//     * 通知侦听器，让侦听器知道当前角色Hit了另一个目标角色.<br>
//     * 注：这个方法只由ActorModule内部调用。
//     * @param actorBeHit 被击中的角色
//     * @param hitAttribute
//     * @param hitValue
//     * @param killedByHit
//     */
//    private void notifyHitTarget(Entity actorBeHit, String hitAttribute, float hitValue, boolean killedByHit) {
//        LOG.log(Level.INFO, "notifyActorHitOtherListener, actorBeHit={0}, hitAttribute={1}, hitValue={2}, killedByHit={3}"
//                , new Object[] {actorBeHit.getData().getId(), hitAttribute, hitValue, killedByHit});
//        if (actorListeners != null) {
//            for (ActorListener l : actorListeners) {
//                l.onActorHitTarget(entity, actorBeHit, hitAttribute, hitValue, killedByHit);
//            }
//        }
//    }
//    
//    /**
//     * 通知侦听器，让侦听器知道当前角色被某一个目标角色击中
//     * @param hitter
//     * @param hitAttribute
//     * @param hitValue
//     * @param killedByHit 
//     */
//    private void notifyHitByTarget(Entity hitter, String hitAttribute, float hitValue, boolean killedByHit) {
//        LOG.log(Level.INFO, "notifyActorHitByTarget, hitter={0}, hitAttribute={1}, hitValue={2}, killedByHit={3}"
//                , new Object[] {hitter != null ? hitter.getData().getId() : null, hitAttribute, hitValue, killedByHit});
//        if (actorListeners != null) {
//            for (ActorListener l : actorListeners) {
//                l.onActorHitByTarget(entity, hitter, hitAttribute, hitValue, killedByHit);
//            }
//        }
//    }
    
    // --------------------------------------------------------------------------------------------------------------------------------
    
    // remove20161104
//    /**
//     * 杀死角色
//     */
//    public void kill() {
//        if (healthAttribute != null) {
////            lifeAttribute.setValue(0); // remove
//            applyHit(null, healthAttribute.getName(), Float.MAX_VALUE);
//        }
//    }
//    
//    /**
//     * 让角色复活
//     */
//    public void resurrect() {
//        if (deadAttribute != null) {
//            deadAttribute.setValue(false);
//        }
//        if (healthAttribute != null) {
//            healthAttribute.setValue(1);
//        }
//    }
    
//    /**
//     * 判断一个目标是否为敌人
//     * @param target
//     * @return 
//     */
//    public boolean isEnemy(Entity target) {
//        if (target == null || target == entity) {
//            return false;
//        }
//        // 如果目标分组值小于或等于0，则始终认为“不”是敌人，这样允许游戏添加一些无害的中立小动物
//        ActorModule targetActorModule = target.getModuleManager().getModule(ActorModule.class);
//        if (targetActorModule.getGroup() <= 0) {
//            return false;
//        }
//        return (targetActorModule.getGroup() != getGroup());
//    }
//    
//    /**
//     * 获取角色的分组（派系），如果没有给角色配置任何分组，则该方法始终返回0;
//     * @return 
//     */
//    public int getGroup() {
//        if (groupAttribute != null) {
//            return groupAttribute.intValue();
//        }
//        return 0;
//    }
//    
//    /**
//     * 设置角色分组,如果当前模块没有绑定分组属性，则该方法什么也不做。
//     * @param group 
//     */
//    public void setGroup(int group) {
//        if (groupAttribute != null) {
//            groupAttribute.setValue(group);
//        }
//    }
//    
//    /**
//     * 获取角色的队伍，如果没有给角色配置任何队伍，则该方法始终返回0;
//     * @return 
//     */
//    public int getTeam() {
//        if (teamAttribute != null) {
//            return teamAttribute.intValue();
//        }
//        return 0;
//    }
//
//    /**
//     * 设置角色队伍,如果当前模块没有绑定队伍属性，则该方法什么也不做。
//     * @param team 
//     */
//    public void setTeam(int team) {
//        if (teamAttribute != null) {
//            teamAttribute.setValue(team);
//        }
//    }
//    
//    /**
//     * 获取角色的健康值
//     * @return 
//     */
//    public int getHealth() {
//        if (healthAttribute != null) {
//            return healthAttribute.intValue();
//        }
//        return 0;
//    }
//    
//    /**
//     * 设置角色的健康值
//     * @param health 
//     */
//    public void setHealth(int health) {
//        if (healthAttribute != null) {
//            healthAttribute.setValue(health);
//        }
//    }
//    
//    /**
//     * 获取角色的视角距离,如果模块没有绑定角色的视角属性，则该方法始终返回0.
//     * @return 
//     */
//    public float getViewDistance() {
//        if (viewAttribute != null) {
//            return viewAttribute.floatValue();
//        }
//        return 0;
//    }
//    
//    /**
//     * 设置角色的视角距离。
//     * @param viewDistance 
//     */
//    public void setViewDistance(float viewDistance) {
//        if (viewAttribute != null) {
//            viewAttribute.setValue(viewDistance);
//        }
//    }
//    
//    /**
//     * 获取角色当前的目标,如果没有或者目标角色不存在则返回null.
//     * @return 
//     */
//    public Entity getTarget() {
//        if (targetAttribute != null) {
//            
////            return playService.findActor(targetAttribute.longValue());
//            
//            return entity.getScene().getEntity(targetAttribute.longValue());
//        }
//        return null;
//    }
//    
//    /**
//     * 设置当前角色的目标对象
//     * @param target 目标对象的唯一ID
//     */
//    public void setTarget(Entity target) {
//        if (targetAttribute != null) {
//            targetAttribute.setValue(target != null ? target.getData().getUniqueId() : -1L);
//        }
//    }
//    
//    @Override
//    public void onValueChanged(Attribute attribute, Number oldValue, Number newValue) {
//        // 当targetAttribute发生变化时触发侦听器
//        if (attribute == targetAttribute) {
//            // 释放旧目标的listener
//            Entity oldTarget = entity.getScene().getEntity(oldValue.longValue());
//            if (oldTarget != null) {
//                ActorModule oldTargetActorModule = oldTarget.getModuleManager().getModule(ActorModule.class);
//                if (oldTargetActorModule != null) {
//                    oldTargetActorModule.notifyActorTargetReleasedListener(entity);
//                }
//            }
//            // 锁定新目标的listener.
//            Entity newTarget = entity.getScene().getEntity(newValue.longValue());
//            if (newTarget != null) {
//                ActorModule newTargetActorModule = newTarget.getModuleManager().getModule(ActorModule.class);
//                if (newTargetActorModule != null) {
//                    newTargetActorModule.notifyActorTargetLockedListener(entity);
//                }
//            }
//            return;
//        }
//        // 角色质量属性在外部被改变时要更新innerControl.
//        if (attribute == massAttribute) {
//            innerControl.setMass(massAttribute.floatValue());
//            return;
//        }
//        // 监听到健康值为0时，把角色标记为死亡
//        if (attribute == healthAttribute) {
//            if (deadAttribute != null) {
//                deadAttribute.setValue(healthAttribute.intValue() <= 0);
//            }
//        }
//    }
//    
//    /**
//     * 获取角色当前的跟随目标
//     * @return 
//     */
//    public long getFollowTarget() {
//        if (followTargetAttribute != null) {
//            return followTargetAttribute.longValue();
//        }
//        return 0;
//    }
//    
//    /**
//     * 设置角色当前的跟随目标
//     * @param target  角色的唯一ID
//     */
//    public void setFollowTarget(long target) {
//        if (followTargetAttribute != null) {
//            followTargetAttribute.setValue(target);
//        }
//    }
//
//    /**
//     * 判断角色是否为“必不可少的”这种角色即使死亡也不应该被移除出场景
//     * @return 
//     */
//    public boolean isEssential() {
//        if (essentialAttribute != null) {
//            return essentialAttribute.getValue();
//        }
//        return false;
//    }
//
//    /**
//     * 设置角色为“必不可少的”这种角色即使死亡也不应该被移除出场景
//     * @param essential 
//     */
//    public void setEssential(boolean essential) {
//        if (essentialAttribute != null) {
//            essentialAttribute.setValue(essential);
//        }
//    }
//    
//    /**
//     * 判断角色是否为“生物”
//     * @return 
//     */
//    public boolean isBiology() {
//        return biologyAttribute != null && biologyAttribute.getValue();
//    }
//    
//    /**
//     * 设置角色为是否为“生物”类型角色
//     * @param biology 
//     */
//    public void setBiology(boolean biology) {
//        if (biologyAttribute != null) {
//            biologyAttribute.setValue(biology);
//        }
//    }
//    
//    /**
//     * 获取当前角色的所有者id,如果没有则返回0
//     * @return 
//     */
//    public long getOwner() {
//        if (ownerAttribute != null) {
//            return ownerAttribute.longValue();
//        }
//        return 0;
//    }
//
//    /**
//     * 设置当前角色的所有者
//     * @param ownerId 
//     */
//    public void setOwner(long ownerId) {
//        if (ownerAttribute != null) {
//            ownerAttribute.setValue(ownerId);
//        }
//    }
//    
//    /**
//     * 判断目标角色是否为一个玩家角色
//     * @return 
//     */
//    public boolean isPlayer() {
//        return player;
//    }
//    
//    /**
//     * 标记目标这“玩家”角色
//     * @param player 
//     */
//    public void setPlayer(boolean player) {
//        this.player = player;
//    }
//    
//    /**
//     * 获取角色的物理质量
//     * @return 
//     */
//    public float getMass() {
//        if (massAttribute != null) {
//            return massAttribute.floatValue();
//        }
//        return 0;
//    }
//    
//    /**
//     * 设置角色的物理质量
//     * @param mass 
//     */
//    public void setMass(float mass) {
//        if (massAttribute != null) {
//            massAttribute.setValue(mass);
//            innerControl.setMass(mass);
//        }
//    }
//    
//    /**
//     * 判断角色是否是可移动的
//     * @return 
//     */
//    public boolean isMovable() {
//        return movableAttribute != null && movableAttribute.getValue();
//    }
//    
//    /**
//     * 设置角色是否为可移动
//     * @param movable 
//     */
//    public void setMovable(boolean movable) {
//        if (movableAttribute != null) {
//            movableAttribute.setValue(movable);
//        }
//    }
//    
//    /**
//     * 判断角色是否为可转动，可转换朝向的
//     * @return 
//     */
//    public boolean isRotatable() {
//        return rotatableAttribute != null && rotatableAttribute.getValue();
//    }
//    
//    /**
//     * 设置角色为是否可转动，可转换朝向的
//     * @param rotatable 
//     */
//    public void setRotatable(boolean rotatable) {
//        if (rotatableAttribute != null) {
//            rotatableAttribute.setValue(rotatable);
//        }
//    }
    
    
    // remove20161104
//    /**
//     * 通知侦听器，让侦听器知道当前角色(actor)已经被某一个目标(lockedByTarget)锁定。
//     * @param lockedByTarget 
//     */
//    private void notifyActorTargetLockedListener(Entity lockedByTarget) {
//        LOG.log(Level.INFO, "notifyActorTargetLockedListener, sourceBeLocked={0}, lockedByTarget={1}"
//                , new Object[] {entity.getData().getId(), lockedByTarget.getData().getId()});
//        if (actorListeners != null) {
//            for (ActorListener lis : actorListeners) {
//                lis.onActorTargetLocked(entity, lockedByTarget);
//            }
//        }
//    }
//    
//    /**
//     * 通知侦听器，让侦听器知道当前角色(actor)已经被某一个目标(releasedByTarget)释放锁定。
//     * @param releasedByTarget 
//     */
//    private void notifyActorTargetReleasedListener(Entity releasedByTarget) {
//        LOG.log(Level.INFO, "notifyActorTargetReleasedListener, sourceBeReleased={0}, releasedByTarget={1}"
//                , new Object[] {entity.getData().getId(), releasedByTarget.getData().getId()});
//        if (actorListeners != null) {
//            for (ActorListener lis : actorListeners) {
//                lis.onActorTargetReleased(entity, releasedByTarget);
//            }
//        }
//    }
    
    
}
