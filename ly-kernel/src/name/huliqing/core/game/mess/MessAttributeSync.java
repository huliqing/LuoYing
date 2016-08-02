/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.game.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.core.Factory;
import name.huliqing.core.game.service.AttributeService;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.object.actor.Actor;

/**
 * SC,同步角色的属性值
 * @author huliqing
 */
@Serializable
public class MessAttributeSync extends MessBase {
    
    private long actorId;
    // 属性ID
    private String attribute;
    private float levelValue;
    private float staticValue;
    private float dynamicValue;

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public float getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(float levelValue) {
        this.levelValue = levelValue;
    }

    public float getStaticValue() {
        return staticValue;
    }

    public void setStaticValue(float staticValue) {
        this.staticValue = staticValue;
    }

    public float getDynamicValue() {
        return dynamicValue;
    }

    public void setDynamicValue(float dynamicValue) {
        this.dynamicValue = dynamicValue;
    }
   
    @Override
    public void applyOnClient() {
        PlayService playService = Factory.get(PlayService.class);
        AttributeService attributeService = Factory.get(AttributeService.class);
        Actor actor = playService.findActor(actorId);
        if (actor != null) {
            attributeService.syncAttribute(actor, attribute, levelValue, staticValue, dynamicValue);
        }
    }

}
