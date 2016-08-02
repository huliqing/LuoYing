/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.network;

import name.huliqing.core.mvc.service.AttributeService;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public interface AttributeNetwork extends AttributeService {
    
    /**
     * 同步目标角色的指定属性值到客户端
     * @param actor
     * @param attributeId
     */
    void syncAttribute(Actor actor, String attributeId);
}
