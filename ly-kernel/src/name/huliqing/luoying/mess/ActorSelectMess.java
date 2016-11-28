/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.mess;

import com.jme3.network.serializing.Serializable;
import name.huliqing.luoying.data.EntityData;

/**
 * 客户端发消息给服务端，请求服务端载入一个角色给客户端使用.主要用在客户端玩家选择角色游戏时。
 * @author huliqing
 */
@Serializable
public class ActorSelectMess extends GameMess {
    
    private EntityData entityData;

    public ActorSelectMess() {}
    
    public ActorSelectMess(EntityData entityData) {
        this.entityData = entityData;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public void setEntityData(EntityData entityData) {
        this.entityData = entityData;
    }
    
}
