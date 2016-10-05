/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.ly.Factory;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.object.actor.Actor;

/**
 *
 * @author huliqing
 */
@Serializable
public class MessAttributeNumberHit extends MessBase {
    
    private long sourceActor;
    
    private long targetActor;
    
    // 角色的属性名称
    private String attrName;
    
    // 属性值
    private float value;

    public long getSourceActor() {
        return sourceActor;
    }

    /**
     * 攻击者，或者施放hit动作的角色。
     * @param sourceActor 
     */
    public void setSourceActor(long sourceActor) {
        this.sourceActor = sourceActor;
    }

    public long getTargetActor() {
        return targetActor;
    }

    /**
     * 设置接受hit的目标角色,或者说是被击中的角色。
     * @param targetActor 
     */
    public void setTargetActor(long targetActor) {
        this.targetActor = targetActor;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void applyOnClient() {
        super.applyOnClient();
        PlayService playService = Factory.get(PlayService.class);
        ActorService actorService = Factory.get(ActorService.class);
        Actor source = playService.findActor(sourceActor);
        Actor target = playService.findActor(targetActor);
        if (target != null && source != null) {
            actorService.hitNumberAttribute(target, source, attrName, value);
        }
    }
    
}
