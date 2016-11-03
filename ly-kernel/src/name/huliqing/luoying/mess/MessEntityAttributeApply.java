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
public class MessEntityAttributeApply extends MessBase {
    
    private long source = -1;
    
    private long target;
    
    // 角色的属性名称
    private String attributeName;
    
    // 属性值
    private float applyValue;

    /**
     * 获取作用值的源角色，如果源Entity不存在，则这个值返回-1。
     * @return 
     */
    public long getSource() {
        return source;
    }

    /**
     * 设置发起源的id(Entity id), 这个发起源表示是哪一个Entity对目标产生了属性作用值。比如,当角色A对角色B的属性造成了
     * 3点伤害的时候，那么角色A就是发起源。如果没有特别的作用源，则这个参数可以设置为-1.
     * @param source 
     */
    public void setSource(long source) {
        this.source = source;
    }

    /**
     * 获取接收属性值作用的目标角色。
     * @return 
     */
    public long getTarget() {
        return target;
    }

    /**
     * 设置接受属性值作用的目标角色,或者说是被击中的角色。
     * @param target 
     */
    public void setTarget(long target) {
        this.target = target;
    }

    /**
     * 获取所作用的属性名称
     * @return 
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * 设置属性名称，目标（target）必须存在这个属性，并且必须是Number类型的，否则没有意义。
     * @param attributeName 
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * 获取属性的作用值。
     * @return 
     */
    public float getApplyValue() {
        return applyValue;
    }

    /**
     * 设置属性的作用值, 这个作用值将直接添加到目标target指定的属性上, 这个值可正可负。
     * @param value 
     */
    public void setApplyValue(float value) {
        this.applyValue = value;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Entity sourceEntity = playService.getEntity(this.source);
        Entity targetEntity = playService.getEntity(this.target);
        if (targetEntity != null && sourceEntity != null) {
            EntityService entityService = Factory.get(EntityService.class);
            entityService.applyNumberAttributeValue(targetEntity, attributeName, applyValue, sourceEntity);
        }
    }
    
}
