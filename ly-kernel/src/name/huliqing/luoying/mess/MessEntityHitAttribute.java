/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.HostedConnection;
import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 设置entity某个属性值的消息
 * @author huliqing
 */
@Serializable
public class MessEntityHitAttribute extends MessBase {
    
    // hitAttribute的受对象
    private long entityId;
    // 指定攻击哪一个属性
    private String attribute;
    // 指定属性值。
    private Object value;
    // 攻击源，这个角色可以为null的
    private Long hitterId;

    /**
     * 获取攻击源，攻击源可能不存在，所以这个方法可能返回null.
     * @return 
     */
    public Long getHitterId() {
        return hitterId;
    }

    /**
     * 设置攻击发起源
     * @param hitterId 
     */
    public void setHitterId(Long hitterId) {
        this.hitterId = hitterId;
    }

    public long getTargetId() {
        return entityId;
    }

    /**
     * 设置受攻击的角色
     * @param entityId 
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getAttribute() {
        return attribute;
    }

    /**
     * 设置攻击的是哪一个属性（名称）
     * @param attribute 
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 设置新的属性值。
     * @param value 
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        Entity hitter = null;
        if (hitterId != null) {
            hitter = playService.getEntity(hitterId);
        }
        Entity entity = playService.getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityService.class).hitAttribute(entity, attribute, value, hitter);
        }
    }

    @Override
    public void applyOnServer(GameServer gameServer, HostedConnection hc) {
        super.applyOnServer(gameServer, hc);
        PlayService playService = Factory.get(PlayService.class);
        Entity hitter = null;
        if (hitterId != null) {
            hitter = playService.getEntity(hitterId);
        }
        Entity entity = playService.getEntity(entityId);
        if (entity != null) {
            Factory.get(EntityNetwork.class).hitAttribute(entity, attribute, value, hitter);
        }
    }
    
}
