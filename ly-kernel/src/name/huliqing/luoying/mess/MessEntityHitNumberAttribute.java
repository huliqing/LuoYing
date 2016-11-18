/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 给目标Entity(target)的指定属性添加值，目标属性必须存在，并且必须是Number类型的，否则什么也不做。
 * @author huliqing
 */
@Serializable
public class MessEntityHitNumberAttribute extends MessBase {
    
    // 受攻击的对象
    private long entityId;
    
    // 角色的属性名称
    private String attribute;
    
    // 属性值
    private float addValue;

    // 攻击源的id，如果不存在则标记为-1
    private long hitterId = -1;
    
    /**
     * 获取作用值的源角色，如果源Entity不存在，则这个值返回-1。
     * @return 
     */
    public long getHitterId() {
        return hitterId;
    }

    /**
     * 设置发起源的id(Entity id), 这个发起源表示是哪一个Entity对目标产生了属性作用值。比如,当角色A对角色B的属性造成了
     * 3点伤害的时候，那么角色A就是发起源。
     * @param hitterId 
     */
    public void setHitterId(long hitterId) { 
        this.hitterId = hitterId;
    }

    /**
     * 获取接收属性值作用的目标角色。
     * @return 
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * 设置接受属性值作用的目标角色,或者说是被击中的角色。
     * @param entityId 
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * 获取所作用的属性名称
     * @return 
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * 设置属性名称，目标（target）必须存在这个属性，并且必须是Number类型的，否则没有意义。
     * @param attribute 
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * 获取属性的作用值。
     * @return 
     */
    public float getAddValue() {
        return addValue;
    }

    /**
     * 设置属性的作用值, 这个作用值将直接添加到目标target指定的属性上, 这个值可正可负。
     * @param addValue 
     */
    public void setAddValue(float addValue) {
        this.addValue = addValue;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Entity hitter = null;
        if (hitterId > 0) {
            hitter = playService.getEntity(hitterId);
        }
        Entity entity = playService.getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityService.class).hitNumberAttribute(entity, attribute, addValue, hitter);
        }
    }
}
