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
 * SC,同步角色的属性值
 * @author huliqing
 */
@Serializable
public class MessAttributeSync<V> extends MessBase {
    
    private long actorId;
    // 属性ID
    private String attrName;
    // 属性值
    private V value;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
   
    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        AttributeService attributeService = Factory.get(AttributeService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
//            attributeService.syncAttribute(actor, attribute, levelValue, staticValue, dynamicValue); // remove20160826
            attributeService.setAttributeValue(actor, attrName, value);
        }
    }

}
