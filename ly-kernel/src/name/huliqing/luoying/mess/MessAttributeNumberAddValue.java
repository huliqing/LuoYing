/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.service.AttributeService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.entity.Entity;

/**
 * @author huliqing
 */
@Serializable
public class MessAttributeNumberAddValue extends MessBase {
    
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
        Entity actor = playService.getEntity(actorId);
        if (actor != null) {
            attributeService.addNumberAttributeValue(actor, attributeName, value);
        }
    }
    
}
