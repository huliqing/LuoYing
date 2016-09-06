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
import name.huliqing.core.data.ModuleData;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.attribute.Attribute;
import name.huliqing.core.object.attribute.BooleanAttribute;
import name.huliqing.core.object.attribute.NumberAttribute;
import name.huliqing.core.object.attribute.ValueChangeListener;

/**
 * 角色的基本控制器
 * @author huliqing
 * @param <T>
 */
public class ActorModule<T extends ModuleData> extends AbstractModule<T> implements ValueChangeListener<Number> {

    private static final Logger LOG = Logger.getLogger(ActorModule.class.getName());
    
    private final AttributeService attributeService = Factory.get(AttributeService.class);

    private Actor actor;
    private BetterCharacterControlWrap innerControl;
    private float radius = 0.4f;
    private float height = 3.2f;
    
    // 监听角色被目标锁定/释放,被击中,被杀死或杀死目标的侦听器
    private List<ActorListener> actorListeners;
    
    // 生命值属性名称
    private String bindLifeAttribute;
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
    private String bindLivingAttribute;
    
    private NumberAttribute lifeAttribute;
    private NumberAttribute groupAttribute;
    private NumberAttribute teamAttribute;
    private NumberAttribute viewAttribute;
    private NumberAttribute targetAttribute;
    private NumberAttribute followTargetAttribute;
    private NumberAttribute ownerAttribute;
    private NumberAttribute massAttribute;
    
    private BooleanAttribute essentialAttribute;
    private BooleanAttribute livingAttribute;
    
    // ---- ext
    // 标记目标角色是否为“玩家”角色,这个参数为程序动态设置,不存档
    private boolean player;
    
    @Override
    public void setData(T data) {
        super.setData(data);
        this.radius = data.getAsFloat("radius", radius);
        this.height = data.getAsFloat("height", height);
        this.bindLifeAttribute = data.getAsString("bindLifeAttribute");
        this.bindGroupAttribute = data.getAsString("bindGroupAttribute");
        this.bindTeamAttribute = data.getAsString("bindTeamAttribute");
        this.bindViewAttribute = data.getAsString("bindViewAttribute");
        this.bindTargetAttribute = data.getAsString("bindTargetAttribute");
        this.bindFollowTargetAttribute = data.getAsString("bindFollowTargetAttribute");
        this.bindOwnerAttribute = data.getAsString("bindOwnerAttribute");
        this.bindMassAttribute = data.getAsString("bindMassAttribute");
        
        this.bindEssentialAttribute = data.getAsString("bindEssentialAttribute");
        this.bindLivingAttribute = data.getAsString("bindLivingAttribute");
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
    }

    @Override
    public void initialize(Actor actor) {
        super.initialize(actor);
        this.actor = actor;
        
        lifeAttribute = attributeService.getAttributeByName(actor, bindLifeAttribute);
        if (lifeAttribute == null) {
            LOG.log(Level.WARNING, "Life attribute not found, by lifeAttributeName={0}, actorId={1}"
                    , new Object[] {bindLifeAttribute, actor.getData().getId()});
        }
        groupAttribute = attributeService.getAttributeByName(actor, bindGroupAttribute);
        teamAttribute = attributeService.getAttributeByName(actor, bindTeamAttribute);
        viewAttribute = attributeService.getAttributeByName(actor, bindViewAttribute);
        // 角色的当前目标属性
        // 注：这里要给targetAttribute加一个侦听器，以便targetAttribute在补外部改变的时候可以触发侦听器
        targetAttribute = attributeService.getAttributeByName(actor, bindTargetAttribute);
        targetAttribute.addListener(this);
        // 角色当前的跟随目标
        followTargetAttribute = attributeService.getAttributeByName(actor, bindFollowTargetAttribute);
        ownerAttribute = attributeService.getAttributeByName(actor, bindOwnerAttribute);
        // 角色质量
        // 注：角色质量可能被外部改变，所以要监听,当改变时要触发innerControl更新
        massAttribute = attributeService.getAttributeByName(actor, bindMassAttribute);
        massAttribute.addListener(this);
        essentialAttribute = attributeService.getAttributeByName(actor, bindEssentialAttribute);
        livingAttribute = attributeService.getAttributeByName(actor, bindLivingAttribute);
        
        // 控制器
        this.innerControl = new BetterCharacterControlWrap(radius, height, getMass());
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
        lifeAttribute = null;
        groupAttribute = null;
        teamAttribute = null;
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
        LOG.log(Level.INFO, "setWalkDirection, actor={0}, walkDirection={1}"
                , new Object[] {actor.getData().getId(), walkDirection});
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
            return lifeAttribute.intValue() <= 0;
        }
        return false;
    }
    
    /**
     * 判断一个目标是否为敌人
     * @param target
     * @return 
     */
    public boolean isEnemy(Actor target) {
        // 如果目标分组值小于或等于0，则始终认为“不”是敌人，这样允许游戏添加一些无害的中立小动物
        ActorModule targetActorModule = target.getModule(ActorModule.class);
        if (targetActorModule.getGroup() <= 0) {
            return false;
        }
        return (getGroup() != targetActorModule.getGroup());
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
     * 获取角色当前的目标对象的唯一ID
     * @return 
     */
    public long getTarget() {
        if (targetAttribute != null) {
            return targetAttribute.longValue();
        }
        return 0;
    }
    
    /**
     * 设置当前角色的目标对象
     * @param target 目标对象的唯一ID
     */
    public void setTarget(long target) {
        if (targetAttribute != null) {
            targetAttribute.setValue(target);
        }
    }
    
    @Override
    public void onValueChanged(Attribute attribute, Number oldValue, Number newValue) {
        // 当targetAttribute发生变化时触发侦听器
        if (attribute == targetAttribute) {
            // 释放旧目标的listener
            long oldTargetId = oldValue.longValue();
            if (oldTargetId > 0) {
                if (actorListeners != null && actorListeners.isEmpty()) {
                    for (int i = 0; i < actorListeners.size(); i++) {
                        actorListeners.get(i).onActorReleased(oldTargetId, actor);
                    }
                }
            }
            // 锁定新目标的listener.
            if (newValue.longValue() > 0) {
                if (actorListeners != null && !actorListeners.isEmpty()) {
                    for (int i = 0; i < actorListeners.size(); i++) {
                        actorListeners.get(i).onActorLocked(newValue.longValue(), actor);
                    }
                }
            }
            return;
        }
        // 角色质量属性在外部被改变时要更新innerControl.
        if (attribute == massAttribute) {
            innerControl.setMass(massAttribute.floatValue());
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
     * @param target 
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
    public boolean isLiving() {
        if (livingAttribute != null) {
            return livingAttribute.booleanValue();
        }
        return false;
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
    
    public float getMass() {
        if (massAttribute != null) {
            return massAttribute.floatValue();
        }
        return 0;
    }
    
    /**
     * 设置角色的质量
     * @param mass 
     */
    public void setMass(float mass) {
        if (massAttribute != null) {
            massAttribute.setValue(mass);
            innerControl.setMass(mass);
        }
    }
}
