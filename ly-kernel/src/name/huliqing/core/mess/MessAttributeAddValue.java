/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.actor.Actor;

/**
 * 设置角色的队伍分组
 * @author huliqing
 */
@Serializable
public class MessAttributeAddValue extends MessBase {
    
    private long actorId;

    private String attributeName;
    
    private float value;
    
    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        AttributeService attributeService = Factory.get(AttributeService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            attributeService.addAttributeValue(actor, attributeName, value);
        }
    }
    
}
