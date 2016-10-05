/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.module;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Factory;
import name.huliqing.ly.data.ModuleData;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.attribute.Attribute;
import name.huliqing.ly.object.attribute.BooleanAttribute;
import name.huliqing.ly.object.attribute.NumberAttribute;
import name.huliqing.ly.object.attribute.ValueChangeListener;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T> implements ValueChangeListener<Number> {
    private static final Logger LOG = Logger.getLogger(ActorModule.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);
    
    private AttributeModule attributeModule;

    private BetterCharacterControlWrap innerControl;
    private float radius = 0.4f;
    private float height = 3.2f;
    
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    // 生命值属性名称
    private String bindHealthAttribute;
    // 角色所在分组属性名称,分组决定了角色所在的派系
    private String bindGroupAttribute;
    // 角色所在队伍
    private String bindTeamAttribute;
    // 角色的可视范围属性名称
    private String bindViewAttribute;
    // 角色的当前目标对象。
    private String bindTargetAttribute;
    // 当前角色所跟踪的目标对象
    private String bindFollowTargetAttribute;
    // 当前角色的所有者唯一id,也可以说是当前角色的主人的id
    private String bindOwnerAttribute;
    // 角色的质量
    private String bindMassAttribute;
    // 角色是否是“必要的”
    private String bindEssentialAttribute;
    // 角色是否是生物
    private String bindBiologyAttribute;
    // 绑定角色属性，这个属性定义角色是否是可移动的
    private String bindMovableAttribute;
    // 绑定一个角色属性，这个属性定义角色是否是可转动朝向的。
    private String bindRotatableAttribute;
    // 判断角色属性，这个属性用于判断角色是否“死亡”
    private String bindDeadAttribute;
    
    private NumberAttribute healthAttribute;
    private NumberAttribute groupAttribute;
    private NumberAttribute teamAttribute;
    private NumberAttribute viewAttribute;
    private NumberAttribute targetAttribute;
    private NumberAttribute followTargetAttribute;
    private NumberAttribute ownerAttribute;
    private NumberAttribute massAttribute;
    private BooleanAttribute essentialAttribute;
    private BooleanAttribute biologyAttribute;
    private BooleanAttribute movableAttribute;
    private BooleanAttribute rotatableAttribute;
    private BooleanAttribute deadAttribute;
    
    // ---- ext
    // 标记目标角色是否为“玩家”角色,这个参数为程序动态设置,不存档
    private boolean player;
    
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
    }
    
    @Override
    public void setData(T data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.bindHealthAttribute = data.getAsString("bindHealthAttribute");
        this.bindGroupAttribute = data.getAsString("bindGroupAttribute");
        this.bindTeamAttribute = data.getAsString("bindTeamAttribute");
        this.bindViewAttribute = data.getAsString("bindViewAttribute");
        this.bindTargetAttribute = data.getAsString("bindTargetAttribute");
        this.bindFollowTargetAttribute = data.getAsString("bindFollowTargetAttribute");
        this.bindOwnerAttribute = data.getAsString("bindOwnerAttribute");
        this.bindMassAttribute = data.getAsString("bindMassAttribute");
        this.bindMovableAttribute = data.getAsString("bindMovableAttribute");
        this.bindRotatableAttribute = data.getAsString("bindRotatableAttribute");
        this.bindEssentialAttribute = data.getAsString("bindEssentialAttribute");
        this.bindBiologyAttribute = data.getAsString("bindBiologyAttribute");
        this.bindDeadAttribute = data.getAsString("bindDeadAttribute");
    }

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        attributeModule = actor.getModule(AttributeModule.class);
        
        healthAttribute = attributeModule.getAttributeByName(bindHealthAttribute, NumberAttribute.class);
        groupAttribute = attributeModule.getAttributeByName(bindGroupAttribute, NumberAttribute.class);
        teamAttribute = attributeModule.getAttributeByName(bindTeamAttribute, NumberAttribute.class);
        viewAttribute = attributeModule.getAttributeByName(bindViewAttribute, NumberAttribute.class);
        targetAttribute = attributeModule.getAttributeByName(bindTargetAttribute, NumberAttribute.class);
        followTargetAttribute = attributeModule.getAttributeByName(bindFollowTargetAttribute, NumberAttribute.class);
        ownerAttribute = attributeModule.getAttributeByName(bindOwnerAttribute, NumberAttribute.class);
        massAttribute = attributeModule.getAttributeByName(bindMassAttribute, NumberAttribute.class);
        
        movableAttribute = attributeModule.getAttributeByName(bindMovableAttribute, BooleanAttribute.class);
        rotatableAttribute = attributeModule.getAttributeByName(bindRotatableAttribute, BooleanAttribute.class);
        essentialAttribute = attributeModule.getAttributeByName(bindEssentialAttribute, BooleanAttribute.class);
        biologyAttribute = attributeModule.getAttributeByName(bindBiologyAttribute, BooleanAttribute.class);
        deadAttribute = attributeModule.getAttributeByName(bindDeadAttribute, BooleanAttribute.class);
        
        healthAttribute.addListener(this);  // 监听角色健康值属性，当健康值等于或小于0于，角色要标记为死亡。
        targetAttribute.addListener(this);  //  监听目标变更属性，以便角色的当前目标发生变化时可以触发侦听器
        massAttribute.addListener(this);    //  监听角色质量属性，当质量发生变化时要触发innerControl更新角色质量
        
        // 控制器
        this.innerControl = new BetterCharacterControlWrap(radius, height, getMass());
        this.innerControl.setLocalForward(actor.getData().getLocalForward());
        this.actor.getSpatial().addControl(innerControl);
        
    }
    
    @Override
    public void cleanup() {
        if (innerControl != null) {
            actor.getSpatial().removeControl(innerControl);
        }
        if (targetAttribute != null) {
            targetAttribute.removeListener(this);
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
//        LOG.log(Level.INFO, "setWalkDirection, actor={0}, walkDirection={1}"
//                , new Object[] {actor.getData().getId(), walkDirection});
        if (movableAttribute != null && movableAttribute.booleanValue()) {
            innerControl.setWalkDirection(walkDirection);
        }
    }
    
    public Vector3f getViewDirection() {
        return innerControl.getViewDirection();
    }
    
    public void setViewDirection(Vector3f viewDirection) {
        if (rotatableAttribute != null && rotatableAttribute.booleanValue()) {
            innerControl.setViewDirection(viewDirection);
        }
    }
    
    /**
     * 让角色看向指定方向
     * @param position 
     */
    public void setLookAt(Vector3f position) {
        // 静态角色不能朝向
        if (getMass() <= 0) {
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
    
    /**
     * 杀死角色
     */
    public void kill() {
        if (healthAttribute != null) {
//            lifeAttribute.setValue(0); // remove
            applyHit(null, healthAttribute.getName(), Float.MAX_VALUE);
        }
    }
    
    /**
     * 让角色复活
     */
    public void resurrect() {
        if (deadAttribute != null) {
            deadAttribute.setValue(false);
        }
        if (healthAttribute != null) {
            healthAttribute.setValue(1);
        }
    }
    
    /**
     * 判断角色是否已经死亡
     * @return 
     */
    public boolean isDead() {
        if (deadAttribute != null) {
            return deadAttribute.booleanValue();
        }
        return false;
    }
    
    /**
     * 判断一个目标是否为敌人
     * @param target
     * @return 
     */
    public boolean isEnemy(Actor target) {
        if (target == null || target == actor) {
            return false;
        }
        // 如果目标分组值小于或等于0，则始终认为“不”是敌人，这样允许游戏添加一些无害的中立小动物
        ActorModule targetActorModule = target.getModule(ActorModule.class);
        if (targetActorModule.getGroup() <= 0) {
            return false;
        }
        return (targetActorModule.getGroup() != getGroup());
    }
    
    /**
     * 获取角色的分组（派系），如果没有给角色配置任何分组，则该方法始终返回0;
     * @return 
     */
    public int getGroup() {
        if (groupAttribute != null) {
            return groupAttribute.intValue();
        }
        return 0;
    }
    
    /**
     * 设置角色分组,如果当前模块没有绑定分组属性，则该方法什么也不做。
     * @param group 
     */
    public void setGroup(int group) {
        if (groupAttribute != null) {
            groupAttribute.setValue(group);
        }
    }
    
    /**
     * 获取角色的队伍，如果没有给角色配置任何队伍，则该方法始终返回0;
     * @return 
     */
    public int getTeam() {
        if (teamAttribute != null) {
            return teamAttribute.intValue();
        }
        return 0;
    }

    /**
     * 设置角色队伍,如果当前模块没有绑定队伍属性，则该方法什么也不做。
     * @param team 
     */
    public void setTeam(int team) {
        if (teamAttribute != null) {
            teamAttribute.setValue(team);
        }
    }
    
    /**
     * 获取角色的健康值
     * @return 
     */
    public int getHealth() {
        if (healthAttribute != null) {
            return healthAttribute.intValue();
        }
        return 0;
    }
    
    /**
     * 设置角色的健康值
     * @param health 
     */
    public void setHealth(int health) {
        if (healthAttribute != null) {
            healthAttribute.setValue(health);
        }
    }
    
    /**
     * 获取角色的视角距离,如果模块没有绑定角色的视角属性，则该方法始终返回0.
     * @return 
     */
    public float getViewDistance() {
        if (viewAttribute != null) {
            return viewAttribute.floatValue();
        }
        return 0;
    }
    
    /**
     * 设置角色的视角距离。
     * @param viewDistance 
     */
    public void setViewDistance(float viewDistance) {
        if (viewAttribute != null) {
            viewAttribute.setValue(viewDistance);
        }
    }
    
    /**
     * 获取角色当前的目标,如果没有或者目标角色不存在则返回null.
     * @return 
     */
    public Actor getTarget() {
        if (targetAttribute != null) {
            return playService.findActor(targetAttribute.longValue());
        }
        return null;
    }
    
    /**
     * 设置当前角色的目标对象
     * @param target 目标对象的唯一ID
     */
    public void setTarget(Actor target) {
        if (targetAttribute != null) {
            targetAttribute.setValue(target != null ? target.getData().getUniqueId() : -1L);
        }
    }
    
    @Override
    public void onValueChanged(Attribute attribute, Number oldValue, Number newValue) {
        // 当targetAttribute发生变化时触发侦听器
        if (attribute == targetAttribute) {
            // 释放旧目标的listener
            Actor oldTarget = playService.findActor(oldValue.longValue());
            if (oldTarget != null) {
                ActorModule oldTargetActorModule = oldTarget.getModule(ActorModule.class);
                if (oldTargetActorModule != null) {
                    oldTargetActorModule.notifyActorTargetReleasedListener(actor);
                }
            }
            // 锁定新目标的listener.
            Actor newTarget = playService.findActor(newValue.longValue());
            if (newTarget != null) {
                ActorModule newTargetActorModule = newTarget.getModule(ActorModule.class);
                if (newTargetActorModule != null) {
                    newTargetActorModule.notifyActorTargetLockedListener(actor);
                }
            }
            return;
        }
        // 角色质量属性在外部被改变时要更新innerControl.
        if (attribute == massAttribute) {
            innerControl.setMass(massAttribute.floatValue());
            return;
        }
        // 监听到健康值为0时，把角色标记为死亡
        if (attribute == healthAttribute) {
            if (deadAttribute != null) {
                deadAttribute.setValue(healthAttribute.intValue() <= 0);
            }
        }
    }
    
    /**
     * 获取角色当前的跟随目标
     * @return 
     */
    public long getFollowTarget() {
        if (followTargetAttribute != null) {
            return followTargetAttribute.longValue();
        }
        return 0;
    }
    
    /**
     * 设置角色当前的跟随目标
     * @param target  角色的唯一ID
     */
    public void setFollowTarget(long target) {
        if (followTargetAttribute != null) {
            followTargetAttribute.setValue(target);
        }
    }

    /**
     * 判断角色是否为“必不可少的”这种角色即使死亡也不应该被移除出场景
     * @return 
     */
    public boolean isEssential() {
        if (essentialAttribute != null) {
            return essentialAttribute.booleanValue();
        }
        return false;
    }

    /**
     * 设置角色为“必不可少的”这种角色即使死亡也不应该被移除出场景
     * @param essential 
     */
    public void setEssential(boolean essential) {
        if (essentialAttribute != null) {
            essentialAttribute.setValue(essential);
        }
    }
    
    /**
     * 判断角色是否为“生物”
     * @return 
     */
    public boolean isBiology() {
        return biologyAttribute != null && biologyAttribute.booleanValue();
    }
    
    /**
     * 设置角色为是否为“生物”类型角色
     * @param biology 
     */
    public void setBiology(boolean biology) {
        if (biologyAttribute != null) {
            biologyAttribute.setValue(biology);
        }
    }
    
    /**
     * 获取当前角色的所有者id,如果没有则返回0
     * @return 
     */
    public long getOwner() {
        if (ownerAttribute != null) {
            return ownerAttribute.longValue();
        }
        return 0;
    }

    /**
     * 设置当前角色的所有者
     * @param ownerId 
     */
    public void setOwner(long ownerId) {
        if (ownerAttribute != null) {
            ownerAttribute.setValue(ownerId);
        }
    }
    
    /**
     * 判断目标角色是否为一个玩家角色
     * @return 
     */
    public boolean isPlayer() {
        return player;
    }
    
    /**
     * 标记目标这“玩家”角色
     * @param player 
     */
    public void setPlayer(boolean player) {
        this.player = player;
    }
    
    /**
     * 获取角色的物理质量
     * @return 
     */
    public float getMass() {
        if (massAttribute != null) {
            return massAttribute.floatValue();
        }
        return 0;
    }
    
    /**
     * 设置角色的物理质量
     * @param mass 
     */
    public void setMass(float mass) {
        if (massAttribute != null) {
            massAttribute.setValue(mass);
            innerControl.setMass(mass);
        }
    }
    
    /**
     * 判断角色是否是可移动的
     * @return 
     */
    public boolean isMovable() {
        return movableAttribute != null && movableAttribute.booleanValue();
    }
    
    /**
     * 设置角色是否为可移动
     * @param movable 
     */
    public void setMovable(boolean movable) {
        if (movableAttribute != null) {
            movableAttribute.setValue(movable);
        }
    }
    
    /**
     * 判断角色是否为可转动，可转换朝向的
     * @return 
     */
    public boolean isRotatable() {
        return rotatableAttribute != null && rotatableAttribute.booleanValue();
    }
    
    /**
     * 设置角色为是否可转动，可转换朝向的
     * @param rotatable 
     */
    public void setRotatable(boolean rotatable) {
        if (rotatableAttribute != null) {
            rotatableAttribute.setValue(rotatable);
        }
    }
    
    /**
     * 执行“让当前角色被另一个角色击中的逻辑”，注：属性必须是NumberAttribute类型，否则什么也不做
     * @param hitter 发起hit的角色, hitter可以为null
     * @param hitAttribute 属性名称
     * @param hitValue apply到指定属性的值，可正可负
     */
    public void applyHit(Actor hitter, String hitAttribute, float hitValue) {
        NumberAttribute attr = attributeModule.getAttributeByName(hitAttribute, NumberAttribute.class);
        if (attr == null) {
            return;
        }
        boolean deadBefore = isDead();
        attr.add(hitValue);
        boolean deadAfter = isDead();
        // 这个killed用来判断角色是否由于这次攻击而死亡。
        boolean killed = !deadBefore && deadAfter;
        
        // 通过侦听器：当前角色被另一个家伙击中
        notifyActorHitByTarget(hitter, hitAttribute, hitValue, killed);
        
        // 通知攻击者，告诉攻击者：你已经击中一个目标。
        if (hitter != null) {
            ActorModule hitterActorModule = hitter.getModule(ActorModule.class);
            if (hitterActorModule != null) {
                hitterActorModule.notifyActorHitOtherListener(actor, hitAttribute, hitValue, killed);
            }
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色(actor)已经被某一个目标(lockedByTarget)锁定。
     * @param lockedByTarget 
     */
    private void notifyActorTargetLockedListener(Actor lockedByTarget) {
        LOG.log(Level.INFO, "notifyActorTargetLockedListener, sourceBeLocked={0}, lockedByTarget={1}"
                , new Object[] {actor.getData().getId(), lockedByTarget.getData().getId()});
        if (actorListeners != null) {
            for (ActorListener lis : actorListeners) {
                lis.onActorTargetLocked(actor, lockedByTarget);
            }
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色(actor)已经被某一个目标(releasedByTarget)释放锁定。
     * @param releasedByTarget 
     */
    private void notifyActorTargetReleasedListener(Actor releasedByTarget) {
        LOG.log(Level.INFO, "notifyActorTargetReleasedListener, sourceBeReleased={0}, releasedByTarget={1}"
                , new Object[] {actor.getData().getId(), releasedByTarget.getData().getId()});
        if (actorListeners != null) {
            for (ActorListener lis : actorListeners) {
                lis.onActorTargetReleased(actor, releasedByTarget);
            }
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色Hit了另一个目标角色.<br>
     * 注：这个方法只由ActorModule内部调用。
     * @param actorBeHit 被击中的角色
     * @param hitAttribute
     * @param hitValue
     * @param killedByHit
     */
    private void notifyActorHitOtherListener(Actor actorBeHit, String hitAttribute, float hitValue, boolean killedByHit) {
        LOG.log(Level.INFO, "notifyActorHitOtherListener, actorBeHit={0}, hitAttribute={1}, hitValue={2}, killedByHit={3}"
                , new Object[] {actorBeHit.getData().getId(), hitAttribute, hitValue, killedByHit});
        if (actorListeners != null) {
            for (ActorListener l : actorListeners) {
                l.onActorHitTarget(actor, actorBeHit, hitAttribute, hitValue, killedByHit);
            }
        }
    }
    
    /**
     * 通知侦听器，让侦听器知道当前角色被某一个目标角色击中
     * @param hitter
     * @param hitAttribute
     * @param hitValue
     * @param killedByHit 
     */
    private void notifyActorHitByTarget(Actor hitter, String hitAttribute, float hitValue, boolean killedByHit) {
        LOG.log(Level.INFO, "notifyActorHitByTarget, hitter={0}, hitAttribute={1}, hitValue={2}, killedByHit={3}"
                , new Object[] {hitter != null ? hitter.getData().getId() : null, hitAttribute, hitValue, killedByHit});
        if (actorListeners != null) {
            for (ActorListener l : actorListeners) {
                l.onActorHitByTarget(actor, hitter, hitAttribute, hitValue, killedByHit);
            }
        }
    }
    
}
